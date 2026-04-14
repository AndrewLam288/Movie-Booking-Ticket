import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function ProtectedRoute() {
    const { isAuthenticated, isInitializing } = useAuth();
    const location = useLocation();

    if (isInitializing) {
        return (
            <section className="auth-page-section">
                <div className="auth-page-card">
                    <div className="auth-page-header">
                        <h1 className="auth-page-title">Loading account...</h1>
                        <p className="auth-page-subtitle">Please wait a moment.</p>
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