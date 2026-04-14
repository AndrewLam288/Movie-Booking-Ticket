import {
    createContext,
    useContext,
    useEffect,
    useMemo,
    useState,
    type ReactNode,
} from 'react';
import { getCurrentUser } from '../api/authApi';
import type { AuthResponse, CurrentUserResponse } from '../types/auth';
import { authStorage } from '../utils/authStorage';

type AuthContextValue = {
    user: CurrentUserResponse | null;
    isAuthenticated: boolean;
    isInitializing: boolean;
    completeLogin: (response: AuthResponse) => Promise<void>;
    logout: () => void;
    refreshCurrentUser: () => Promise<void>;
};

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<CurrentUserResponse | null>(null);
    const [isInitializing, setIsInitializing] = useState(true);

    async function refreshCurrentUser(): Promise<void> {
        const token = authStorage.getAccessToken();

        if (!token) {
            setUser(null);
            return;
        }

        try {
            const currentUser = await getCurrentUser();
            setUser(currentUser);
        } catch {
            authStorage.clearAccessToken();
            setUser(null);
        }
    }

    async function completeLogin(response: AuthResponse): Promise<void> {
        authStorage.setAccessToken(response.accessToken);
        await refreshCurrentUser();
    }

    function logout(): void {
        authStorage.clearAccessToken();
        setUser(null);
    }

    useEffect(() => {
        let isMounted = true;

        async function initializeAuth() {
            try {
                const token = authStorage.getAccessToken();

                if (!token) {
                    if (isMounted) {
                        setUser(null);
                        setIsInitializing(false);
                    }
                    return;
                }

                const currentUser = await getCurrentUser();

                if (isMounted) {
                    setUser(currentUser);
                }
            } catch {
                authStorage.clearAccessToken();

                if (isMounted) {
                    setUser(null);
                }
            } finally {
                if (isMounted) {
                    setIsInitializing(false);
                }
            }
        }

        void initializeAuth();

        return () => {
            isMounted = false;
        };
    }, []);

    const value = useMemo<AuthContextValue>(
        () => ({
            user,
            isAuthenticated: !!user,
            isInitializing,
            completeLogin,
            logout,
            refreshCurrentUser,
        }),
        [user, isInitializing]
    );

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
    const context = useContext(AuthContext);

    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider.');
    }

    return context;
}