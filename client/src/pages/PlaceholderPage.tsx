interface PlaceholderPageProps {
    title: string;
}

export default function PlaceholderPage({ title }: PlaceholderPageProps) {
    return (
        <section className="movies-page">
            <div className="feedback-box">
                <h1>{title}</h1>
                <p>This page is not built yet. It is only a placeholder for the next phase.</p>
            </div>
        </section>
    );
}