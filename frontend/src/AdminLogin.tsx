import { useState, type FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from './AdminApi';


export default function AdminLogin() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [sending, setSending] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSending(true);
    setError(null);
    try {
      await login(email, password);
      navigate('/admin/products');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Could not sign in.');
    } finally {
      setSending(false);
    }
  }

  return (
    <section className="admin-auth">
      <form className="form admin-auth__form" onSubmit={handleSubmit} noValidate>
        <h1 className="form__title">Admin sign in</h1>
        <p className="form__note">For site owner use only.</p>

        {error && <p className="form__banner" role="alert">{error}</p>}

        <p className="field">
          <label htmlFor="admin-email">Email</label>
          <input
            id="admin-email"
            type="email"
            autoComplete="username"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </p>

        <p className="field">
          <label htmlFor="admin-password">Password</label>
          <input
            id="admin-password"
            type="password"
            autoComplete="current-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </p>

        <button className="btn btn--solid btn--block" disabled={sending}>
          {sending ? 'Signing in…' : 'Sign in'}
        </button>
      </form>
    </section>
  );
}