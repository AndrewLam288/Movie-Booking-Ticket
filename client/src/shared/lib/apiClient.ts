const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "/api/v1";

const ACCESS_TOKEN_KEY = 'movie_booking_access_token';

function getAccessToken(): string | null {
    return localStorage.getItem(ACCESS_TOKEN_KEY);
}

function buildHeaders(): Record<string, string> {
    const token = getAccessToken();

    return {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
    };
}

async function handleResponse<T>(response: Response): Promise<T> {
    if (!response.ok) {
        let errorMessage = `Request failed with status ${response.status}`;

        try {
            const errorBody = await response.json();

            if (typeof errorBody === 'string' && errorBody.trim()) {
                errorMessage = errorBody;
            } else if (
                errorBody &&
                typeof errorBody === 'object' &&
                'message' in errorBody &&
                typeof errorBody.message === 'string' &&
                errorBody.message.trim()
            ) {
                errorMessage = errorBody.message;
            }
        } catch {
            // ignore parse error
        }

        throw new Error(errorMessage);
    }

    if (response.status === 204) {
        return undefined as T;
    }

    return (await response.json()) as T;
}

export async function apiGet<T>(path: string): Promise<T> {
    const response = await fetch(`${API_BASE_URL}${path}`, {
        method: 'GET',
        headers: buildHeaders(),
    });

    return handleResponse<T>(response);
}

export async function apiPost<TRequest, TResponse>(
    path: string,
    body: TRequest
): Promise<TResponse> {
    const response = await fetch(`${API_BASE_URL}${path}`, {
        method: 'POST',
        headers: buildHeaders(),
        body: JSON.stringify(body),
    });

    return handleResponse<TResponse>(response);
}

export async function apiPut<TRequest, TResponse>(
    path: string,
    body: TRequest
): Promise<TResponse> {
    const response = await fetch(`${API_BASE_URL}${path}`, {
        method: 'PUT',
        headers: buildHeaders(),
        body: JSON.stringify(body),
    });

    return handleResponse<TResponse>(response);
}

export async function apiPatch<TRequest, TResponse>(
    path: string,
    body: TRequest
): Promise<TResponse> {
    const response = await fetch(`${API_BASE_URL}${path}`, {
        method: 'PATCH',
        headers: buildHeaders(),
        body: JSON.stringify(body),
    });

    return handleResponse<TResponse>(response);
}

export async function apiDelete<T>(path: string): Promise<T> {
    const response = await fetch(`${API_BASE_URL}${path}`, {
        method: 'DELETE',
        headers: buildHeaders(),
    });

    return handleResponse<T>(response);
}