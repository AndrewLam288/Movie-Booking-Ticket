import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { login } from "../api/authApi";
import { useAuth } from "../context/AuthContext";
import { getApiErrorMessage } from "../utils/getApiErrorMessage";
import "./LoginPage.css";

export default function LoginPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const { completeLogin } = useAuth();

    const redirectTo =
        (location.state as { from?: { pathname?: string } } | null)?.from?.pathname ||
        "/account";

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();

        setErrorMessage("");
        setIsSubmitting(true);

        try {
            const response = await login({
                email: email.trim(),
                password,
            });

            await completeLogin(response);
            navigate(redirectTo, { replace: true });
        } catch (error) {
            setErrorMessage(getApiErrorMessage(error, "Login failed. Please try again."));
        } finally {
            setIsSubmitting(false);
        }
    }

    return (
        <section className="auth-page">
            <div className="auth-card">
                <div className="auth-card__header">
                    <h1 className="auth-card__title">Sign In</h1>
                    <p className="auth-card__subtitle">
                        Welcome back. Sign in to continue booking movies.
                    </p>
                </div>

                <form className="auth-form" onSubmit={handleSubmit}>
                    <div className="auth-form__group">
                        <label htmlFor="email" className="auth-form__label">
                            Email
                        </label>
                        <input
                            id="email"
                            type="email"
                            className="auth-form__input"
                            value={email}
                            onChange={(event) => setEmail(event.target.value)}
                            autoComplete="email"
                            required
                        />
                    </div>

                    <div className="auth-form__group">
                        <label htmlFor="password" className="auth-form__label">
                            Password
                        </label>
                        <input
                            id="password"
                            type="password"
                            className="auth-form__input"
                            value={password}
                            onChange={(event) => setPassword(event.target.value)}
                            autoComplete="current-password"
                            required
                        />
                    </div>

                    {errorMessage ? (
                        <p className="auth-form__error">{errorMessage}</p>
                    ) : null}

                    <button
                        type="submit"
                        className="auth-form__button"
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? "Signing in..." : "Sign In"}
                    </button>
                </form>

                <p className="auth-card__footer">
                    Don&apos;t have an account? <Link to="/register">Create one</Link>
                </p>
            </div>
        </section>
    );
}