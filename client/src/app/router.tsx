import { createBrowserRouter, Navigate } from "react-router-dom";
import RootWithProviders from "./providers/RootWithProviders";
import MoviesPage from "../features/movies/pages/MoviesPage";
import MovieDetailPage from "../features/movies/pages/MovieDetailPage";
import LoginPage from "../features/auth/pages/LoginPage";
import RegisterPage from "../features/auth/pages/RegisterPage";
import AccountPage from "../features/auth/pages/AccountPage";
import ProtectedRoute from "../features/auth/components/ProtectedRoute";
import CinemasPage from "../features/cinemas/pages/CinemasPage";
import CinemaDetailPage from "../features/cinemas/pages/CinemaDetailPage";
import ShowtimeDetailPage from "../features/showtimes/pages/ShowtimeDetailPage";
import NotFoundPage from "../pages/NotFoundPage";
import SeatBookingPage from "../features/seats/pages/SeatBookingPage";
import CheckoutPage from "../features/seats/pages/CheckoutPage";
import BookingConfirmedPage from "../features/seats/pages/BookingConfirmedPage";

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
                    {
                        path: "checkout/showtimes/:showtimeId",
                        element: <CheckoutPage />,
                    },
                    {
                        path: "booking-confirmed/:bookingCode",
                        element: <BookingConfirmedPage />,
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