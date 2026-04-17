import { Client, type IMessage } from "@stomp/stompjs";
import type { SeatAvailabilityEventDto } from "../types/seat";

type SeatEventHandler = (event: SeatAvailabilityEventDto) => void;

export interface SeatWebSocketSubscription {
    disconnect: () => void;
}

function getWebSocketUrl(): string {
    const apiBaseUrl =
        import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api/v1";

    const apiUrl = new URL(apiBaseUrl);
    const protocol = apiUrl.protocol === "https:" ? "wss:" : "ws:";

    return `${protocol}//${apiUrl.host}/ws`;
}

export function subscribeToSeatAvailability(
    showtimeId: number,
    onEvent: SeatEventHandler
): SeatWebSocketSubscription {
    const client = new Client({
        brokerURL: getWebSocketUrl(),
        reconnectDelay: 5000,
        debug: () => {},
    });

    client.onConnect = () => {
        client.subscribe(`/topic/showtimes/${showtimeId}/seats`, (message: IMessage) => {
            const parsed = JSON.parse(message.body) as SeatAvailabilityEventDto;
            onEvent(parsed);
        });
    };

    client.onStompError = (frame) => {
        console.error("STOMP error:", frame.headers["message"], frame.body);
    };

    client.onWebSocketError = (event) => {
        console.error("WebSocket error:", event);
    };

    client.activate();

    return {
        disconnect: () => {
            void client.deactivate();
        },
    };
}