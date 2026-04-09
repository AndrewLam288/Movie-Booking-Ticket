import "./MovieFilters.css";
import type { MovieStatus } from "../types/movie";

interface MovieFiltersProps {
    searchTerm: string;
    selectedStatus: MovieStatus | "ALL";
    onSearchChange: (value: string) => void;
    onStatusChange: (value: MovieStatus | "ALL") => void;
}

const statusOptions: Array<{ label: string; value: MovieStatus | "ALL" }> = [
    { label: "All", value: "ALL" },
    { label: "Now Showing", value: "NOW_SHOWING" },
    { label: "Coming Soon", value: "COMING_SOON" },
    { label: "Ended", value: "ENDED" },
];

export default function MovieFilters({
                                         searchTerm,
                                         selectedStatus,
                                         onSearchChange,
                                         onStatusChange,
                                     }: MovieFiltersProps) {
    return (
        <aside className="movie-filters">
            <div className="movie-filters-card">
                <label className="search-label">
                    <span>Search</span>
                    <input
                        type="text"
                        placeholder="Search movie title..."
                        value={searchTerm}
                        onChange={(event) => onSearchChange(event.target.value)}
                    />
                </label>

                <div className="status-filter-group">
                    <p className="filter-title">Status</p>

                    {statusOptions.map((option) => (
                        <button
                            key={option.value}
                            type="button"
                            className={
                                selectedStatus === option.value
                                    ? "status-filter-btn active"
                                    : "status-filter-btn"
                            }
                            onClick={() => onStatusChange(option.value)}
                        >
                            {option.label}
                        </button>
                    ))}
                </div>
            </div>
        </aside>
    );
}