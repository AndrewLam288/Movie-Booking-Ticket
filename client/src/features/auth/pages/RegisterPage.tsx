import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { register } from "../api/authApi";
import { useAuth } from "../context/useAuth";
import { getApiErrorMessage } from "../utils/getApiErrorMessage";
import "./RegisterPage.css";

export default function RegisterPage() {
    const navigate = useNavigate();
    const { completeLogin } = useAuth();

    const [fullName, setFullName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();

        setErrorMessage("");
        setIsSubmitting(true);

        try {
            const response = await register({
                email: email.trim(),
                password,
                fullName: fullName.trim(),
                phoneNumber: phoneNumber.trim() || undefined,
            });

            await completeLogin(response);
            navigate("/account", { replace: true });
        } catch (error) {
            setErrorMessage(
                getApiErrorMessage(error, "Registration failed. Please try again.")
            );
        } finally {
            setIsSubmitting(false);
        }
    }

    return (
        <section className="register-page">
            <div className="register-card">
                <div className="register-card__header">
                    <h1 className="register-card__title">Create Account</h1>
                    <p className="register-card__subtitle">
                        Register to browse movies and manage your bookings.
                    </p>
                </div>

                <form className="register-form" onSubmit={handleSubmit}>
                    <div className="register-form__group">
                        <label htmlFor="fullName" className="register-form__label">
                            Full Name
                        </label>
                        <input
                            id="fullName"
                            type="text"
                            className="register-form__input"
                            value={fullName}
                            onChange={(event) => setFullName(event.target.value)}
                            autoComplete="name"
                            required
                        />
                    </div>

                    <div className="register-form__group">
                        <label htmlFor="email" className="register-form__label">
                            Email
                        </label>
                        <input
                            id="email"
                            type="email"
                            className="register-form__input"
                            value={email}
                            onChange={(event) => setEmail(event.target.value)}
                            autoComplete="email"
                            required
                        />
                    </div>

                    <div className="register-form__group">
                        <label htmlFor="password" className="register-form__label">
                            Password
                        </label>
                        <input
                            id="password"
                            type="password"
                            className="register-form__input"
                            value={password}
                            onChange={(event) => setPassword(event.target.value)}
                            autoComplete="new-password"
                            required
                        />
                    </div>

                    <div className="register-form__group">
                        <label htmlFor="phoneNumber" className="register-form__label">
                            Phone Number
                        </label>
                        <input
                            id="phoneNumber"
                            type="text"
                            className="register-form__input"
                            value={phoneNumber}
                            onChange={(event) => setPhoneNumber(event.target.value)}
                            autoComplete="tel"
                        />
                    </div>

                    {errorMessage ? (
                        <p className="register-form__error">{errorMessage}</p>
                    ) : null}

                    <button
                        type="submit"
                        className="register-form__button"
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? "Creating account..." : "Create Account"}
                    </button>
                </form>

                <p className="register-card__footer">
                    Already have an account? <Link to="/sign-in">Sign In</Link>
                </p>
            </div>
        </section>
    );
}