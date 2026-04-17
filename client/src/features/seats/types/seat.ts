export type SeatType = 'STANDARD' | 'VIP' | 'COUPLE';

export type SeatAvailabilityStatus =
    | 'AVAILABLE'
    | 'HELD'
    | 'BOOKED'
    | 'UNAVAILABLE';

export interface SeatItem {
    seatId: number;
    seatRow: string;
    seatNumber: number;
    seatType: SeatType;
    isActive: boolean;
    status: SeatAvailabilityStatus;
    heldBySessionId: string | null;
}

export interface SeatMapResponse {
    showtimeId: number;
    roomId: number;
    roomName: string;
    seats: SeatItem[];
}

export interface HoldSeatRequestDto {
    seatId: number;
    clientSessionId: string;
}

export interface ReleaseSeatRequestDto {
    seatId: number;
    clientSessionId: string;
}

export interface SeatAvailabilityEventDto {
    showtimeId: number;
    seatId: number;
    status: SeatAvailabilityStatus;
    clientSessionId: string | null;
}