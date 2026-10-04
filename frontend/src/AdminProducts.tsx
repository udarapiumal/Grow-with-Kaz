import { useEffect, useState, type FormEvent } from 'react';
import { Link } from 'react-router-dom';
import { listProducts, saveProduct, deleteProduct, logout } from './AdminApi';
import type { AdminProduct } from './AdminApi';


const emptyForm = { title: '', description: '', category: '', price: '', active: true };

export default function AdminProducts() {
  const [products, setProducts] = useState<AdminProduct[]>([]);
  const [loading, setLoading] = useState(true);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [form, setForm] = useState(emptyForm);
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [saving, setSaving] = useState(false);

  async function refresh() {
    setLoading(true);
    try {
      setProducts(await listProducts());
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Could not load products.');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { refresh(); }, []);

  function startEdit(p: AdminProduct) {
    setEditingId(p.id);
    setForm({
      title: p.title,
      description: p.description,
      category: p.category,
      price: (p.priceCents / 100).toFixed(2),
      active: p.active,
    });
    setImageFile(null);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  function startNew() {
    setEditingId(null);
    setForm(emptyForm);
    setImageFile(null);
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSaving(true);
    setError(null);

    const priceCents = Math.round(parseFloat(form.price || '0') * 100);
    const data = new FormData();
    data.set('title', form.title);
    data.set('description', form.description);
    data.set('category', form.category);
    data.set('priceCents', String(priceCents));
    data.set('active', String(form.active));
    if (imageFile) data.set('image', imageFile);

    try {
      await saveProduct(editingId, data);
      startNew();
      await refresh();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Could not save product.');
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete(id: number) {
    if (!confirm('Delete this product? This cannot be undone.')) return;
    try {
      await deleteProduct(id);
      if (editingId === id) startNew();
      await refresh();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Could not delete product.');
    }
  }

  return (
    <section className="admin-page">
      <div className="admin-page__header">
        <h1>Products</h1>
        <button className="btn btn--ghost" onClick={() => logout().then(() => { window.location.href = '/admin/login'; })}>
          Sign out
        </button>
      </div>

      {error && <p className="form__banner" role="alert">{error}</p>}

      <form className="form admin-form" onSubmit={handleSubmit} noValidate>
        <h2 className="form__title">{editingId ? 'Edit product' : 'Add a product'}</h2>

        <div className="form__row">
          <p className="field">
            <label htmlFor="p-title">Title</label>
            <input id="p-title" value={form.title}
                   onChange={(e) => setForm((f) => ({ ...f, title: e.target.value }))} />
          </p>
          <p className="field">
            <label htmlFor="p-category">Category</label>
            <input id="p-category" value={form.category}
                   onChange={(e) => setForm((f) => ({ ...f, category: e.target.value }))} />
          </p>
        </div>

        <p className="field">
          <label htmlFor="p-description">Description</label>
          <textarea id="p-description" rows={4} value={form.description}
                    onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))} />
        </p>

        <div className="form__row">
          <p className="field">
            <label htmlFor="p-price">Price (USD)</label>
            <input id="p-price" type="number" min="0" step="0.01" value={form.price}
                   onChange={(e) => setForm((f) => ({ ...f, price: e.target.value }))} />
          </p>
          <p className="field">
            <label htmlFor="p-image">
              Image {editingId && <span className="field__hint">(leave empty to keep current)</span>}
            </label>
            <input id="p-image" type="file" accept="image/jpeg,image/png,image/webp"
                   onChange={(e) => setImageFile(e.target.files?.[0] ?? null)} />
          </p>
        </div>

        <label className="admin-checkbox">
          <input type="checkbox" checked={form.active}
                 onChange={(e) => setForm((f) => ({ ...f, active: e.target.checked }))} />
          Visible on the site
        </label>

        <div className="admin-form__actions">
          <button className="btn btn--solid" disabled={saving}>
            {saving ? 'Saving…' : editingId ? 'Save changes' : 'Add product'}
          </button>
          {editingId && (
            <button type="button" className="btn btn--ghost" onClick={startNew}>Cancel edit</button>
          )}
        </div>
      </form>

      <div className="admin-table-wrap">
        {loading ? (
          <p>Loading…</p>
        ) : products.length === 0 ? (
          <p>No products yet — add your first one above.</p>
        ) : (
          <table className="admin-table">
            <thead>
              <tr><th></th><th>Title</th><th>Category</th><th>Price</th><th>Status</th><th></th></tr>
            </thead>
            <tbody>
              {products.map((p) => (
                <tr key={p.id}>
                  <td>
                    {p.imageUrl
                      ? <img src={p.imageUrl} alt="" className="admin-table__thumb" />
                      : <span className="admin-table__thumb admin-table__thumb--empty" />}
                  </td>
                  <td>{p.title}</td>
                  <td>{p.category}</td>
                  <td>${(p.priceCents / 100).toFixed(2)}</td>
                  <td>{p.active ? 'Visible' : 'Hidden'}</td>
                  <td className="admin-table__actions">
                    <button className="btn btn--ghost" onClick={() => startEdit(p)}>Edit</button>
                    <button className="btn btn--ghost" onClick={() => handleDelete(p.id)}>Delete</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      <Link to="/" className="admin-page__back">← Back to site</Link>
    </section>
  );
}