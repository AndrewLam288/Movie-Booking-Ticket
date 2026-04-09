import { Link } from "react-router-dom";
import type { MovieSummary } from "../types/movie";
import "./MovieCard.css";

interface MovieCardProps {
    movie: MovieSummary;
}

function formatDuration(minutes: number) {
    const hours = Math.floor(minutes / 60);
    const remainMinutes = minutes % 60;
    return `${hours}h ${remainMinutes}m`;
}

function formatStatus(status: MovieSummary["status"]) {
    if (status === "NOW_SHOWING") return "Now Showing";
    if (status === "COMING_SOON") return "Coming Soon";
    return "Ended";
}

export default function MovieCard({ movie }: MovieCardProps) {
    return (
        <Link to={`/movies/${movie.id}`} className="movie-card">
            <div className="movie-poster-wrapper">
                <img
                    src={movie.posterUrl}
                    alt={movie.title}
                    className="movie-poster"
                    loading="lazy"
                />
                <span className="age-badge">{movie.ageRating}</span>
            </div>

            <div className="movie-card-body">
                <h3>{movie.title}</h3>
                <p>{formatDuration(movie.durationMinutes)}</p>
                <p>{movie.language}</p>
                <span className="status-badge">{formatStatus(movie.status)}</span>
            </div>
        </Link>
    );
}