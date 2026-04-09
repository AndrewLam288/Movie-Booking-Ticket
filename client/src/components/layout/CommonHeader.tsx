import { Link, NavLink } from "react-router-dom";
import "./CommonHeader.css";

const navItems = [
    { label: "Movies", to: "/movies" },
    { label: "Food & Drinks", to: "/food-drinks" },
    { label: "My Bookings", to: "/my-bookings" },
    { label: "Cart", to: "/cart" },
    { label: "Sign In", to: "/sign-in" },
];

export default function CommonHeader() {
    return (
        <header className="common-header">
            <div className="top-banner">
                <div className="brand-left">
                    <Link to="/movies" className="logo-circle">
                        MB
                    </Link>
                    <div className="brand-text">
                        <p className="brand-title">Movie Booking Project</p>
                        <p className="brand-subtitle">Find your theater and book faster</p>
                    </div>
                </div>

                <div className="header-actions">
                    <label className="theater-picker">
                        <span>Theater</span>
                        <select defaultValue="default">
                            <option value="default" disabled>
                                Find your theater
                            </option>
                            <option>Morris Cinema</option>
                            <option>Downtown Cinema</option>
                            <option>North Plaza Theater</option>
                        </select>
                    </label>
                </div>
            </div>

            <nav className="main-nav">
                {navItems.map((item) => (
                    <NavLink
                        key={item.to}
                        to={item.to}
                        className={({ isActive }) =>
                            isActive ? "nav-link active" : "nav-link"
                        }
                    >
                        {item.label}
                    </NavLink>
                ))}
            </nav>
        </header>
    );
}