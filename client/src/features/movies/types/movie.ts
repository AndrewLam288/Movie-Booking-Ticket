export type MovieStatus = "COMING_SOON" | "NOW_SHOWING" | "ENDED";
export type AgeRating = "G" | "PG" | "R";

export interface MovieSummary {
    id: number;
    title: string;
    durationMinutes: number;
    language: string;
    posterUrl: string;
    releaseDate: string;
    ageRating: AgeRating;
    status: MovieStatus;
}

export interface MovieDetail {
    id: number;
    title: string;
    description: string;
    durationMinutes: number;
    language: string;
    director: string;
    castMembers: string;
    posterUrl: string;
    bannerUrl: string;
    trailerUrl: string;
    releaseDate: string;
    ageRating: AgeRating;
    status: MovieStatus;
}