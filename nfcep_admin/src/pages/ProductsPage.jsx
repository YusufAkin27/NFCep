import { useState, useEffect } from 'react';
import { getProducts, createProduct, updateProduct, deleteProduct } from '../api';

export default function ProductsPage() {
  const [page, setPage] = useState({ content: [], totalPages: 0, number: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [modal, setModal] = useState(null);
  const [deleteId, setDeleteId] = useState(null);

  const load = (pageNum = 0) => {
    setLoading(true);
    getProducts(pageNum, 20)
      .then((p) => setPage({ content: p.content || [], totalPages: p.totalPages || 0, number: p.number ?? 0 }))
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, []);

  const openAdd = () => setModal({ type: 'add', name: '', description: '', inStock: true, price: '', photo: null });
  const openEdit = (p) => setModal({ type: 'edit', id: p.id, name: p.name, description: p.description || '', inStock: p.inStock, price: String(p.price), photo: null, imageUrl: p.imageUrl });
  const closeModal = () => setModal(null);

  const handleSubmitModal = async (e) => {
    e.preventDefault();
    if (!modal) return;
    setError('');
    try {
      const payload = {
        name: modal.name.trim(),
        description: modal.description?.trim() || null,
        inStock: !!modal.inStock,
        price: parseFloat(modal.price),
      };
      if (modal.type === 'add') {
        await createProduct(payload, modal.photo || undefined);
      } else {
        await updateProduct(modal.id, payload, modal.photo || undefined);
      }
      closeModal();
      load(page.number);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async () => {
    if (!deleteId) return;
    setError('');
    try {
      await deleteProduct(deleteId);
      setDeleteId(null);
      load(page.number);
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading && page.content.length === 0) return <div className="page-loading">Yükleniyor…</div>;

  return (
    <div className="page">
      <h1>Ürünler</h1>
      {error && <div className="page-error" style={{ marginBottom: '1rem' }}>{error}</div>}
      <div className="page-actions">
        <button type="button" className="btn btn-primary" onClick={openAdd}>Ürün ekle</button>
      </div>
      <table className="data-table">
        <thead>
          <tr>
            <th>Ad</th>
            <th>Açıklama</th>
            <th>Fiyat</th>
            <th>Stok</th>
            <th>İşlemler</th>
          </tr>
        </thead>
        <tbody>
          {page.content.map((p) => (
            <tr key={p.id}>
              <td>{p.name}</td>
              <td>{(p.description || '').slice(0, 50)}{(p.description || '').length > 50 ? '…' : ''}</td>
              <td>₺{Number(p.price).toFixed(2)}</td>
              <td>{p.inStock ? 'Var' : 'Yok'}</td>
              <td>
                <button type="button" className="btn btn-secondary btn-sm" onClick={() => openEdit(p)}>Düzenle</button>
                {' '}
                <button type="button" className="btn btn-danger btn-sm" onClick={() => setDeleteId(p.id)}>Sil</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {page.totalPages > 1 && (
        <div className="page-actions" style={{ marginTop: '1rem' }}>
          <button type="button" className="btn btn-secondary btn-sm" disabled={page.number === 0} onClick={() => load(page.number - 1)}>Önceki</button>
          <span style={{ alignSelf: 'center' }}>Sayfa {page.number + 1} / {page.totalPages}</span>
          <button type="button" className="btn btn-secondary btn-sm" disabled={page.number >= page.totalPages - 1} onClick={() => load(page.number + 1)}>Sonraki</button>
        </div>
      )}

      {modal && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h2>{modal.type === 'add' ? 'Ürün ekle' : 'Ürün düzenle'}</h2>
            <form onSubmit={handleSubmitModal}>
              <div className="form-group">
                <label>Ad</label>
                <input value={modal.name} onChange={(e) => setModal({ ...modal, name: e.target.value })} required />
              </div>
              <div className="form-group">
                <label>Açıklama</label>
                <textarea value={modal.description} onChange={(e) => setModal({ ...modal, description: e.target.value })} rows={2} />
              </div>
              <div className="form-group">
                <label>Fiyat</label>
                <input type="number" step="0.01" min="0.01" value={modal.price} onChange={(e) => setModal({ ...modal, price: e.target.value })} required />
              </div>
              <div className="form-group">
                <label>
                  <input type="checkbox" checked={modal.inStock} onChange={(e) => setModal({ ...modal, inStock: e.target.checked })} />
                  {' '}Stokta var
                </label>
              </div>
              <div className="form-group">
                <label>Fotoğraf {modal.type === 'edit' && modal.imageUrl && '(mevcut görsel var, yeni yüklerseniz değişir)'}</label>
                <input type="file" accept="image/*" onChange={(e) => setModal({ ...modal, photo: e.target.files[0] || null })} />
                {modal.type === 'edit' && modal.imageUrl && <img src={modal.imageUrl} alt="" style={{ maxWidth: 120, marginTop: 8 }} />}
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-secondary" onClick={closeModal}>İptal</button>
                <button type="submit" className="btn btn-primary">Kaydet</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {deleteId && (
        <div className="modal-overlay" onClick={() => setDeleteId(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h2>Ürünü sil</h2>
            <p>Bu ürünü silmek istediğinize emin misiniz?</p>
            <div className="modal-actions">
              <button type="button" className="btn btn-secondary" onClick={() => setDeleteId(null)}>İptal</button>
              <button type="button" className="btn btn-danger" onClick={handleDelete}>Sil</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
