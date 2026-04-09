import { useEffect, useMemo, useState } from "react";
import type { MovieStatus, MovieSummary } from "../types/movie";
import { getMovies } from "../api/moviesApi";
import MovieCard from "../components/MovieCard";
import MovieFilters from "../components/MovieFilters";
import LoadingState from "../../../shared/ui/LoadingState";
import ErrorState from "../../../shared/ui/ErrorState";
import EmptyState from "../../../shared/ui/EmptyState";
import "./MoviesPage.css";

export default function MoviesPage() {
    const [movies, setMovies] = useState<MovieSummary[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [searchTerm, setSearchTerm] = useState("");
    const [selectedStatus, setSelectedStatus] = useState<MovieStatus | "ALL">("ALL");

    useEffect(() => {
        let mounted = true;

        async function loadMovies() {
            try {
                setLoading(true);
                setError("");
                const data = await getMovies();
                if (mounted) {
                    setMovies(data);
                }
            } catch (err) {
                if (mounted) {
                    setError(err instanceof Error ? err.message : "Failed to load movies.");
                }
            } finally {
                if (mounted) {
                    setLoading(false);
                }
            }
        }

        loadMovies();

        return () => {
            mounted = false;
        };
    }, []);

    const filteredMovies = useMemo(() => {
        return movies.filter((movie) => {
            const matchesSearch = movie.title
                .toLowerCase()
                .includes(searchTerm.trim().toLowerCase());

            const matchesStatus =
                selectedStatus === "ALL" || movie.status === selectedStatus;

            return matchesSearch && matchesStatus;
        });
    }, [movies, searchTerm, selectedStatus]);

    return (
        <section className="movies-page">
            <div className="page-hero">
                <h1>Movies</h1>
                <p>Browse what is showing now, what is coming soon, and past releases.</p>
            </div>

            <div className="movies-content">
                <MovieFilters
                    searchTerm={searchTerm}
                    selectedStatus={selectedStatus}
                    onSearchChange={setSearchTerm}
                    onStatusChange={setSelectedStatus}
                />

                <div className="movies-grid-section">
                    {loading && <LoadingState message="Loading movies..." />}
                    {!loading && error && <ErrorState message={error} />}
                    {!loading && !error && filteredMovies.length === 0 && (
                        <EmptyState message="No movies match your current filters." />
                    )}

                    {!loading && !error && filteredMovies.length > 0 && (
                        <div className="movies-grid">
                            {filteredMovies.map((movie) => (
                                <MovieCard key={movie.id} movie={movie} />
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </section>
    );
}