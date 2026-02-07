import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { getOrders, updateOrderStatus, createCall } from '../api';
import './OrdersPage.css';

const STATUS_LABELS = {
  ALINDI: 'Alındı',
  HAZIRLANIYOR: 'Hazırlanıyor',
  TAMAMLANDI: 'Tamamlandı',
};

const STATUS_FLOW = {
  ALINDI: ['HAZIRLANIYOR', 'TAMAMLANDI'],
  HAZIRLANIYOR: ['TAMAMLANDI'],
  TAMAMLANDI: [],
};

export default function OrdersPage() {
  const { token, logout } = useAuth();
  const [page, setPage] = useState({ content: [], totalPages: 0, number: 0, totalElements: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [updatingId, setUpdatingId] = useState(null);

  const loadOrders = async (pageNum = 0) => {
    setLoading(true);
    setError('');
    try {
      const data = await getOrders(pageNum, 20);
      setPage(data);
    } catch (err) {
      setError(err.message || 'Siparişler yüklenemedi.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadOrders(0);
  }, [token]);

  useEffect(() => {
    if (!token) return;
    const interval = setInterval(() => {
      getOrders(page.number, 20)
        .then(setPage)
        .catch(() => {});
    }, 15000);
    return () => clearInterval(interval);
  }, [token, page.number]);

  const handleStatusChange = async (order, newStatus) => {
    setUpdatingId(order.id);
    setError('');
    try {
      await updateOrderStatus(order.id, newStatus);
      if (newStatus === 'TAMAMLANDI') {
        await createCall(
          order.tableNumber,
          order.tableName,
          'Yemek hazır, lütfen masaya götürün.'
        );
      }
      await loadOrders(page.number);
    } catch (err) {
      setError(err.message || 'Durum güncellenemedi.');
    } finally {
      setUpdatingId(null);
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '—';
    const d = new Date(dateStr);
    return d.toLocaleTimeString('tr-TR', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
  };

  const formatMoney = (n) => {
    if (n == null) return '—';
    return new Intl.NumberFormat('tr-TR', { style: 'currency', currency: 'TRY' }).format(n);
  };

  return (
    <div className="orders-page">
      <header className="orders-header">
        <h1>Mutfak — Gelen Siparişler</h1>
        <button type="button" className="logout-btn" onClick={logout}>
          Çıkış
        </button>
      </header>

      {error && <div className="orders-error">{error}</div>}

      {loading && page.content?.length === 0 ? (
        <div className="orders-loading">Siparişler yükleniyor...</div>
      ) : (
        <div className="orders-grid">
          {page.content?.map((order) => (
            <article key={order.id} className="order-card">
              <div className="order-card-header">
                <span className="order-table">
                  {order.tableName} ({order.tableNumber})
                </span>
                <span className={`order-status order-status-${order.status?.toLowerCase()}`}>
                  {STATUS_LABELS[order.status] || order.status}
                </span>
              </div>
              <div className="order-time">{formatDate(order.createdAt)}</div>
              <ul className="order-items">
                {order.items?.map((item, i) => (
                  <li key={i}>
                    <span className="item-name">{item.productName}</span>
                    <span className="item-qty">× {item.quantity}</span>
                    <span className="item-total">{formatMoney(item.lineTotal)}</span>
                  </li>
                ))}
              </ul>
              <div className="order-total">
                Toplam: <strong>{formatMoney(order.totalAmount)}</strong>
              </div>
              <div className="order-actions">
                {STATUS_FLOW[order.status]?.map((nextStatus) => (
                  <button
                    key={nextStatus}
                    type="button"
                    className="order-action-btn"
                    disabled={updatingId === order.id}
                    onClick={() => handleStatusChange(order, nextStatus)}
                  >
                    {updatingId === order.id ? '...' : STATUS_LABELS[nextStatus] + ' yap'}
                  </button>
                ))}
                {(!STATUS_FLOW[order.status] || STATUS_FLOW[order.status].length === 0) && (
                  <span className="order-done">Tamamlandı — Garson bilgilendirildi</span>
                )}
              </div>
            </article>
          ))}
        </div>
      )}

      {page.totalPages > 1 && (
        <div className="orders-pagination">
          <button
            type="button"
            disabled={page.number <= 0}
            onClick={() => loadOrders(page.number - 1)}
          >
            Önceki
          </button>
          <span>
            Sayfa {page.number + 1} / {page.totalPages}
          </span>
          <button
            type="button"
            disabled={page.number >= page.totalPages - 1}
            onClick={() => loadOrders(page.number + 1)}
          >
            Sonraki
          </button>
        </div>
      )}

      {!loading && page.content?.length === 0 && !error && (
        <div className="orders-empty">Henüz aktif sipariş yok.</div>
      )}
    </div>
  );
}
