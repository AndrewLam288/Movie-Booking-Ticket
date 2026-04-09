import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getMovieDetail } from "../api/moviesApi";
import type { MovieDetail } from "../types/movie";
import LoadingState from "../../../shared/ui/LoadingState";
import ErrorState from "../../../shared/ui/ErrorState";

function formatDuration(minutes: number) {
    const hours = Math.floor(minutes / 60);
    const remainMinutes = minutes % 60;
    return `${hours}h ${remainMinutes}m`;
}

export default function MovieDetailPage() {
    const { movieId } = useParams<"movieId">();
    const [movie, setMovie] = useState<MovieDetail | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        if (!movieId) {
            setMovie(null);
            setError("Movie not found.");
            setLoading(false);
            return;
        }

        const currentMovieId = movieId;
        let mounted = true;

        async function loadMovie() {
            try {
                setLoading(true);
                setError("");
                const data = await getMovieDetail(currentMovieId);
                if (mounted) {
                    setMovie(data);
                }
            } catch (err) {
                if (mounted) {
                    setError(err instanceof Error ? err.message : "Failed to load movie.");
                }
            } finally {
                if (mounted) {
                    setLoading(false);
                }
            }
        }

        loadMovie();

        return () => {
            mounted = false;
        };
    }, [movieId]);

    if (loading) {
        return (
            <section className="movie-detail-page">
                <LoadingState message="Loading movie details..." />
            </section>
        );
    }

    if (error || !movie) {
        return (
            <section className="movie-detail-page">
                <ErrorState message={error || "Movie not found."} />
            </section>
        );
    }

    return (
        <section className="movie-detail-page">
            <div
                className="detail-banner"
                style={{
                    backgroundImage: `linear-gradient(rgba(15,23,42,0.7), rgba(15,23,42,0.78)), url(${movie.bannerUrl || movie.posterUrl})`,
                }}
            >
                <div className="detail-banner-content">
                    <span className="detail-language">{movie.language}</span>
                    <h1>{movie.title}</h1>
                </div>
            </div>

            <div className="detail-content-card">
                <div className="detail-main-grid">
                    <div className="detail-poster-column">
                        <img src={movie.posterUrl} alt={movie.title} className="detail-poster" />
                    </div>

                    <div className="detail-info-column">
                        <div className="detail-summary">
                            <p className="detail-meta-line">
                                <strong>Duration:</strong> {formatDuration(movie.durationMinutes)}
                            </p>
                            <p className="detail-meta-line">
                                <strong>Release Date:</strong> {movie.releaseDate}
                            </p>
                            <p className="detail-meta-line">
                                <strong>Age Rating:</strong> {movie.ageRating}
                            </p>
                            <p className="detail-meta-line">
                                <strong>Director:</strong> {movie.director}
                            </p>
                        </div>

                        <div className="detail-description-box">
                            <h2>Description</h2>
                            <p>{movie.description}</p>
                        </div>

                        <div className="detail-actions">
                            <a
                                href={movie.trailerUrl}
                                target="_blank"
                                rel="noreferrer"
                                className="secondary-btn"
                            >
                                Watch Trailer
                            </a>

                            <Link to={`/buy-tickets/${movie.id}`} className="primary-btn">
                                Buy Tickets
                            </Link>
                        </div>
                    </div>
                </div>

                <div className="cast-section">
                    <h2>Cast</h2>
                    <div className="cast-box">{movie.castMembers}</div>
                </div>
            </div>
        </section>
    );
}
