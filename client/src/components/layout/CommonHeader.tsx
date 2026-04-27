import { useEffect, useState } from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../features/auth/context/useAuth";
import { getCinemas } from "../../features/cinemas/api/cinemasApi";
import type { CinemaSummary } from "../../features/cinemas/types/cinema";
import "./CommonHeader.css";

export default function CommonHeader() {
    const { isAuthenticated } = useAuth();
    const navigate = useNavigate();

    const [cinemas, setCinemas] = useState<CinemaSummary[]>([]);
    const [isLoadingCinemas, setIsLoadingCinemas] = useState(true);

    useEffect(() => {
        let isMounted = true;

        async function loadCinemas() {
            try {
                setIsLoadingCinemas(true);
                const data = await getCinemas();

                if (isMounted) {
                    setCinemas(data);
                }
            } catch (error) {
                console.error("Failed to load cinemas for header picker:", error);

                if (isMounted) {
                    setCinemas([]);
                }
            } finally {
                if (isMounted) {
                    setIsLoadingCinemas(false);
                }
            }
        }

        void loadCinemas();

        return () => {
            isMounted = false;
        };
    }, []);

    const navItems = [
        { label: "Movies", to: "/movies" },
        { label: "Cinemas", to: "/cinemas" },
        ...(isAuthenticated
            ? [{ label: "My Account", to: "/account" }]
            : [{ label: "Sign In", to: "/sign-in" }]),
    ];

    function handleCinemaChange(event: React.ChangeEvent<HTMLSelectElement>) {
        const selectedCinemaId = event.target.value;

        if (!selectedCinemaId) {
            return;
        }

        navigate(`/cinemas/${selectedCinemaId}`);
    }

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
                        <select
                            defaultValue=""
                            onChange={handleCinemaChange}
                            disabled={isLoadingCinemas}
                        >
                            <option value="" disabled>
                                {isLoadingCinemas ? "Loading theaters..." : "Find your theater"}
                            </option>

                            {cinemas.map((cinema) => (
                                <option key={cinema.id} value={cinema.id}>
                                    {cinema.name}
                                </option>
                            ))}
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