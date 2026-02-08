import { useState, useEffect } from 'react';
import { getTables, getProducts, createOrder } from '../api';

export default function OrderNewPage() {
  const [tables, setTables] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [tableNumber, setTableNumber] = useState('');
  const [tableName, setTableName] = useState('');
  const [items, setItems] = useState([]);

  useEffect(() => {
    Promise.all([getTables(), getProducts(0, 500)])
      .then(([tList, pPage]) => {
        setTables(tList);
        const content = pPage.content || pPage;
        setProducts(Array.isArray(content) ? content : []);
      })
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, []);

  const addItem = (product, qty = 1) => {
    const existing = items.find((i) => i.productId === product.id);
    if (existing) {
      setItems(items.map((i) => i.productId === product.id ? { ...i, quantity: i.quantity + qty } : i));
    } else {
      setItems([...items, { productId: product.id, productName: product.name, price: product.price, quantity: qty }]);
    }
  };

  const updateQty = (productId, delta) => {
    setItems((prev) => {
      const item = prev.find((i) => i.productId === productId);
      if (!item) return prev;
      const next = item.quantity + delta;
      if (next <= 0) return prev.filter((i) => i.productId !== productId);
      return prev.map((i) => i.productId === productId ? { ...i, quantity: next } : i);
    });
  };

  const selectTable = (t) => {
    setTableNumber(t.tableNumber);
    setTableName(t.tableName);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!tableNumber.trim() || !tableName.trim()) {
      setError('Masa numarası ve masa adı gerekli.');
      return;
    }
    if (items.length === 0) {
      setError('En az bir ürün ekleyin.');
      return;
    }
    setError('');
    setSuccess('');
    try {
      await createOrder(tableNumber.trim(), tableName.trim(), items.map((i) => ({ productId: i.productId, quantity: i.quantity })));
      setSuccess('Sipariş oluşturuldu.');
      setItems([]);
    } catch (err) {
      setError(err.message);
    }
  };

  const total = items.reduce((sum, i) => sum + Number(i.price) * i.quantity, 0);

  if (loading) return <div className="page-loading">Yükleniyor…</div>;

  return (
    <div className="page">
      <h1>Sipariş oluştur</h1>
      {error && <div className="page-error" style={{ marginBottom: '1rem' }}>{error}</div>}
      {success && <div style={{ marginBottom: '1rem', color: '#27ae60' }}>{success}</div>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Masa</label>
          <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap', marginBottom: '0.5rem' }}>
            {tables.map((t) => (
              <button key={t.id} type="button" className="btn btn-sm" style={{ background: tableNumber === t.tableNumber ? '#e94560' : '#ddd', color: tableNumber === t.tableNumber ? '#fff' : '#333' }} onClick={() => selectTable(t)}>
                {t.tableName} ({t.tableNumber})
              </button>
            ))}
          </div>
          <input placeholder="Masa numarası" value={tableNumber} onChange={(e) => setTableNumber(e.target.value)} />
          <input placeholder="Masa adı" value={tableName} onChange={(e) => setTableName(e.target.value)} style={{ marginTop: '0.35rem' }} />
        </div>
        <div className="form-group">
          <label>Ürünler (listeden ekleyin)</label>
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem', marginBottom: '0.5rem' }}>
            {products.filter((p) => p.inStock !== false).map((p) => (
              <button key={p.id} type="button" className="btn btn-secondary btn-sm" onClick={() => addItem(p)}>
                {p.name} (₺{Number(p.price).toFixed(2)})
              </button>
            ))}
          </div>
        </div>
        {items.length > 0 && (
          <div className="form-group">
            <label>Sepet</label>
            <ul style={{ listStyle: 'none', padding: 0, margin: 0 }}>
              {items.map((i) => (
                <li key={i.productId} style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.35rem' }}>
                  <span>{i.productName}</span>
                  <button type="button" className="btn btn-sm" onClick={() => updateQty(i.productId, -1)}>−</button>
                  <span>{i.quantity}</span>
                  <button type="button" className="btn btn-sm" onClick={() => updateQty(i.productId, 1)}>+</button>
                  <span>₺{(Number(i.price) * i.quantity).toFixed(2)}</span>
                </li>
              ))}
            </ul>
            <p><strong>Toplam: ₺{total.toFixed(2)}</strong></p>
          </div>
        )}
        <button type="submit" className="btn btn-primary" disabled={items.length === 0 || !tableNumber.trim() || !tableName.trim()}>
          Sipariş ver
        </button>
      </form>
    </div>
  );
}
