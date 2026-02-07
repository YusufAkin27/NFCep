const API_BASE = '/api';

async function request(path, options = {}) {
  const res = await fetch(API_BASE + path, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options,
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || `HTTP ${res.status}`);
  }
  if (res.status === 204) return null;
  return res.json();
}

export async function getTable(tableNumber) {
  return request(`/tables/${encodeURIComponent(tableNumber)}`);
}

export async function getProducts(page = 0, size = 100) {
  const res = await request(`/products?page=${page}&size=${size}`);
  return res.content ?? res;
}

export async function createOrder(tableNumber, tableName, items) {
  return request('/orders', {
    method: 'POST',
    body: JSON.stringify({ tableNumber, tableName, items }),
  });
}

export async function callWaiter(tableNumber, tableName, message = '') {
  return request('/calls', {
    method: 'POST',
    body: JSON.stringify({ tableNumber, tableName, message }),
  });
}
