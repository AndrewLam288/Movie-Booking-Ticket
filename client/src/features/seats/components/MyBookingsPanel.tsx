import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getMyBookings } from "../api/seatApi";
import { getShowtimeById } from "../../showtimes/api/showtimesApi";
import type { BookingResponseDto } from "../types/seat";
import type { ShowtimeDetail } from "../../showtimes/types/showtime";
import "./MyBookingsPanel.css";

interface BookingWithShowtime {
    booking: BookingResponseDto;
    showtimeDetail: ShowtimeDetail | null;
}

function formatDateTime(value?: string): string {
    if (!value) {
        return "—";
    }

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
        return value.replace("T", " ");
    }

    return new Intl.DateTimeFormat("en-US", {
        month: "short",
        day: "numeric",
        year: "numeric",
        hour: "numeric",
        minute: "2-digit",
    }).format(date);
}

export default function MyBookingsPanel() {
    const [items, setItems] = useState<BookingWithShowtime[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        let mounted = true;

        async function loadBookings() {
            try {
                setLoading(true);
                setError("");

                const bookings = await getMyBookings();

                const enriched = await Promise.all(
                    bookings.map(async (booking) => {
                        try {
                            const showtimeDetail = await getShowtimeById(booking.showtimeId);
                            return { booking, showtimeDetail };
                        } catch {
                            return { booking, showtimeDetail: null };
                        }
                    })
                );

                if (mounted) {
                    setItems(enriched);
                }
            } catch (err) {
                if (mounted) {
                    setError(err instanceof Error ? err.message : "Failed to load booking history.");
                }
            } finally {
                if (mounted) {
                    setLoading(false);
                }
            }
        }

        void loadBookings();

        return () => {
            mounted = false;
        };
    }, []);

    if (loading) {
        return (
            <div className="my-bookings-panel__box">
                Loading your booking history...
            </div>
        );
    }

    if (error) {
        return (
            <div className="my-bookings-panel__box my-bookings-panel__box--error">
                {error}
            </div>
        );
    }

    if (items.length === 0) {
        return (
            <div className="my-bookings-panel__box">
                No booking data is available yet.
            </div>
        );
    }

    return (
        <div className="my-bookings-panel">
            {items.map(({ booking, showtimeDetail }) => (
                <article key={booking.bookingCode} className="my-bookings-panel__card">
                    <div className="my-bookings-panel__header">
                        <div>
                            <p className="my-bookings-panel__eyebrow">Booking</p>
                            <h3 className="my-bookings-panel__title">
                                {showtimeDetail?.movieTitle ?? "Movie"}
                            </h3>
                        </div>

                        <span className="my-bookings-panel__status">
                            {booking.status}
                        </span>
                    </div>

                    <div className="my-bookings-panel__grid">
                        <div className="my-bookings-panel__row">
                            <span>Booking Code</span>
                            <strong>{booking.bookingCode}</strong>
                        </div>

                        <div className="my-bookings-panel__row">
                            <span>Booked At</span>
                            <strong>{formatDateTime(booking.bookedAt)}</strong>
                        </div>

                        <div className="my-bookings-panel__row">
                            <span>Showtime</span>
                            <strong>{formatDateTime(showtimeDetail?.startTime)}</strong>
                        </div>

                        <div className="my-bookings-panel__row">
                            <span>Ends</span>
                            <strong>{formatDateTime(showtimeDetail?.endTime)}</strong>
                        </div>

                        <div className="my-bookings-panel__row">
                            <span>Room</span>
                            <strong>{showtimeDetail?.roomName ?? "—"}</strong>
                        </div>

                        <div className="my-bookings-panel__row">
                            <span>Total</span>
                            <strong>${booking.totalAmount.toFixed(2)}</strong>
                        </div>
                    </div>

                    <div className="my-bookings-panel__section">
                        <span className="my-bookings-panel__label">Seats</span>
                        <div className="my-bookings-panel__chips">
                            {booking.seats.map((seat) => (
                                <span key={seat.seatId} className="my-bookings-panel__chip">
                                    {seat.seatLabel}
                                </span>
                            ))}
                        </div>
                    </div>

                    <div className="my-bookings-panel__actions">
                        <Link
                            to={`/booking-confirmed/${booking.bookingCode}`}
                            className="my-bookings-panel__button"
                        >
                            View Receipt
                        </Link>
                    </div>
                </article>
            ))}
        </div>
    );
}