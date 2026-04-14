import { apiGet, apiPost } from '../../../shared/lib/apiClient';
import type {
    AuthResponse,
    CurrentUserResponse,
    LoginRequest,
    RegisterRequest,
} from '../types/auth';

export async function login(request: LoginRequest): Promise<AuthResponse> {
    return apiPost<LoginRequest, AuthResponse>('/auth/login', request);
}

export async function register(
    request: RegisterRequest
): Promise<AuthResponse> {
    return apiPost<RegisterRequest, AuthResponse>('/auth/register', request);
}

export async function getCurrentUser(): Promise<CurrentUserResponse> {
    return apiGet<CurrentUserResponse>('/auth/me');
}