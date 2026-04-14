import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "./AccountPage.css";

type AccountTab = "profile" | "bookings";

export default function AccountPage() {
    const navigate = useNavigate();
    const { user, logout } = useAuth();
    const [activeTab, setActiveTab] = useState<AccountTab>("profile");

    function handleLogout() {
        logout();
        navigate("/sign-in", { replace: true });
    }

    return (
        <section className="account-page">
            <div className="account-layout">
                <aside className="account-sidebar">
                    <button
                        type="button"
                        className={`account-sidebar__button ${
                            activeTab === "profile" ? "is-active" : ""
                        }`}
                        onClick={() => setActiveTab("profile")}
                    >
                        Profile
                    </button>

                    <button
                        type="button"
                        className={`account-sidebar__button ${
                            activeTab === "bookings" ? "is-active" : ""
                        }`}
                        onClick={() => setActiveTab("bookings")}
                    >
                        My Bookings
                    </button>

                    <button
                        type="button"
                        className="account-sidebar__button account-sidebar__button--logout"
                        onClick={handleLogout}
                    >
                        Sign Out
                    </button>
                </aside>

                <div className="account-panel">
                    {activeTab === "profile" ? (
                        <>
                            <div className="account-panel__header">
                                <h1 className="account-panel__title">My Account</h1>
                                <p className="account-panel__subtitle">
                                    Basic account information from your authenticated profile.
                                </p>
                            </div>

                            <div className="account-info">
                                <div className="account-info__field">
                                    <label className="account-info__label">Full Name</label>
                                    <div className="account-info__value">
                                        {user?.fullName ?? "-"}
                                    </div>
                                </div>

                                <div className="account-info__field">
                                    <label className="account-info__label">Email</label>
                                    <div className="account-info__value">
                                        {user?.email ?? "-"}
                                    </div>
                                </div>

                                <div className="account-info__field">
                                    <label className="account-info__label">Phone Number</label>
                                    <div className="account-info__value">
                                        {user?.phoneNumber ?? "-"}
                                    </div>
                                </div>

                                <div className="account-info__field">
                                    <label className="account-info__label">Role</label>
                                    <div className="account-info__value">
                                        {user?.role ?? "-"}
                                    </div>
                                </div>
                            </div>
                        </>
                    ) : (
                        <>
                            <div className="account-panel__header">
                                <h1 className="account-panel__title">My Bookings</h1>
                                <p className="account-panel__subtitle">
                                    Booking history will appear here once the booking flow is built.
                                </p>
                            </div>

                            <div className="account-empty-state">
                                <p>No booking data is available yet.</p>
                            </div>
                        </>
                    )}
                </div>
            </div>
        </section>
    );
}