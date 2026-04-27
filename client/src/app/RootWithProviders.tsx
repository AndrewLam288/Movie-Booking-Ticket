import AppLayout from "../../components/layout/AppLayout";
import { AuthProvider } from "../../features/auth/context/AuthProvider";

export default function RootWithProviders() {
    return (
        <AuthProvider>
            <AppLayout />
        </AuthProvider>
    );
}