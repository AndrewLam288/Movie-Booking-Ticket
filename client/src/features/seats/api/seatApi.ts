import { apiDelete, apiGet, apiPost } from "../../../shared/lib/apiClient";
import type {
    BookingResponseDto,
    CreateBookingRequestDto,
    HoldSeatRequestDto,
    ReleaseSeatRequestDto,
    SeatMapResponse,
} from "../types/seat";

export async function getSeatMap(showtimeId: number): Promise<SeatMapResponse> {
    return apiGet<SeatMapResponse>(`/showtimes/${showtimeId}/seats`);
}

export async function holdSeat(
    showtimeId: number,
    seatOrPayload: number | HoldSeatRequestDto
): Promise<void> {
    const seatId =
        typeof seatOrPayload === "number" ? seatOrPayload : seatOrPayload.seatId;

    await apiPost<undefined, void>(
        `/showtimes/${showtimeId}/seats/${seatId}/hold`,
        undefined
    );
}

export async function releaseSeat(
    showtimeId: number,
    seatOrPayload: number | ReleaseSeatRequestDto
): Promise<void> {
    const seatId =
        typeof seatOrPayload === "number" ? seatOrPayload : seatOrPayload.seatId;

    await apiDelete<void>(`/showtimes/${showtimeId}/seats/${seatId}/hold`);
}

export async function createBooking(
    payload: CreateBookingRequestDto
): Promise<BookingResponseDto> {
    return apiPost<CreateBookingRequestDto, BookingResponseDto>(
        "/bookings",
        payload
    );
}

export async function getBookingByCode(
    bookingCode: string
): Promise<BookingResponseDto> {
    return apiGet<BookingResponseDto>(`/bookings/${bookingCode}`);
}