export type UserRole = 'CUSTOMER' | 'ADMIN';

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    email: string;
    password: string;
    fullName: string;
    phoneNumber?: string;
}

export interface AuthResponse {
    accessToken: string;
    tokenType: string;
    userId: number;
    email: string;
    fullName: string;
    role: UserRole;
}

export interface CurrentUserResponse {
    userId: number;
    email: string;
    fullName: string;
    phoneNumber?: string | null;
    role: UserRole;
}