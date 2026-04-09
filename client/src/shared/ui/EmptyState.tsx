export default function EmptyState({ message }: { message: string }) {
    return <div className="feedback-box empty-box">{message}</div>;
}