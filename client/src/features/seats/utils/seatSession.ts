const SEAT_SESSION_STORAGE_KEY = 'movie_booking_seat_session_id';

function generateSeatSessionId(): string {
    if (typeof crypto !== 'undefined' && 'randomUUID' in crypto) {
        return crypto.randomUUID();
    }

    return `seat-session-${Date.now()}-${Math.random().toString(36).slice(2)}`;
}

export function getSeatSessionId(): string {
    const existingValue = window.localStorage.getItem(SEAT_SESSION_STORAGE_KEY);

    if (existingValue) {
        return existingValue;
    }

    const newValue = generateSeatSessionId();
    window.localStorage.setItem(SEAT_SESSION_STORAGE_KEY, newValue);
    return newValue;
}