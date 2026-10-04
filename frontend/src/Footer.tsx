import { Link } from 'react-router-dom';

export default function Footer() {
  return (
    <footer className="footer">
      <div className="footer__inner">
        <div>
          <Link to="/" className="nav__brand">
            <span className="nav__mark">GK</span>
            Grow With Kaz
          </Link>
          <p className="footer__blurb">
            Presets, transitions and LUTs for editors who want their work to look
            finished, not filtered.
          </p>
        </div>

        <div className="footer__links">
          <h2>Site</h2>
          <Link to="/">Home</Link>
          <Link to="/products">Products</Link>
          <Link to="/contact">Contact</Link>
          <Link to="/admin/login">Admin</Link>
        </div>

        <div className="footer__links">
          <h2>Elsewhere</h2>
          <a href="https://www.youtube.com/@GrowWithKaz" target="_blank" rel="noreferrer">YouTube</a>
          <a href="https://instagram.com" target="_blank" rel="noreferrer">Instagram</a>
          <a href="https://tiktok.com" target="_blank" rel="noreferrer">TikTok</a>
        </div>
      </div>
      <p className="footer__legal">© {new Date().getFullYear()} Grow with Kaz</p>
    </footer>
  );
}