import { apiGet } from "../../../shared/lib/apiClient";
import type { MovieDetail, MovieSummary } from "../types/movie";

export async function getMovies(): Promise<MovieSummary[]> {
    return apiGet<MovieSummary[]>("/movies");
}

export async function getMovieDetail(movieId: string): Promise<MovieDetail> {
    return apiGet<MovieDetail>(`/movies/${movieId}`);
}