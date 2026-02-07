import { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { getTable, getProducts, createOrder, callWaiter } from '../api';

export default function TableMenu() {
  const { tableNumber } = useParams();
  const [table, setTable] = useState(null);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [cart, setCart] = useState([]);
  const [orderSent, setOrderSent] = useState(false);
  const [callSent, setCallSent] = useState(false);

  const loadTable = useCallback(async () => {
    try {
      const t = await getTable(tableNumber);
      setTable(t);
    } catch (e) {
      setError('Masa bulunamadı.');
    }
  }, [tableNumber]);

  const loadProducts = useCallback(async () => {
    try {
      const page = await getProducts(0, 200);
      const list = Array.isArray(page) ? page : (page.content ?? []);
      setProducts(list.filter((p) => p.inStock !== false));
    } catch (e) {
      setError('Ürünler yüklenemedi.');
    }
  }, []);

  useEffect(() => {
    setError('');
    setLoading(true);
    Promise.all([loadTable(), loadProducts()]).finally(() => setLoading(false));
  }, [loadTable, loadProducts]);

  const addToCart = (product, qty = 1) => {
    setCart((prev) => {
      const existing = prev.find((c) => c.productId === product.id);
      if (existing) {
        return prev.map((c) =>
          c.productId === product.id ? { ...c, quantity: c.quantity + qty } : c
        );
      }
      return [...prev, { productId: product.id, productName: product.name, price: product.price, quantity: qty }];
    });
  };

  const updateCartQty = (productId, delta) => {
    setCart((prev) => {
      const item = prev.find((c) => c.productId === productId);
      if (!item) return prev;
      const next = item.quantity + delta;
      if (next <= 0) return prev.filter((c) => c.productId !== productId);
      return prev.map((c) =>
        c.productId === productId ? { ...c, quantity: next } : c
      );
    });
  };

  const cartItems = cart.map((c) => ({
    productId: c.productId,
    quantity: c.quantity,
  }));
  const totalAmount = cart.reduce((sum, c) => sum + Number(c.price) * c.quantity, 0);

  const handlePlaceOrder = async () => {
    if (!table || cart.length === 0) return;
    setError('');
    try {
      await createOrder(table.tableNumber, table.tableName, cartItems);
      setOrderSent(true);
      setCart([]);
    } catch (e) {
      setError(e.message || 'Sipariş gönderilemedi.');
    }
  };

  const handleCallWaiter = async () => {
    if (!table) return;
    setError('');
    try {
      await callWaiter(table.tableNumber, table.tableName, 'Masa çağrısı');
      setCallSent(true);
    } catch (e) {
      setError(e.message || 'Çağrı gönderilemedi.');
    }
  };

  if (loading) return <div className="loading">Yükleniyor…</div>;
  if (error && !table) return <div className="error-page">{error}</div>;

  return (
    <div className="table-menu">
      <header>
        <h1>Masa {table?.tableName ?? tableNumber}</h1>
      </header>

      {orderSent && <p className="success">Siparişiniz alındı.</p>}
      {callSent && <p className="success">Garson çağrınız iletildi.</p>}
      {error && <p className="error">{error}</p>}

      <div className="content">
        <section className="products">
          <h2>Menü</h2>
          <ul className="product-list">
            {products.map((p) => (
              <li key={p.id} className="product-card">
                {p.imageUrl && (
                  <img src={p.imageUrl} alt={p.name} className="product-img" />
                )}
                <div className="product-info">
                  <span className="product-name">{p.name}</span>
                  {p.description && (
                    <span className="product-desc">{p.description}</span>
                  )}
                  <span className="product-price">
                    ₺{Number(p.price).toFixed(2)}
                  </span>
                  <button
                    type="button"
                    className="btn-add"
                    onClick={() => addToCart(p)}
                  >
                    Ekle
                  </button>
                </div>
              </li>
            ))}
          </ul>
        </section>

        <aside className="cart">
          <h2>Sepet</h2>
          {cart.length === 0 ? (
            <p className="cart-empty">Sepet boş</p>
          ) : (
            <>
              <ul className="cart-list">
                {cart.map((c) => (
                  <li key={c.productId}>
                    <span>{c.productName}</span>
                    <span className="cart-qty">
                      <button
                        type="button"
                        aria-label="Azalt"
                        onClick={() => updateCartQty(c.productId, -1)}
                      >
                        −
                      </button>
                      {c.quantity}
                      <button
                        type="button"
                        aria-label="Artır"
                        onClick={() => updateCartQty(c.productId, 1)}
                      >
                        +
                      </button>
                    </span>
                  </li>
                ))}
              </ul>
              <p className="cart-total">Toplam: ₺{totalAmount.toFixed(2)}</p>
              <button
                type="button"
                className="btn-order"
                onClick={handlePlaceOrder}
              >
                Sipariş ver
              </button>
            </>
          )}
          <button
            type="button"
            className="btn-call"
            onClick={handleCallWaiter}
            disabled={callSent}
          >
            {callSent ? 'Çağrı iletildi' : 'Garson çağır'}
          </button>
        </aside>
      </div>
    </div>
  );
}
