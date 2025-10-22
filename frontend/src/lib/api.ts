import axios from 'axios';

export const api = axios.create({
  baseURL: '/api',
});

export function setAuthToken(token: string | null) {
  if (token) {
    api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    localStorage.setItem('token', token);
  } else {
    delete api.defaults.headers.common['Authorization'];
    localStorage.removeItem('token');
  }
}

export function bootstrapToken() {
  const token = localStorage.getItem('token');
  if (token) setAuthToken(token);
}
