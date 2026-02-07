const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export const getToken = () => localStorage.getItem('mutfak_token');
export const setToken = (token) => {
  if (token) localStorage.setItem('mutfak_token', token);
  else localStorage.removeItem('mutfak_token');
};

export async function apiFetch(path, options = {}) {
  const token = getToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {}),
  };
  if (token) headers['Authorization'] = `Bearer ${token}`;
  const res = await fetch(`${API_BASE}${path}`, { ...options, headers });
  if (res.status === 401) {
    setToken(null);
    throw new Error('Oturum süresi doldu.');
  }
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.error || `Hata: ${res.status}`);
  }
  if (res.status === 204) return null;
  return res.json();
}

export async function loginMutfak(username, password) {
  const res = await fetch(`${API_BASE}/api/auth/mutfak/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
  });
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.error || `Hata: ${res.status}`);
  }
  return res.json();
}

export async function getOrders(page = 0, size = 20) {
  return apiFetch(`/api/mutfak/orders?page=${page}&size=${size}`);
}

export async function updateOrderStatus(orderId, status) {
  return apiFetch(`/api/mutfak/orders/${orderId}/status`, {
    method: 'PATCH',
    body: JSON.stringify({ status }),
  });
}

export async function createCall(tableNumber, tableName, message) {
  return apiFetch('/api/mutfak/calls', {
    method: 'POST',
    body: JSON.stringify({ tableNumber, tableName, message }),
  });
}
