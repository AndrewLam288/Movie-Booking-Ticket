export type ShowtimeSummary = {
    id: number;
    movieId: number;
    movieTitle: string;
    cinemaId: number;
    cinemaName: string;
    roomId: number;
    roomName: string;
    format: 'TWO_D' | 'THREE_D' | 'IMAX' | 'FOUR_DX';
    startTime: string;
    endTime: string;
    basePrice: number;
    status: 'SCHEDULED' | 'CANCELLED' | 'COMPLETED';
};

export type ShowtimeDetail = {
    id: number;
    movieId: number;
    movieTitle: string;
    cinemaId: number;
    cinemaName: string;
    cinemaAddressLine: string;
    cinemaCity: string;
    cinemaState: string | null;
    cinemaPostalCode: string;
    cinemaCountry: string;
    roomId: number;
    roomName: string;
    format: 'TWO_D' | 'THREE_D' | 'IMAX' | 'FOUR_DX';
    startTime: string;
    endTime: string;
    basePrice: number;
    status: 'SCHEDULED' | 'CANCELLED' | 'COMPLETED';
};