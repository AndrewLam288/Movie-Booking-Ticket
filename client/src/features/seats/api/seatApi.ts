import { apiGet, apiPost } from '../../../shared/lib/apiClient';
import type {
    HoldSeatRequestDto,
    ReleaseSeatRequestDto,
    SeatMapResponse,
} from '../types/seat';

export async function getSeatMap(showtimeId: number): Promise<SeatMapResponse> {
    return apiGet<SeatMapResponse>(`/showtimes/${showtimeId}/seats`);
}

export async function holdSeat(
    showtimeId: number,
    payload: HoldSeatRequestDto,
): Promise<void> {
    await apiPost<HoldSeatRequestDto, void>(
        `/showtimes/${showtimeId}/seats/hold`,
        payload,
    );
}

export async function releaseSeat(
    showtimeId: number,
    payload: ReleaseSeatRequestDto,
): Promise<void> {
    await apiPost<ReleaseSeatRequestDto, void>(
        `/showtimes/${showtimeId}/seats/release`,
        payload,
    );
}