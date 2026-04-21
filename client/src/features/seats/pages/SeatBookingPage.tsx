import { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import SeatGrid from '../components/SeatGrid';
import SeatLegend from '../components/SeatLegend';
import SeatBookingSummary from '../components/SeatBookingSummary';
import { useSeatBooking } from '../hooks/useSeatBooking';
import './SeatBookingPage.css';
import { useAuth } from '../../auth/context/AuthContext';
import { getShowtimesByMovieId } from '../../showtimes/api/showtimesApi';
import type { ShowtimeSummary } from '../../showtimes/types/showtime';

function buildShowtimeLabel(showtime: ShowtimeSummary): string {
    const cinemaName =
        'cinemaName' in showtime && typeof showtime.cinemaName === 'string'
            ? showtime.cinemaName
            : 'Cinema';

    const startTime =
        'startTime' in showtime && typeof showtime.startTime === 'string'
            ? showtime.startTime
            : `Showtime ${showtime.id}`;

    return `${startTime} - ${cinemaName}`;
}

export default function SeatBookingPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const { movieId } = useParams<'movieId'>();
    const { user } = useAuth();

    const parsedMovieId = Number(movieId);

    const [showtimeOptions, setShowtimeOptions] = useState<ShowtimeSummary[]>([]);
    const [selectedShowtimeId, setSelectedShowtimeId] = useState<number | null>(null);
    const [showtimesLoading, setShowtimesLoading] = useState(true);
    const [showtimesError, setShowtimesError] = useState('');

    const holderKey = user ? `USER:${user.userId}` : null;

    useEffect(() => {
        if (!Number.isFinite(parsedMovieId)) {
            setShowtimeOptions([]);
            setSelectedShowtimeId(null);
            setShowtimesLoading(false);
            setShowtimesError('Invalid movie id.');
            return;
        }

        let mounted = true;

        async function loadShowtimes() {
            try {
                setShowtimesLoading(true);
                setShowtimesError('');

                const data = await getShowtimesByMovieId(parsedMovieId);

                if (!mounted) {
                    return;
                }

                setShowtimeOptions(data);
                setSelectedShowtimeId(data.length > 0 ? data[0].id : null);
            } catch (err) {
                if (!mounted) {
                    return;
                }

                setShowtimeOptions([]);
                setSelectedShowtimeId(null);
                setShowtimesError(
                    err instanceof Error ? err.message : 'Failed to load showtimes.'
                );
            } finally {
                if (mounted) {
                    setShowtimesLoading(false);
                }
            }
        }

        void loadShowtimes();

        return () => {
            mounted = false;
        };
    }, [parsedMovieId]);

    const {
        roomName,
        seats,
        selectedLabels,
        pendingSeatIds,
        loading,
        bookingLoading,
        error,
        toggleSeat,
        clearSelection,
    } = useSeatBooking(selectedShowtimeId, holderKey);

    function handleContinueToCheckout() {
        if (!selectedShowtimeId || selectedLabels.length === 0) {
            return;
        }

        navigate(`/checkout/showtimes/${selectedShowtimeId}`, {
            state: {
                movieId: parsedMovieId,
                from: location.pathname,
            },
        });
    }

    if (!Number.isFinite(parsedMovieId)) {
        return <div className="seat-booking-page">Invalid movie id.</div>;
    }

    return (
        <div className="seat-booking-page">
            <div className="seat-booking-page__header">
                <div>
                    <p className="seat-booking-page__eyebrow">Seat Booking</p>
                    <h1 className="seat-booking-page__title">Choose Your Seats</h1>
                    <p className="seat-booking-page__subtitle">
                        Select a showtime, hold seats, then continue to checkout.
                    </p>
                </div>

                <button
                    type="button"
                    className="seat-booking-page__back-button"
                    onClick={() => navigate(-1)}
                >
                    Back
                </button>
            </div>

            {showtimesError ? (
                <div className="seat-booking-page__error">{showtimesError}</div>
            ) : null}

            {error ? <div className="seat-booking-page__error">{error}</div> : null}

            <div className="seat-booking-page__layout">
                <section className="seat-booking-page__main-card">
                    <div className="seat-booking-page__top-controls">
                        <div className="seat-booking-page__field">
                            <label htmlFor="showtime-select" className="seat-booking-page__label">
                                Showtime
                            </label>

                            <select
                                id="showtime-select"
                                className="seat-booking-page__select"
                                value={selectedShowtimeId ?? ''}
                                onChange={(event) =>
                                    setSelectedShowtimeId(Number(event.target.value))
                                }
                                disabled={showtimesLoading || showtimeOptions.length === 0}
                            >
                                {showtimeOptions.map((showtime) => (
                                    <option key={showtime.id} value={showtime.id}>
                                        {buildShowtimeLabel(showtime)}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </div>

                    <h2 className="seat-booking-page__section-title">Seats Availability</h2>
                    <SeatLegend />

                    {!selectedShowtimeId ? (
                        <div className="seat-booking-page__loading">
                            No showtimes available for this movie.
                        </div>
                    ) : loading ? (
                        <div className="seat-booking-page__loading">Loading seat map...</div>
                    ) : (
                        <SeatGrid
                            seats={seats}
                            selectedSeatIds={seats
                                .filter(
                                    (seat) =>
                                        seat.status === 'HELD' &&
                                        seat.heldBySessionId === holderKey
                                )
                                .map((seat) => seat.seatId)}
                            pendingSeatIds={pendingSeatIds}
                            onSeatClick={toggleSeat}
                        />
                    )}
                </section>

                <SeatBookingSummary
                    roomName={roomName}
                    selectedLabels={selectedLabels}
                    bookingLoading={bookingLoading}
                    primaryButtonLabel="Continue to Checkout"
                    onConfirm={handleContinueToCheckout}
                    onClear={clearSelection}
                />
            </div>
        </div>
    );
}