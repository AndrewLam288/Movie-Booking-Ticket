import { createContext } from "react";
import type { AuthResponse, CurrentUserResponse } from "../types/auth";

export type AuthContextValue = {
    user: CurrentUserResponse | null;
    isAuthenticated: boolean;
    isInitializing: boolean;
    completeLogin: (response: AuthResponse) => Promise<void>;
    logout: () => void;
    refreshCurrentUser: () => Promise<void>;
};

export const AuthContext = createContext<AuthContextValue | undefined>(undefined);