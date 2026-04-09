export default function ErrorState({ message }: { message: string }) {
    return <div className="feedback-box error-box">{message}</div>;
}