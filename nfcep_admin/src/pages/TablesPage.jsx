import { useState, useEffect } from 'react';
import { getTables, createTable, updateTable, deleteTable } from '../api';

export default function TablesPage() {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [modal, setModal] = useState(null);
  const [deleteId, setDeleteId] = useState(null);

  const load = () => {
    setLoading(true);
    getTables()
      .then(setList)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, []);

  const openAdd = () => setModal({ type: 'add', tableNumber: '', tableName: '' });
  const openEdit = (t) => setModal({ type: 'edit', id: t.id, tableNumber: t.tableNumber, tableName: t.tableName });
  const closeModal = () => setModal(null);

  const handleSubmitModal = async (e) => {
    e.preventDefault();
    if (!modal) return;
    setError('');
    try {
      if (modal.type === 'add') {
        await createTable(modal.tableNumber.trim(), modal.tableName.trim());
      } else {
        await updateTable(modal.id, { tableNumber: modal.tableNumber.trim(), tableName: modal.tableName.trim() });
      }
      closeModal();
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async () => {
    if (!deleteId) return;
    setError('');
    try {
      await deleteTable(deleteId);
      setDeleteId(null);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <div className="page-loading">Yükleniyor…</div>;

  return (
    <div className="page">
      <h1>Masalar</h1>
      {error && <div className="page-error" style={{ marginBottom: '1rem' }}>{error}</div>}
      <div className="page-actions">
        <button type="button" className="btn btn-primary" onClick={openAdd}>Masa ekle</button>
      </div>
      <table className="data-table">
        <thead>
          <tr>
            <th>Masa no</th>
            <th>Masa adı</th>
            <th>İşlemler</th>
          </tr>
        </thead>
        <tbody>
          {list.map((t) => (
            <tr key={t.id}>
              <td>{t.tableNumber}</td>
              <td>{t.tableName}</td>
              <td>
                <button type="button" className="btn btn-secondary btn-sm" onClick={() => openEdit(t)}>Düzenle</button>
                {' '}
                <button type="button" className="btn btn-danger btn-sm" onClick={() => setDeleteId(t.id)}>Sil</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {list.length === 0 && <p className="empty-msg">Masa yok.</p>}

      {modal && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h2>{modal.type === 'add' ? 'Masa ekle' : 'Masa düzenle'}</h2>
            <form onSubmit={handleSubmitModal}>
              <div className="form-group">
                <label>Masa numarası</label>
                <input value={modal.tableNumber} onChange={(e) => setModal({ ...modal, tableNumber: e.target.value })} required />
              </div>
              <div className="form-group">
                <label>Masa adı</label>
                <input value={modal.tableName} onChange={(e) => setModal({ ...modal, tableName: e.target.value })} required />
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
            <h2>Masayı sil</h2>
            <p>Bu masayı silmek istediğinize emin misiniz?</p>
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
