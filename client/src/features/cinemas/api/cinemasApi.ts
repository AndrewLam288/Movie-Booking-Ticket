import type { CinemaDetail, CinemaSummary } from '../types/cinema';

const API_BASE_URL =
    import.meta.env.VITE_API_BASE_URL ?? "/api/v1";

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

export async function getCinemas(): Promise<CinemaSummary[]> {
    const response = await fetch(`${API_BASE_URL}/cinemas`, {
        method: 'GET',
        headers: {
            Accept: 'application/json',
        },
    });

    return handleResponse<CinemaSummary[]>(response);
}

export async function getCinemaById(cinemaId: number): Promise<CinemaDetail> {
    const response = await fetch(`${API_BASE_URL}/cinemas/${cinemaId}`, {
        method: 'GET',
        headers: {
            Accept: 'application/json',
        },
    });

    return handleResponse<CinemaDetail>(response);
}