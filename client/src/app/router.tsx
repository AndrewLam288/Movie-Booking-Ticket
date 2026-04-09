import { createBrowserRouter, Navigate } from "react-router-dom";
import AppLayout from "../components/layout/AppLayout";
import MoviesPage from "../features/movies/pages/MoviesPage";
import MovieDetailPage from "../features/movies/pages/MovieDetailPage";
import PlaceholderPage from "../pages/PlaceholderPage";
import NotFoundPage from "../pages/NotFoundPage";

export const router = createBrowserRouter([
    {
        path: "/",
        element: <AppLayout />,
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
                path: "food-drinks",
                element: <PlaceholderPage title="Food & Drinks" />,
            },
            {
                path: "my-bookings",
                element: <PlaceholderPage title="My Bookings" />,
            },
            {
                path: "cart",
                element: <PlaceholderPage title="Cart" />,
            },
            {
                path: "sign-in",
                element: <PlaceholderPage title="Sign In" />,
            },
            {
                path: "buy-tickets/:movieId",
                element: <PlaceholderPage title="Buy Tickets" />,
            },
            {
                path: "*",
                element: <NotFoundPage />,
            },
        ],
    },
]);