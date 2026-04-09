const API_BASE_URL =
    import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api/v1";

function buildAuthHeader(): Record<string, string> {
    const username = import.meta.env.VITE_BASIC_AUTH_USERNAME;
    const password = import.meta.env.VITE_BASIC_AUTH_PASSWORD;

    if (!username || !password) {
        return {};
    }

    return {
        Authorization: `Basic ${btoa(`${username}:${password}`)}`,
    };
}

export async function apiGet<T>(path: string): Promise<T> {
    const response = await fetch(`${API_BASE_URL}${path}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            ...buildAuthHeader(),
        },
    });

    if (!response.ok) {
        let errorMessage = `Request failed with status ${response.status}`;

        try {
            const errorBody = await response.json();
            if (errorBody?.message) {
                errorMessage = errorBody.message;
            }
        } catch {
            // ignore parse error
        }

        throw new Error(errorMessage);
    }

    return response.json() as Promise<T>;
}