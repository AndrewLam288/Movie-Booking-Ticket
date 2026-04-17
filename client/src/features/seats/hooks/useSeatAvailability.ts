import { useCallback, useEffect, useState } from "react";
import { getSeatMap } from "../api/seatApi";
import { subscribeToSeatAvailability } from "../api/seatWebSocket";
import type { SeatAvailabilityEventDto, SeatItem } from "../types/seat";

function applySeatEvent(currentSeats: SeatItem[], event: SeatAvailabilityEventDto): SeatItem[] {
    return currentSeats.map((seat) => {
        if (seat.seatId !== event.seatId) {
            return seat;
        }

        const nextHeldBySessionId =
            event.status === "HELD" ? event.clientSessionId ?? null : null;

        return {
            ...seat,
            status: event.status,
            heldBySessionId: nextHeldBySessionId,
        };
    });
}

export function useSeatAvailability(showtimeId: number) {
    const [seats, setSeats] = useState<SeatItem[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const refreshSeatMap = useCallback(async () => {
        try {
            setLoading(true);
            setError("");

            const response = await getSeatMap(showtimeId);
            setSeats(response.seats);
        } catch (err) {
            setError(err instanceof Error ? err.message : "Failed to load seats.");
        } finally {
            setLoading(false);
        }
    }, [showtimeId]);

    useEffect(() => {
        if (!Number.isFinite(showtimeId)) {
            setError("Invalid showtime id.");
            setLoading(false);
            return;
        }

        void refreshSeatMap();
    }, [refreshSeatMap, showtimeId]);

    useEffect(() => {
        if (!Number.isFinite(showtimeId)) {
            return;
        }

        const subscription = subscribeToSeatAvailability(showtimeId, (event) => {
            setSeats((currentSeats) => applySeatEvent(currentSeats, event));
        });

        return () => {
            subscription.disconnect();
        };
    }, [showtimeId]);

    return {
        seats,
        loading,
        error,
    };
}