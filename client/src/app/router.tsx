import { createBrowserRouter, Navigate } from "react-router-dom";
import AppLayout from "../components/layout/AppLayout";
import MoviesPage from "../features/movies/pages/MoviesPage";
import MovieDetailPage from "../features/movies/pages/MovieDetailPage";
import LoginPage from "../features/auth/pages/LoginPage";
import RegisterPage from "../features/auth/pages/RegisterPage";
import AccountPage from "../features/auth/pages/AccountPage";
import ProtectedRoute from "../features/auth/components/ProtectedRoute";
import { AuthProvider } from "../features/auth/context/AuthContext";
import CinemasPage from "../features/cinemas/pages/CinemasPage";
import CinemaDetailPage from "../features/cinemas/pages/CinemaDetailPage";
import ShowtimeDetailPage from "../features/showtimes/pages/ShowtimeDetailPage";
import PlaceholderPage from "../pages/PlaceholderPage";
import NotFoundPage from "../pages/NotFoundPage";
import SeatBookingPage from "../features/seats/pages/SeatBookingPage";

function RootWithProviders() {
    return (
        <AuthProvider>
            <AppLayout />
        </AuthProvider>
    );
}

export const router = createBrowserRouter([
    {
        path: "/",
        element: <RootWithProviders />,
        errorElement: <NotFoundPage />,
        children: [
            {
                index: true,
                element: <Navigate to="/movies" replace />,
            },
            {
                path: "movies",
                element: <MoviesPage />,
            },
            {
                path: "movies/:movieId",
                element: <MovieDetailPage />,
            },
            {
                path: "cinemas",
                element: <CinemasPage />,
            },
            {
                path: "cinemas/:cinemaId",
                element: <CinemaDetailPage />,
            },
            {
                path: "showtimes/:showtimeId",
                element: <ShowtimeDetailPage />,
            },
            {
                path: "food-drinks",
                element: <PlaceholderPage title="Food & Drinks" />,
            },
            {
                path: "cart",
                element: <PlaceholderPage title="Cart" />,
            },
            {
                path: "sign-in",
                element: <LoginPage />,
            },
            {
                path: "register",
                element: <RegisterPage />,
            },
            {
                element: <ProtectedRoute />,
                children: [
                    {
                        path: "account",
                        element: <AccountPage />,
                    },
                    {
                        path: "buy-tickets/:movieId",
                        element: <SeatBookingPage />,
                    },
                ],
            },
            {
                path: "*",
                element: <NotFoundPage />,
            },
        ],
    },
]);