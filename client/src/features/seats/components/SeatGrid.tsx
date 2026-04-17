import type { SeatItem } from "../types/seat";
import "./SeatGrid.css";

interface SeatGridProps {
    seats: SeatItem[];
}

function groupSeatsByRow(seats: SeatItem[]): Map<string, SeatItem[]> {
    const grouped = new Map<string, SeatItem[]>();

    seats.forEach((seat) => {
        const currentRow = grouped.get(seat.seatRow) ?? [];
        currentRow.push(seat);
        grouped.set(seat.seatRow, currentRow);
    });

    grouped.forEach((rowSeats) => {
        rowSeats.sort((a, b) => a.seatNumber - b.seatNumber);
    });

    return new Map([...grouped.entries()].sort(([a], [b]) => a.localeCompare(b)));
}

function buildSeatClassName(seat: SeatItem): string {
    const classNames = ["seat-grid__seat"];

    if (seat.status === "HELD") {
        classNames.push("seat-grid__seat--held");
    } else if (seat.status === "BOOKED") {
        classNames.push("seat-grid__seat--booked");
    } else if (seat.status === "UNAVAILABLE") {
        classNames.push("seat-grid__seat--unavailable");
    } else {
        classNames.push("seat-grid__seat--available");
    }

    if (seat.seatType === "VIP") {
        classNames.push("seat-grid__seat--vip");
    }

    if (seat.seatType === "COUPLE") {
        classNames.push("seat-grid__seat--couple");
    }

    return classNames.join(" ");
}

export default function SeatGrid({ seats }: SeatGridProps) {
    const groupedSeats = groupSeatsByRow(seats);
    const rows = [...groupedSeats.entries()];

    return (
        <div className="seat-grid">
            <div className="seat-grid__screen">Screen</div>

            <div className="seat-grid__layout">
                <div className="seat-grid__row-labels">
                    {rows.map(([rowLabel]) => (
                        <div key={rowLabel} className="seat-grid__row-label">
                            {rowLabel}
                        </div>
                    ))}
                </div>

                <div className="seat-grid__stairs">
                    <span>Stairs</span>
                </div>

                <div className="seat-grid__center">
                    <div className="seat-grid__rows">
                        {rows.map(([rowLabel, rowSeats]) => (
                            <div key={rowLabel} className="seat-grid__row">
                                <div className="seat-grid__row-seats">
                                    {rowSeats.map((seat) => (
                                        <button
                                            key={seat.seatId}
                                            type="button"
                                            className={buildSeatClassName(seat)}
                                            disabled
                                            title={`${seat.seatRow}${seat.seatNumber} - ${seat.seatType}`}
                                        >
                                            {seat.seatRow}
                                            {seat.seatNumber}
                                        </button>
                                    ))}
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                <div className="seat-grid__stairs">
                    <span>Stairs</span>
                </div>
            </div>
        </div>
    );
}