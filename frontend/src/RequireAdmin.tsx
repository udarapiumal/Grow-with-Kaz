import { useEffect, useState } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { me } from './AdminApi';


/**
 * Route guard: checks the session with the backend before rendering whatever
 * is nested inside it. The backend, not this component, is the real gate —
 * this only decides what to show while we wait for the answer.
 */
export default function RequireAdmin() {
  const [status, setStatus] = useState<'checking' | 'signed-in' | 'signed-out'>('checking');

  useEffect(() => {
    let cancelled = false;
    me().then((admin) => {
      if (!cancelled) setStatus(admin ? 'signed-in' : 'signed-out');
    });
    return () => { cancelled = true; };
  }, []);

  if (status === 'checking') return <p className="admin-loading">Checking session…</p>;
  if (status === 'signed-out') return <Navigate to="/admin/login" replace />;
  return <Outlet />;
}