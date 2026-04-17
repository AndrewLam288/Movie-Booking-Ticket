import { Link } from "react-router-dom";
import type { CinemaSummary } from "../types/cinema";
import "./CinemaCard.css";

type CinemaCardProps = {
    cinema: CinemaSummary;
};

function buildLocation(cinema: CinemaSummary): string {
    return [cinema.city, cinema.state, cinema.country].filter(Boolean).join(", ");
}

export default function CinemaCard({ cinema }: CinemaCardProps) {
    return (
        <article className="cinema-card">
            <div className="cinema-card__body">
                <h2 className="cinema-card__title">{cinema.name}</h2>

                <div className="cinema-card__info-box">
                    <p className="cinema-card__info-line">{cinema.addressLine}</p>
                    <p className="cinema-card__info-line">{buildLocation(cinema)}</p>
                    <p className="cinema-card__info-line">Postal code: {cinema.postalCode}</p>
                </div>
            </div>

            <div className="cinema-card__footer">
                <Link className="cinema-card__button" to={`/cinemas/${cinema.id}`}>
                    View showtimes
                </Link>
            </div>
        </article>
    );
}