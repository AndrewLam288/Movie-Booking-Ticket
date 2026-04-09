import { Link } from "react-router-dom";

export default function NotFoundPage() {
    return (
        <section className="movies-page">
            <div className="feedback-box error-box">
                <h1>404 - Page Not Found</h1>
                <p>The page you are looking for does not exist.</p>
                <Link to="/movies" className="primary-btn" style={{ marginTop: "1rem" }}>
                    Back to Movies
                </Link>
            </div>
        </section>
    );
}