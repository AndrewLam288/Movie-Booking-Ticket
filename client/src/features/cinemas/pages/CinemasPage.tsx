import { useEffect, useState } from 'react';
import { getCinemas } from '../api/cinemasApi';
import type { CinemaSummary } from '../types/cinema';
import CinemaCard from '../components/CinemaCard';
import './CinemasPage.css';

export default function CinemasPage() {
    const [cinemas, setCinemas] = useState<CinemaSummary[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        let isMounted = true;

        async function loadCinemas() {
            try {
                setLoading(true);
                setError('');

                const data = await getCinemas();

                if (isMounted) {
                    setCinemas(data);
                }
            } catch (err) {
                if (isMounted) {
                    setError(err instanceof Error ? err.message : 'Failed to load cinemas.');
                }
            } finally {
                if (isMounted) {
                    setLoading(false);
                }
            }
        }

        void loadCinemas();

        return () => {
            isMounted = false;
        };
    }, []);

    return (
        <section className="cinemas-page">
            <div className="cinemas-page__header">
                <h1>Cinemas</h1>
                <p>Browse available cinema locations and see their showtimes.</p>
            </div>

            {loading && <p className="cinemas-page__state">Loading cinemas...</p>}
            {!loading && error && <p className="cinemas-page__error">{error}</p>}

            {!loading && !error && cinemas.length === 0 && (
                <p className="cinemas-page__state">No cinemas are available right now.</p>
            )}

            {!loading && !error && cinemas.length > 0 && (
                <div className="cinemas-page__grid">
                    {cinemas.map((cinema) => (
                        <CinemaCard key={cinema.id} cinema={cinema} />
                    ))}
                </div>
            )}
        </section>
    );
}