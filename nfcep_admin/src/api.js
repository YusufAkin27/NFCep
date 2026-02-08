const API_BASE = import.meta.env.VITE_API_URL || '';

export const getToken = () => localStorage.getItem('admin_token');
export const setToken = (token) => {
  if (token) localStorage.setItem('admin_token', token);
  else localStorage.removeItem('admin_token');
};

export async function apiFetch(path, options = {}) {
  const token = getToken();
  const headers = { ...(options.headers || {}) };
  if (!(options.body instanceof FormData)) {
    headers['Content-Type'] = 'application/json';
  }
  if (token) headers['Authorization'] = `Bearer ${token}`;
  const fetchOptions = { ...options, headers };
  if (options.body && !(options.body instanceof FormData) && typeof options.body === 'object' && !(options.body instanceof ArrayBuffer)) {
    fetchOptions.body = JSON.stringify(options.body);
  } else if (options.body) {
    fetchOptions.body = options.body;
  }
  const res = await fetch(`${API_BASE}${path}`, fetchOptions);
  if (res.status === 401) {
    setToken(null);
    throw new Error('Oturum süresi doldu.');
  }
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message || err.error || `Hata: ${res.status}`);
  }
  if (res.status === 204) return null;
  return res.json();
}

export async function loginAdmin(username, password) {
  const res = await fetch(`${API_BASE}/api/auth/admin/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password }),
  });
  if (!res.ok) {
    const err = await res.json().catch(() => ({}));
    throw new Error(err.message || err.error || `Hata: ${res.status}`);
  }
  return res.json();
}

// Statistics
export async function getStatistics() {
  return apiFetch('/api/admin/statistics');
}

// Products
export async function getProducts(page = 0, size = 20) {
  return apiFetch(`/api/admin/products?page=${page}&size=${size}`);
}

export async function createProduct(product, photo) {
  const form = new FormData();
  form.append('product', new Blob([JSON.stringify(product)], { type: 'application/json' }));
  if (photo) form.append('photo', photo);
  return apiFetch('/api/admin/products', { method: 'POST', body: form });
}

export async function updateProduct(id, product, photo) {
  const form = new FormData();
  form.append('product', new Blob([JSON.stringify(product)], { type: 'application/json' }));
  if (photo) form.append('photo', photo);
  return apiFetch(`/api/admin/products/${id}`, { method: 'PUT', body: form });
}

export async function deleteProduct(id) {
  return apiFetch(`/api/admin/products/${id}`, { method: 'DELETE' });
}

// Tables
export async function getTables() {
  return apiFetch('/api/admin/tables');
}

export async function createTable(tableNumber, tableName) {
  return apiFetch('/api/admin/tables', { method: 'POST', body: { tableNumber, tableName } });
}

export async function updateTable(id, data) {
  return apiFetch(`/api/admin/tables/${id}`, { method: 'PUT', body: data });
}

export async function deleteTable(id) {
  return apiFetch(`/api/admin/tables/${id}`, { method: 'DELETE' });
}

// Garsons
export async function getGarsons() {
  return apiFetch('/api/admin/garsons');
}

export async function createGarson(data) {
  return apiFetch('/api/admin/garsons', { method: 'POST', body: data });
}

export async function changeGarsonPassword(id, newPassword) {
  return apiFetch(`/api/admin/garsons/${id}/password`, { method: 'PUT', body: { newPassword } });
}

export async function deleteGarson(id) {
  return apiFetch(`/api/admin/garsons/${id}`, { method: 'DELETE' });
}

export async function setGarsonWorkingToday(id, workingToday) {
  return apiFetch(`/api/admin/garsons/${id}/working-today`, { method: 'PATCH', body: { workingToday } });
}

// Mutfak
export async function getMutfak() {
  return apiFetch('/api/admin/mutfak');
}

export async function createMutfak(data) {
  return apiFetch('/api/admin/mutfak', { method: 'POST', body: data });
}

export async function changeMutfakPassword(id, newPassword) {
  return apiFetch(`/api/admin/mutfak/${id}/password`, { method: 'PUT', body: { newPassword } });
}

export async function deleteMutfak(id) {
  return apiFetch(`/api/admin/mutfak/${id}`, { method: 'DELETE' });
}

// Orders
export async function createOrder(tableNumber, tableName, items) {
  return apiFetch('/api/admin/orders', { method: 'POST', body: { tableNumber, tableName, items } });
}
