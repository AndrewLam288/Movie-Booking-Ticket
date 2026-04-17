import { Link } from "react-router-dom";
import type { ShowtimeSummary } from "../types/showtime";
import "./ShowtimeList.css";

type ShowtimeListProps = {
    showtimes: ShowtimeSummary[];
};

function formatDateTime(value: string): string {
    return new Date(value).toLocaleString();
}

function formatPrice(value: number): string {
    return new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD",
    }).format(value);
}

function formatFormat(format: ShowtimeSummary["format"]): string {
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

function formatStatus(status: ShowtimeSummary["status"]): string {
    return status.charAt(0) + status.slice(1).toLowerCase();
}

export default function ShowtimeList({ showtimes }: ShowtimeListProps) {
    if (showtimes.length === 0) {
        return <p className="showtime-list__empty">No showtimes available yet.</p>;
    }

    return (
        <div className="showtime-list">
            {showtimes.map((showtime) => (
                <article key={showtime.id} className="showtime-card">
                    <div className="showtime-card__top">
                        <h3 className="showtime-card__title">{showtime.movieTitle}</h3>
                        <span className="showtime-card__badge">
                            {formatFormat(showtime.format)}
                        </span>
                    </div>

                    <div className="showtime-card__details">
                        <div className="showtime-card__detail-row">
                            <span className="showtime-card__label">Room</span>
                            <span className="showtime-card__value">{showtime.roomName}</span>
                        </div>

                        <div className="showtime-card__detail-row">
                            <span className="showtime-card__label">Start</span>
                            <span className="showtime-card__value">
                                {formatDateTime(showtime.startTime)}
                            </span>
                        </div>

                        <div className="showtime-card__detail-row">
                            <span className="showtime-card__label">End</span>
                            <span className="showtime-card__value">
                                {formatDateTime(showtime.endTime)}
                            </span>
                        </div>

                        <div className="showtime-card__detail-row">
                            <span className="showtime-card__label">Status</span>
                            <span className="showtime-card__value">
                                {formatStatus(showtime.status)}
                            </span>
                        </div>
                    </div>

                    <div className="showtime-card__footer">
                        <p className="showtime-card__price">
                            Price: <strong>{formatPrice(showtime.basePrice)}</strong>
                        </p>

                        <Link
                            className="showtime-card__button"
                            to={`/showtimes/${showtime.id}`}
                        >
                            View details
                        </Link>
                    </div>
                </article>
            ))}
        </div>
    );
}