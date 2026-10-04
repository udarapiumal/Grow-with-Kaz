import { NavLink, Link } from 'react-router-dom';
import { useState } from 'react';

const links = [
  { to: '/', label: 'Home' },
  { to: '/products', label: 'Products' },
  { to: '/contact', label: 'Contact' },
];

export default function NavBar() {
  const [open, setOpen] = useState(false);

  return (
    <header className="nav">
      <div className="nav__inner">
        <Link to="/" className="nav__brand" onClick={() => setOpen(false)}>
          <span className="nav__mark">GK</span>
          Grow With Kaz
        </Link>

        <button
          className="nav__toggle"
          aria-expanded={open}
          aria-label="Menu"
          onClick={() => setOpen((v) => !v)}
        >
          <span />
          <span />
        </button>

        <nav className={open ? 'nav__links is-open' : 'nav__links'}>
          {links.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              end={link.to === '/'}
              className={({ isActive }) => (isActive ? 'nav__link is-active' : 'nav__link')}
              onClick={() => setOpen(false)}
            >
              {link.label}
            </NavLink>
          ))}
        </nav>
      </div>
    </header>
  );
}