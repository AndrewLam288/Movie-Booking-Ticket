import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getCinemaById } from '../api/cinemasApi';
import { getShowtimesByCinemaId } from '../../showtimes/api/showtimesApi';
import type { CinemaDetail } from '../types/cinema';
import type { ShowtimeSummary } from '../../showtimes/types/showtime';
import ShowtimeList from '../../showtimes/components/ShowtimeList';
import './CinemaDetailPage.css';

function buildLocation(cinema: CinemaDetail): string {
    return [cinema.city, cinema.state, cinema.country].filter(Boolean).join(', ');
}

export default function CinemaDetailPage() {
    const { cinemaId } = useParams();
    const parsedCinemaId = Number(cinemaId);

    const [cinema, setCinema] = useState<CinemaDetail | null>(null);
    const [showtimes, setShowtimes] = useState<ShowtimeSummary[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        let isMounted = true;

        async function loadPage() {
            if (!Number.isFinite(parsedCinemaId)) {
                setError('Invalid cinema id.');
                setLoading(false);
                return;
            }

            try {
                setLoading(true);
                setError('');

                const [cinemaData, showtimeData] = await Promise.all([
                    getCinemaById(parsedCinemaId),
                    getShowtimesByCinemaId(parsedCinemaId),
                ]);

                if (isMounted) {
                    setCinema(cinemaData);
                    setShowtimes(showtimeData);
                }
            } catch (err) {
                if (isMounted) {
                    setError(err instanceof Error ? err.message : 'Failed to load cinema page.');
                }
            } finally {
                if (isMounted) {
                    setLoading(false);
                }
            }
        }

        void loadPage();

        return () => {
            isMounted = false;
        };
    }, [parsedCinemaId]);

    if (loading) {
        return <section className="cinema-detail-page">Loading cinema details...</section>;
    }

    if (error) {
        return <section className="cinema-detail-page cinema-detail-page__error">{error}</section>;
    }

    if (!cinema) {
        return <section className="cinema-detail-page">Cinema not found.</section>;
    }

    return (
        <section className="cinema-detail-page">
            <header className="cinema-detail-page__header">
                <h1>{cinema.name}</h1>
                <p>{cinema.addressLine}</p>
                <p>{buildLocation(cinema)}</p>
                <p>Postal code: {cinema.postalCode}</p>
            </header>

            <div className="cinema-detail-page__section">
                <h2>Showtimes</h2>
                <ShowtimeList showtimes={showtimes} />
            </div>
        </section>
    );
}