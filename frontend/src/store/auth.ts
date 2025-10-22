import { create } from 'zustand';
import { api, setAuthToken } from '../lib/api';

interface AuthState {
  token: string | null;
  email: string | null;
  login: (email: string, password: string) => Promise<void>;
  register: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

export const useAuth = create<AuthState>((set) => ({
  token: localStorage.getItem('token'),
  email: null,
  async login(email, password) {
    const res = await api.post('/auth/login', { email, password });
    const token = res.data.token as string;
    setAuthToken(token);
    set({ token, email });
  },
  async register(email, password) {
    const res = await api.post('/auth/register', { email, password });
    const token = res.data.token as string;
    setAuthToken(token);
    set({ token, email });
  },
  logout() {
    setAuthToken(null);
    set({ token: null, email: null });
  },
}));
