import { useState, useEffect } from 'react';
import { getMutfak, createMutfak, changeMutfakPassword, deleteMutfak } from '../api';

export default function MutfakPage() {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [modal, setModal] = useState(null);
  const [passwordModal, setPasswordModal] = useState(null);
  const [deleteId, setDeleteId] = useState(null);

  const load = () => {
    setLoading(true);
    getMutfak()
      .then(setList)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, []);

  const openAdd = () => setModal({ username: '', password: '', firstName: '', lastName: '' });
  const closeModal = () => setModal(null);

  const handleSubmitModal = async (e) => {
    e.preventDefault();
    if (!modal) return;
    setError('');
    try {
      await createMutfak({
        username: modal.username.trim(),
        password: modal.password,
        firstName: modal.firstName?.trim() || null,
        lastName: modal.lastName?.trim() || null,
      });
      closeModal();
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  const openPassword = (m) => setPasswordModal({ id: m.id, username: m.username, newPassword: '' });
  const submitPassword = async (e) => {
    e.preventDefault();
    if (!passwordModal?.newPassword?.trim()) return;
    setError('');
    try {
      await changeMutfakPassword(passwordModal.id, passwordModal.newPassword);
      setPasswordModal(null);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async () => {
    if (!deleteId) return;
    setError('');
    try {
      await deleteMutfak(deleteId);
      setDeleteId(null);
      load();
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <div className="page-loading">Yükleniyor…</div>;

  return (
    <div className="page">
      <h1>Mutfak</h1>
      {error && <div className="page-error" style={{ marginBottom: '1rem' }}>{error}</div>}
      <div className="page-actions">
        <button type="button" className="btn btn-primary" onClick={openAdd}>Mutfak kullanıcısı ekle</button>
      </div>
      <table className="data-table">
        <thead>
          <tr>
            <th>Kullanıcı adı</th>
            <th>Ad Soyad</th>
            <th>İşlemler</th>
          </tr>
        </thead>
        <tbody>
          {list.map((m) => (
            <tr key={m.id}>
              <td>{m.username}</td>
              <td>{[m.firstName, m.lastName].filter(Boolean).join(' ') || '-'}</td>
              <td>
                <button type="button" className="btn btn-secondary btn-sm" onClick={() => openPassword(m)}>Şifre değiştir</button>
                {' '}
                <button type="button" className="btn btn-danger btn-sm" onClick={() => setDeleteId(m.id)}>Sil</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {list.length === 0 && <p className="empty-msg">Mutfak kullanıcısı yok.</p>}

      {modal && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h2>Mutfak kullanıcısı ekle</h2>
            <form onSubmit={handleSubmitModal}>
              <div className="form-group">
                <label>Kullanıcı adı</label>
                <input value={modal.username} onChange={(e) => setModal({ ...modal, username: e.target.value })} required />
              </div>
              <div className="form-group">
                <label>Şifre (en az 6 karakter)</label>
                <input type="password" value={modal.password} onChange={(e) => setModal({ ...modal, password: e.target.value })} required minLength={6} />
              </div>
              <div className="form-group">
                <label>Ad</label>
                <input value={modal.firstName} onChange={(e) => setModal({ ...modal, firstName: e.target.value })} />
              </div>
              <div className="form-group">
                <label>Soyad</label>
                <input value={modal.lastName} onChange={(e) => setModal({ ...modal, lastName: e.target.value })} />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-secondary" onClick={closeModal}>İptal</button>
                <button type="submit" className="btn btn-primary">Kaydet</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {passwordModal && (
        <div className="modal-overlay" onClick={() => setPasswordModal(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h2>Şifre değiştir: {passwordModal.username}</h2>
            <form onSubmit={submitPassword}>
              <div className="form-group">
                <label>Yeni şifre (en az 6 karakter)</label>
                <input type="password" value={passwordModal.newPassword} onChange={(e) => setPasswordModal({ ...passwordModal, newPassword: e.target.value })} required minLength={6} />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn btn-secondary" onClick={() => setPasswordModal(null)}>İptal</button>
                <button type="submit" className="btn btn-primary">Kaydet</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {deleteId && (
        <div className="modal-overlay" onClick={() => setDeleteId(null)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h2>Mutfak kullanıcısını sil</h2>
            <p>Bu kullanıcıyı silmek istediğinize emin misiniz?</p>
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
