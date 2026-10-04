import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';


export default function Products() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchProducts()
      .then(setProducts)
      .catch((err) => setError(err instanceof Error ? err.message : 'Could not load products.'))
      .finally(() => setLoading(false));
  }, []);

  return (
    <section className="section products">
      <h1 className="section__title">All products</h1>
      <p className="section__lead">
        Presets, transitions, LUTs and sound effects, ready to drop into your timeline.
      </p>

      {loading && <p className="products__state">Loading products…</p>}
      {error && <p className="products__state products__state--error">{error}</p>}

      {!loading && !error && products.length === 0 && (
        <p className="products__state">No products yet — check back soon.</p>
      )}

      <div className="cards cards--products">
        {products.map((product) => (
          <Link key={product.slug} to={`/products/${product.slug}`} className="card card--link">
            <div className="card__media">
              {product.imageUrl
                ? <img src={product.imageUrl} alt="" />
                : <span className="card__media-empty" />}
            </div>
            <div className="card__top">
              <span className="card__tag">{product.category}</span>
              <span className="card__price">{formatPrice(product.priceCents)}</span>
            </div>
            <h3 className="card__title">{product.title}</h3>
            <p className="card__blurb">{product.description}</p>
            <span className="card__link">View details</span>
          </Link>
        ))}
      </div>
    </section>
  );
}