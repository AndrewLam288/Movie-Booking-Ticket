import { useEffect, useMemo, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { useAuth } from "../../auth/context/useAuth";
import { createBooking, getSeatMap } from "../api/seatApi";
import { getShowtimeById } from "../../showtimes/api/showtimesApi";
import type { ShowtimeDetail } from "../../showtimes/types/showtime";
import "./CheckoutPage.css";

interface CheckoutLocationState {
    movieId?: number;
    from?: string;
}

interface HeldSeatItem {
    seatId: number;
    seatLabel: string;
}

function getHeldSeatsForCurrentUser(
    seats: Array<{
        seatId: number;
        seatRow: string;
        seatNumber: number;
        status: string;
        heldBySessionId: string | null;
    }>,
    holderKey: string | null
): HeldSeatItem[] {
    if (!holderKey) {
        return [];
    }

    return seats
        .filter(
            (seat) =>
                seat.status === "HELD" &&
                seat.heldBySessionId === holderKey
        )
        .map((seat) => ({
            seatId: seat.seatId,
            seatLabel: `${seat.seatRow}${seat.seatNumber}`,
        }))
        .sort((a, b) => a.seatLabel.localeCompare(b.seatLabel));
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

export default function CheckoutPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const { showtimeId } = useParams<"showtimeId">();
    const { user } = useAuth();

    const parsedShowtimeId = Number(showtimeId);
    const routeState = (location.state as CheckoutLocationState | undefined) ?? {};
    const holderKey = user ? `USER:${user.userId}` : null;

    const [roomName, setRoomName] = useState("");
    const [heldSeats, setHeldSeats] = useState<HeldSeatItem[]>([]);
    const [showtimeDetail, setShowtimeDetail] = useState<ShowtimeDetail | null>(null);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        if (!Number.isFinite(parsedShowtimeId)) {
            setError("Invalid showtime id.");
            setLoading(false);
            return;
        }

        let mounted = true;

        async function loadCheckoutData() {
            try {
                setLoading(true);
                setError("");

                const [seatMap, showtime] = await Promise.all([
                    getSeatMap(parsedShowtimeId),
                    getShowtimeById(parsedShowtimeId),
                ]);

                if (!mounted) {
                    return;
                }

                setRoomName(seatMap.roomName);
                setHeldSeats(getHeldSeatsForCurrentUser(seatMap.seats, holderKey));
                setShowtimeDetail(showtime);
            } catch (err) {
                if (!mounted) {
                    return;
                }

                setError(err instanceof Error ? err.message : "Failed to load checkout details.");
            } finally {
                if (mounted) {
                    setLoading(false);
                }
            }
        }

        void loadCheckoutData();

        return () => {
            mounted = false;
        };
    }, [parsedShowtimeId, holderKey]);

    const seatIds = useMemo(() => heldSeats.map((seat) => seat.seatId), [heldSeats]);

    async function handleCompleteCheckout() {
        if (!parsedShowtimeId || seatIds.length === 0) {
            return;
        }

        try {
            setSubmitting(true);
            setError("");

            const booking = await createBooking({
                showtimeId: parsedShowtimeId,
                seatIds,
            });

            navigate(`/booking-confirmed/${booking.bookingCode}`, {
                replace: true,
                state: {
                    booking,
                    movieId: routeState.movieId,
                },
            });
        } catch (err) {
            setError(err instanceof Error ? err.message : "Failed to complete checkout.");
        } finally {
            setSubmitting(false);
        }
    }

    if (!Number.isFinite(parsedShowtimeId)) {
        return <div className="checkout-page">Invalid showtime id.</div>;
    }

    return (
        <div className="checkout-page">
            <div className="checkout-page__header">
                <div>
                    <p className="checkout-page__eyebrow">Checkout</p>
                    <h1 className="checkout-page__title">Review Your Booking</h1>
                    <p className="checkout-page__subtitle">
                        Confirm your held seats and complete checkout.
                    </p>
                </div>

                <button
                    type="button"
                    className="checkout-page__back-button"
                    onClick={() =>
                        navigate(routeState.from ?? `/buy-tickets/${routeState.movieId ?? ""}`)
                    }
                >
                    Back
                </button>
            </div>

            {error ? <div className="checkout-page__error">{error}</div> : null}

            {loading ? (
                <div className="checkout-page__card">Loading checkout details...</div>
            ) : (
                <div className="checkout-page__layout">
                    <section className="checkout-page__card">
                        <h2 className="checkout-page__section-title">Booking Summary</h2>

                        <div className="checkout-page__summary-row">
                            <span>Movie</span>
                            <strong>{showtimeDetail?.movieTitle ?? "—"}</strong>
                        </div>

                        <div className="checkout-page__summary-row">
                            <span>Showtime</span>
                            <strong>{formatShowtimeDateTime(showtimeDetail?.startTime)}</strong>
                        </div>

                        <div className="checkout-page__summary-row">
                            <span>Ends</span>
                            <strong>{formatShowtimeDateTime(showtimeDetail?.endTime)}</strong>
                        </div>

                        <div className="checkout-page__summary-row">
                            <span>Base Price</span>
                            <strong>
                                {typeof showtimeDetail?.basePrice === "number"
                                    ? `$${showtimeDetail.basePrice.toFixed(2)}`
                                    : "—"}
                            </strong>
                        </div>

                        <div className="checkout-page__summary-row">
                            <span>Room</span>
                            <strong>{roomName || showtimeDetail?.roomName || "—"}</strong>
                        </div>

                        <div className="checkout-page__summary-row checkout-page__summary-row--top">
                            <span>Seats</span>
                            <div className="checkout-page__chips">
                                {heldSeats.length > 0 ? (
                                    heldSeats.map((seat) => (
                                        <span key={seat.seatId} className="checkout-page__chip">
                                            {seat.seatLabel}
                                        </span>
                                    ))
                                ) : (
                                    <span className="checkout-page__empty">
                                        No active held seats found.
                                    </span>
                                )}
                            </div>
                        </div>
                    </section>

                    <aside className="checkout-page__card">
                        <h2 className="checkout-page__section-title">Payment</h2>
                        <p className="checkout-page__placeholder-text">
                            Payment integration will be added later. This is a placeholder checkout step.
                        </p>

                        <div className="checkout-page__payment-options">
                            <button type="button" className="checkout-page__payment-option" disabled>
                                Credit / Debit Card
                            </button>
                            <button type="button" className="checkout-page__payment-option" disabled>
                                Apple Pay / Google Pay
                            </button>
                            <button type="button" className="checkout-page__payment-option" disabled>
                                PayPal
                            </button>
                        </div>

                        <button
                            type="button"
                            className="checkout-page__complete-button"
                            onClick={handleCompleteCheckout}
                            disabled={heldSeats.length === 0 || submitting}
                        >
                            {submitting ? "Completing..." : "Complete Checkout"}
                        </button>
                    </aside>
                </div>
            )}
        </div>
    );
}