import { useEffect, useState } from "react";
import { Link, useLocation, useParams } from "react-router-dom";
import { getBookingByCode } from "../api/seatApi";
import { getShowtimeById } from "../../showtimes/api/showtimesApi";
import type { BookingResponseDto } from "../types/seat";
import type { ShowtimeDetail } from "../../showtimes/types/showtime";
import "./BookingConfirmedPage.css";

interface BookingConfirmedLocationState {
    booking?: BookingResponseDto;
}

function formatShowtimeDateTime(value?: string): string {
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

export default function BookingConfirmedPage() {
    const location = useLocation();
    const { bookingCode } = useParams<"bookingCode">();

    const [booking, setBooking] = useState<BookingResponseDto | null>(
        (location.state as BookingConfirmedLocationState | undefined)?.booking ?? null
    );
    const [showtimeDetail, setShowtimeDetail] = useState<ShowtimeDetail | null>(null);
    const [loading, setLoading] = useState(!booking);
    const [error, setError] = useState("");

    useEffect(() => {
        if (booking || !bookingCode) {
            return;
        }

        const currentBookingCode = bookingCode;
        let mounted = true;

        async function loadBooking() {
            try {
                setLoading(true);
                setError("");

                const data = await getBookingByCode(currentBookingCode);

                if (mounted) {
                    setBooking(data);
                }
            } catch (err) {
                if (mounted) {
                    setError(err instanceof Error ? err.message : "Failed to load booking.");
                }
            } finally {
                if (mounted) {
                    setLoading(false);
                }
            }
        }

        void loadBooking();

        return () => {
            mounted = false;
        };
    }, [booking, bookingCode]);

    useEffect(() => {
        if (!booking) {
            return;
        }

        const currentBooking = booking;
        let mounted = true;

        async function loadShowtimeDetail() {
            try {
                const data = await getShowtimeById(currentBooking.showtimeId);

                if (mounted) {
                    setShowtimeDetail(data);
                }
            } catch {
                if (mounted) {
                    setShowtimeDetail(null);
                }
            }
        }

        void loadShowtimeDetail();

        return () => {
            mounted = false;
        };
    }, [booking]);

    return (
        <div className="booking-confirmed-page">
            <div className="booking-confirmed-page__card">
                <p className="booking-confirmed-page__eyebrow">Booking Completed</p>
                <h1 className="booking-confirmed-page__title">Your booking is confirmed</h1>

                {loading ? (
                    <p className="booking-confirmed-page__text">Loading booking details...</p>
                ) : error ? (
                    <p className="booking-confirmed-page__error">{error}</p>
                ) : booking ? (
                    <>
                        <div className="booking-confirmed-page__summary">
                            <div className="booking-confirmed-page__row">
                                <span>Booking Code</span>
                                <strong>{booking.bookingCode}</strong>
                            </div>

                            <div className="booking-confirmed-page__row">
                                <span>Movie</span>
                                <strong>{showtimeDetail?.movieTitle ?? "—"}</strong>
                            </div>

                            <div className="booking-confirmed-page__row">
                                <span>Showtime</span>
                                <strong>{formatShowtimeDateTime(showtimeDetail?.startTime)}</strong>
                            </div>

                            <div className="booking-confirmed-page__row">
                                <span>Ends</span>
                                <strong>{formatShowtimeDateTime(showtimeDetail?.endTime)}</strong>
                            </div>

                            <div className="booking-confirmed-page__row">
                                <span>Base Price</span>
                                <strong>
                                    {typeof showtimeDetail?.basePrice === "number"
                                        ? `$${showtimeDetail.basePrice.toFixed(2)}`
                                        : "—"}
                                </strong>
                            </div>

                            <div className="booking-confirmed-page__row">
                                <span>Room</span>
                                <strong>{showtimeDetail?.roomName ?? "—"}</strong>
                            </div>

                            <div className="booking-confirmed-page__row booking-confirmed-page__row--top">
                                <span>Seats</span>
                                <div className="booking-confirmed-page__chips">
                                    {booking.seats.map((seat) => (
                                        <span
                                            key={seat.seatId}
                                            className="booking-confirmed-page__chip"
                                        >
                                            {seat.seatLabel}
                                        </span>
                                    ))}
                                </div>
                            </div>

                            <div className="booking-confirmed-page__row">
                                <span>Total</span>
                                <strong>${booking.totalAmount.toFixed(2)}</strong>
                            </div>
                        </div>

                        <div className="booking-confirmed-page__actions">
                            <Link to="/movies" className="booking-confirmed-page__button">
                                Back to Movies
                            </Link>
                        </div>
                    </>
                ) : (
                    <p className="booking-confirmed-page__text">Booking not found.</p>
                )}
            </div>
        </div>
    );
}