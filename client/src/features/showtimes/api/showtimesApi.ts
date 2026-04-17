import type { ShowtimeDetail, ShowtimeSummary } from '../types/showtime';

const API_BASE_URL =
    import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api/v1';

async function handleResponse<T>(response: Response): Promise<T> {
    if (!response.ok) {
        let message = `Request failed with status ${response.status}`;

        try {
            const errorBody = (await response.json()) as { message?: string; error?: string };
            message = errorBody.message ?? errorBody.error ?? message;
        } catch {
            // ignore json parse failure
        }

        throw new Error(message);
    }

    return (await response.json()) as T;
}

export async function getShowtimesByCinemaId(cinemaId: number): Promise<ShowtimeSummary[]> {
    const response = await fetch(`${API_BASE_URL}/cinemas/${cinemaId}/showtimes`, {
        method: 'GET',
        headers: {
            Accept: 'application/json',
        },
    });

    return handleResponse<ShowtimeSummary[]>(response);
}

export async function getShowtimeById(showtimeId: number): Promise<ShowtimeDetail> {
    const response = await fetch(`${API_BASE_URL}/showtimes/${showtimeId}`, {
        method: 'GET',
        headers: {
            Accept: 'application/json',
        },
    });

    return handleResponse<ShowtimeDetail>(response);
}