import { useState, useEffect } from 'react';
import { getStatistics } from '../api';

export default function DashboardPage() {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    getStatistics()
      .then(setList)
      .catch((e) => setError(e.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="page-loading">Yükleniyor…</div>;
  if (error) return <div className="page-error">{error}</div>;

  const roleLabel = (roles) => {
    if (!roles || !roles.length) return '-';
    return [...roles].map((r) => (r === 'GARSON' ? 'Garson' : r === 'MUTFAK' ? 'Mutfak' : r)).join(', ');
  };

  return (
    <div className="page">
      <h1>İstatistikler</h1>
      <div className="dashboard-cards">
        {list.length === 0 ? (
          <p className="empty-msg">Kayıtlı personel yok.</p>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>Kullanıcı adı</th>
                <th>Ad Soyad</th>
                <th>Rol</th>
                <th>Bugün çalışıyor</th>
                <th>Çalışılan gün sayısı</th>
              </tr>
            </thead>
            <tbody>
              {list.map((row) => (
                <tr key={row.id}>
                  <td>{row.username}</td>
                  <td>{[row.firstName, row.lastName].filter(Boolean).join(' ') || '-'}</td>
                  <td>{roleLabel(row.roles)}</td>
                  <td>{row.workingToday === true ? 'Evet' : 'Hayır'}</td>
                  <td>{row.workDaysCount ?? 0}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
