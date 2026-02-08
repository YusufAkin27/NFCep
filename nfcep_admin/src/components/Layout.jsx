import { Outlet, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Layout() {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const nav = [
    { to: '/', end: true, label: 'Dashboard' },
    { to: '/products', end: false, label: 'Ürünler' },
    { to: '/tables', end: false, label: 'Masalar' },
    { to: '/garsons', end: false, label: 'Garsonlar' },
    { to: '/mutfak', end: false, label: 'Mutfak' },
    { to: '/orders/new', end: false, label: 'Sipariş oluştur' },
  ];

  return (
    <div className="layout">
      <aside className="sidebar">
        <h1 className="sidebar-title">NFCep Admin</h1>
        <nav className="sidebar-nav">
          {nav.map(({ to, end, label }) => (
            <NavLink
              key={to}
              to={to}
              end={end}
              className={({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')}
            >
              {label}
            </NavLink>
          ))}
        </nav>
        <button type="button" className="btn-logout" onClick={handleLogout}>
          Çıkış
        </button>
      </aside>
      <main className="main">
        <Outlet />
      </main>
    </div>
  );
}
