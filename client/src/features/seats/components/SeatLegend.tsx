import "./SeatLegend.css";

export default function SeatLegend() {
    return (
        <div className="seat-legend">
            <div className="seat-legend__item">
                <span className="seat-legend__swatch seat-legend__swatch--available" />
                <span>Available</span>
            </div>

            <div className="seat-legend__item">
                <span className="seat-legend__swatch seat-legend__swatch--held" />
                <span>Held</span>
            </div>

            <div className="seat-legend__item">
                <span className="seat-legend__swatch seat-legend__swatch--booked" />
                <span>Booked</span>
            </div>

            <div className="seat-legend__item">
                <span className="seat-legend__swatch seat-legend__swatch--unavailable" />
                <span>Unavailable</span>
            </div>

            <div className="seat-legend__item">
                <span className="seat-legend__swatch seat-legend__swatch--vip" />
                <span>VIP seat</span>
            </div>

            <div className="seat-legend__item">
                <span className="seat-legend__swatch seat-legend__swatch--couple" />
                <span>Couple seat</span>
            </div>
        </div>
    );
}