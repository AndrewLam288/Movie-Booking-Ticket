import {
    useCallback,
    useEffect,
    useMemo,
    useState,
    type ReactNode,
} from "react";
import { getCurrentUser } from "../api/authApi";
import type { AuthResponse, CurrentUserResponse } from "../types/auth";
import { authStorage } from "../utils/authStorage";
import { AuthContext, type AuthContextValue } from "./AuthContext";

type AuthProviderProps = {
    children: ReactNode;
};

export function AuthProvider({ children }: AuthProviderProps) {
    const [user, setUser] = useState<CurrentUserResponse | null>(null);
    const [isInitializing, setIsInitializing] = useState(true);

    const refreshCurrentUser = useCallback(async (): Promise<void> => {
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
    }, []);

    const completeLogin = useCallback(
        async (response: AuthResponse): Promise<void> => {
            authStorage.setAccessToken(response.accessToken);
            await refreshCurrentUser();
        },
        [refreshCurrentUser]
    );

    const logout = useCallback((): void => {
        authStorage.clearAccessToken();
        setUser(null);
    }, []);

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
        [user, isInitializing, completeLogin, logout, refreshCurrentUser]
    );

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}