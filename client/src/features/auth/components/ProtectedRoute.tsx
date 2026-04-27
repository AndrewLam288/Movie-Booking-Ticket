import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../context/useAuth";
import "../pages/LoginPage.css";

export default function ProtectedRoute() {
    const { isAuthenticated, isInitializing } = useAuth();
    const location = useLocation();

    if (isInitializing) {
        return (
            <section className="auth-page">
                <div className="auth-card">
                    <div className="auth-card__header">
                        <h1 className="auth-card__title">Loading account...</h1>
                        <p className="auth-card__subtitle">Please wait a moment.</p>
                    </div>
                </div>
            </section>
        );
    }

    if (!isAuthenticated) {
        return <Navigate to="/sign-in" replace state={{ from: location }} />;
    }

    return <Outlet />;
}