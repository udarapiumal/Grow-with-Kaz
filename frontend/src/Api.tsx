export type ContactPayload = {
  name: string;
  email: string;
  company: string;
  inquiryType: string;
  message: string;
  website: string; // honeypot, always sent empty by real users
};

export type Product = {
  slug: string;
  title: string;
  description: string;
  category: string;
  priceCents: number;
  imageUrl: string | null;
};

export type FieldErrors = Partial<Record<keyof ContactPayload, string>>;

export class ApiError extends Error {
  fieldErrors: FieldErrors;
  constructor(message: string, fieldErrors: FieldErrors = {}) {
    super(message);
    this.fieldErrors = fieldErrors;
  }
}

export function formatPrice(cents: number): string {
  return `$${(cents / 100).toFixed(2)}`;
}

export async function fetchProducts(): Promise<Product[]> {
  const response = await fetch('/api/products');
  if (!response.ok) throw new ApiError('Could not load products.');
  return response.json();
}

export async function fetchProductBySlug(slug: string): Promise<Product | null> {
  const response = await fetch(`/api/products/${encodeURIComponent(slug)}`);
  if (response.status === 404) return null;
  if (!response.ok) throw new ApiError('Could not load that product.');
  return response.json();
}

export async function sendContactMessage(payload: ContactPayload): Promise<string> {
  let response: Response;
  try {
    response = await fetch('/api/contact', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });
  } catch {
    throw new ApiError('Could not reach the server. Check your connection and try again.');
  }

  const body = await response.json().catch(() => ({}));

  if (!response.ok) {
    throw new ApiError(body.message ?? 'Something went wrong. Try again.', body.fieldErrors ?? {});
  }

  return body.message as string;
}