import "./SeatBookingSummary.css";

interface SeatBookingSummaryProps {
    roomName?: string;
    selectedLabels: string[];
    bookingCode?: string;
    bookingLoading: boolean;
    onConfirm: () => void;
    onClear: () => void;
}

export default function SeatBookingSummary({
                                               roomName,
                                               selectedLabels,
                                               bookingCode,
                                               bookingLoading,
                                               onConfirm,
                                               onClear,
                                           }: SeatBookingSummaryProps) {
    const hasSelection = selectedLabels.length > 0;

    return (
        <aside className="seat-booking-summary">
            <h3 className="seat-booking-summary__title">Your Selection</h3>

            {roomName ? (
                <p className="seat-booking-summary__room">Room: {roomName}</p>
            ) : null}

            {bookingCode ? (
                <div className="seat-booking-summary__success">
                    <strong>Booking confirmed</strong>
                    <span>Code: {bookingCode}</span>
                </div>
            ) : null}

            <div className="seat-booking-summary__section">
                <div className="seat-booking-summary__label">Seats</div>

                {hasSelection ? (
                    <div className="seat-booking-summary__chips">
                        {selectedLabels.map((label) => (
                            <span key={label} className="seat-booking-summary__chip">
                {label}
              </span>
                        ))}
                    </div>
                ) : (
                    <p className="seat-booking-summary__empty">
                        Select available seats to continue booking.
                    </p>
                )}
            </div>

            <div className="seat-booking-summary__actions">
                <button
                    type="button"
                    className="seat-booking-summary__button seat-booking-summary__button--primary"
                    disabled={!hasSelection || bookingLoading}
                    onClick={onConfirm}
                >
                    {bookingLoading ? "Confirming..." : "Confirm Booking"}
                </button>

                <button
                    type="button"
                    className="seat-booking-summary__button seat-booking-summary__button--secondary"
                    disabled={!hasSelection || bookingLoading}
                    onClick={onClear}
                >
                    Clear Selection
                </button>
            </div>
        </aside>
    );
}