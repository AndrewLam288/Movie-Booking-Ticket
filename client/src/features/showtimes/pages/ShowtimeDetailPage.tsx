import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getShowtimeById } from "../api/showtimesApi";
import type { ShowtimeDetail } from "../types/showtime";
import "./ShowtimeDetailPage.css";

function formatDateTime(value: string): string {
    return new Date(value).toLocaleString();
}

function formatPrice(value: number): string {
    return new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD",
    }).format(value);
}

function buildLocation(showtime: ShowtimeDetail): string {
    return [showtime.cinemaCity, showtime.cinemaState, showtime.cinemaCountry]
        .filter(Boolean)
        .join(", ");
}

function formatFormat(format: ShowtimeDetail["format"]): string {
    switch (format) {
        case "TWO_D":
            return "Two-D";
        case "THREE_D":
            return "Three-D";
        case "FOUR_DX":
            return "Four-DX";
        default:
            return format;
    }
}

function formatStatus(status: ShowtimeDetail["status"]): string {
    return status.charAt(0) + status.slice(1).toLowerCase();
}

export default function ShowtimeDetailPage() {
    const { showtimeId } = useParams();
    const parsedShowtimeId = Number(showtimeId);

    const [showtime, setShowtime] = useState<ShowtimeDetail | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        let isMounted = true;

        async function loadShowtime() {
            if (!Number.isFinite(parsedShowtimeId)) {
                setError("Invalid showtime id.");
                setLoading(false);
                return;
            }

            try {
                setLoading(true);
                setError("");

                const data = await getShowtimeById(parsedShowtimeId);

                if (isMounted) {
                    setShowtime(data);
                }
            } catch (err) {
                if (isMounted) {
                    setError(err instanceof Error ? err.message : "Failed to load showtime.");
                }
            } finally {
                if (isMounted) {
                    setLoading(false);
                }
            }
        }

        void loadShowtime();

        return () => {
            isMounted = false;
        };
    }, [parsedShowtimeId]);

    if (loading) {
        return <section className="showtime-detail-page">Loading showtime...</section>;
    }

    if (error) {
        return (
            <section className="showtime-detail-page showtime-detail-page__error">
                {error}
            </section>
        );
    }

    if (!showtime) {
        return <section className="showtime-detail-page">Showtime not found.</section>;
    }

    return (
        <section className="showtime-detail-page">
            <div className="showtime-detail-page__header">
                <h1>{showtime.movieTitle}</h1>
            </div>

            <div className="showtime-detail-page__section">
                <div className="showtime-detail-page__detail-panel">
                    <div className="showtime-detail-page__rows">
                        <div className="showtime-detail-page__row">
                            <span className="showtime-detail-page__label">Format</span>
                            <span className="showtime-detail-page__value">
                                {formatFormat(showtime.format)}
                            </span>
                        </div>

                        <div className="showtime-detail-page__row">
                            <span className="showtime-detail-page__label">Status</span>
                            <span className="showtime-detail-page__value">
                                {formatStatus(showtime.status)}
                            </span>
                        </div>

                        <div className="showtime-detail-page__row">
                            <span className="showtime-detail-page__label">Room</span>
                            <span className="showtime-detail-page__value">{showtime.roomName}</span>
                        </div>

                        <div className="showtime-detail-page__row">
                            <span className="showtime-detail-page__label">Start</span>
                            <span className="showtime-detail-page__value">
                                {formatDateTime(showtime.startTime)}
                            </span>
                        </div>

                        <div className="showtime-detail-page__row">
                            <span className="showtime-detail-page__label">End</span>
                            <span className="showtime-detail-page__value">
                                {formatDateTime(showtime.endTime)}
                            </span>
                        </div>
                    </div>

                    <p className="showtime-detail-page__price">
                        Price: <strong>{formatPrice(showtime.basePrice)}</strong>
                    </p>
                </div>

                <aside className="showtime-detail-page__cinema-panel">
                    <h2>Cinema</h2>
                    <p>{showtime.cinemaName}</p>
                    <p>{showtime.cinemaAddressLine}</p>
                    <p>{buildLocation(showtime)}</p>
                    <p>Postal code: {showtime.cinemaPostalCode}</p>
                </aside>
            </div>

            <div className="showtime-detail-page__footer">
                <Link
                    className="showtime-detail-page__back-link"
                    to={`/cinemas/${showtime.cinemaId}`}
                >
                    Back to cinema
                </Link>
            </div>
        </section>
    );
}