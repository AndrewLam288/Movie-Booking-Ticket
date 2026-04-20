import axios from 'axios';
import { useCallback, useEffect, useMemo, useState } from 'react';
import {
    createBooking,
    getSeatMap,
    holdSeat,
    releaseSeat,
} from '../api/seatApi';
import { subscribeToSeatAvailability } from '../api/seatWebSocket';
import type {
    BookingResponseDto,
    SeatAvailabilityEventDto,
    SeatItem,
} from '../types/seat';

function getErrorMessage(error: unknown): string {
    if (axios.isAxiosError(error)) {
        const responseData = error.response?.data;

        if (typeof responseData === 'string' && responseData.trim()) {
            return responseData;
        }

        if (
            responseData &&
            typeof responseData === 'object' &&
            'message' in responseData &&
            typeof responseData.message === 'string'
        ) {
            return responseData.message;
        }

        if (error.message) {
            return error.message;
        }
    }

    if (error instanceof Error && error.message) {
        return error.message;
    }

    return 'Something went wrong. Please try again.';
}

function applySeatEvent(currentSeats: SeatItem[], event: SeatAvailabilityEventDto): SeatItem[] {
    return currentSeats.map((seat) => {
        if (seat.seatId !== event.seatId) {
            return seat;
        }

        return {
            ...seat,
            status: event.status,
            heldBySessionId: event.status === 'HELD' ? event.clientSessionId ?? null : null,
        };
    });
}

function getSelectedSeatIdsFromSeats(seats: SeatItem[], holderKey: string | null): number[] {
    if (!holderKey) {
        return [];
    }

    return seats
        .filter(
            (seat) =>
                seat.status === 'HELD' &&
                seat.heldBySessionId === holderKey
        )
        .map((seat) => seat.seatId);
}

export function useSeatBooking(showtimeId: number | null, holderKey: string | null) {
    const [roomName, setRoomName] = useState('');
    const [seats, setSeats] = useState<SeatItem[]>([]);
    const [pendingSeatIds, setPendingSeatIds] = useState<number[]>([]);
    const [loading, setLoading] = useState(false);
    const [bookingLoading, setBookingLoading] = useState(false);
    const [error, setError] = useState('');
    const [bookingResult, setBookingResult] = useState<BookingResponseDto | null>(null);

    useEffect(() => {
        setRoomName('');
        setSeats([]);
        setPendingSeatIds([]);
        setBookingResult(null);
        setError('');
        setLoading(false);
        setBookingLoading(false);
    }, [showtimeId]);

    const refreshSeatMap = useCallback(async () => {
        if (!showtimeId) {
            setSeats([]);
            setRoomName('');
            setLoading(false);
            return;
        }

        try {
            setLoading(true);
            setError('');

            const response = await getSeatMap(showtimeId);
            setRoomName(response.roomName);
            setSeats(response.seats);
        } catch (err) {
            setError(getErrorMessage(err));
        } finally {
            setLoading(false);
        }
    }, [showtimeId]);

    useEffect(() => {
        void refreshSeatMap();
    }, [refreshSeatMap]);

    useEffect(() => {
        if (!showtimeId) {
            return;
        }

        const subscription = subscribeToSeatAvailability(showtimeId, (event) => {
            setSeats((currentSeats) => applySeatEvent(currentSeats, event));
        });

        return () => {
            subscription.disconnect();
        };
    }, [showtimeId]);

    const selectedSeatIds = useMemo(() => {
        return getSelectedSeatIdsFromSeats(seats, holderKey);
    }, [seats, holderKey]);

    const selectedLabels = useMemo(() => {
        return seats
            .filter((seat) => selectedSeatIds.includes(seat.seatId))
            .sort((a, b) => {
                if (a.seatRow === b.seatRow) {
                    return a.seatNumber - b.seatNumber;
                }

                return a.seatRow.localeCompare(b.seatRow);
            })
            .map((seat) => `${seat.seatRow}${seat.seatNumber}`);
    }, [seats, selectedSeatIds]);

    const toggleSeat = useCallback(
        async (seat: SeatItem) => {
            if (!showtimeId) {
                return;
            }

            if (pendingSeatIds.includes(seat.seatId)) {
                return;
            }

            const isSelected = selectedSeatIds.includes(seat.seatId);

            if (seat.status === 'BOOKED' || seat.status === 'UNAVAILABLE') {
                return;
            }

            if (!isSelected && seat.status !== 'AVAILABLE') {
                return;
            }

            setError('');
            setPendingSeatIds((current) => [...current, seat.seatId]);

            try {
                if (isSelected) {
                    await releaseSeat(showtimeId, seat.seatId);
                    await refreshSeatMap();
                } else {
                    await holdSeat(showtimeId, seat.seatId);
                    await refreshSeatMap();
                }
            } catch (err) {
                setError(getErrorMessage(err));
                await refreshSeatMap();
            } finally {
                setPendingSeatIds((current) =>
                    current.filter((id) => id !== seat.seatId)
                );
            }
        },
        [pendingSeatIds, refreshSeatMap, selectedSeatIds, showtimeId]
    );

    const confirmBooking = useCallback(async () => {
        if (!showtimeId || !selectedSeatIds.length) {
            return;
        }

        try {
            setBookingLoading(true);
            setError('');

            const booking = await createBooking({
                showtimeId,
                seatIds: selectedSeatIds,
            });

            setBookingResult(booking);
            await refreshSeatMap();
        } catch (err) {
            setError(getErrorMessage(err));
            await refreshSeatMap();
        } finally {
            setBookingLoading(false);
        }
    }, [refreshSeatMap, selectedSeatIds, showtimeId]);

    const clearSelection = useCallback(async () => {
        if (!showtimeId || !selectedSeatIds.length) {
            return;
        }

        try {
            setError('');

            await Promise.all(
                selectedSeatIds.map((seatId) => releaseSeat(showtimeId, seatId))
            );

            await refreshSeatMap();
        } catch (err) {
            setError(getErrorMessage(err));
            await refreshSeatMap();
        }
    }, [refreshSeatMap, selectedSeatIds, showtimeId]);

    return {
        roomName,
        seats,
        selectedSeatIds,
        selectedLabels,
        pendingSeatIds,
        loading,
        bookingLoading,
        error,
        bookingResult,
        toggleSeat,
        confirmBooking,
        clearSelection,
    };
}