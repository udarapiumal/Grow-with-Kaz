import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { type Product, fetchProductBySlug, formatPrice } from './Api';


export default function ProductDetail() {
  const { slug } = useParams<{ slug: string }>();
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!slug) return;
    setLoading(true);
    fetchProductBySlug(slug)
      .then(setProduct)
      .catch((err) => setError(err instanceof Error ? err.message : 'Could not load this product.'))
      .finally(() => setLoading(false));
  }, [slug]);

  if (loading) return <p className="products__state">Loading…</p>;

  if (error || !product) {
    return (
      <section className="section">
        <p className="products__state products__state--error">
          {error ?? "That product doesn't exist or is no longer available."}
        </p>
        <Link to="/products" className="btn btn--ghost">← All products</Link>
      </section>
    );
  }

  return (
    <section className="section product-detail">
      <Link to="/products" className="product-detail__back">← All products</Link>

      <div className="product-detail__layout">
        <div className="product-detail__media">
          {product.imageUrl
            ? <img src={product.imageUrl} alt={product.title} />
            : <span className="card__media-empty" />}
        </div>

        <div>
          <span className="card__tag">{product.category}</span>
          <h1 className="product-detail__title">{product.title}</h1>
          <p className="product-detail__price">{formatPrice(product.priceCents)}</p>
          <p className="product-detail__desc">{product.description}</p>

          <div className="hero__actions" style={{ justifyContent: 'flex-start' }}>
            <Link to="/contact" className="btn btn--solid">Ask about this pack</Link>
          </div>
        </div>
      </div>
    </section>
  );
}