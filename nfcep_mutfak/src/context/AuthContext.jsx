import { createContext, useContext, useState, useEffect } from 'react';
import { getToken, setToken as apiSetToken } from '../api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setTokenState] = useState(getToken());

  useEffect(() => {
    const t = getToken();
    setTokenState(t);
  }, []);

  const login = (newToken) => {
    apiSetToken(newToken);
    setTokenState(newToken);
  };

  const logout = () => {
    apiSetToken(null);
    setTokenState(null);
  };

  return (
    <AuthContext.Provider value={{ token, login, logout, isLoggedIn: !!token }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
