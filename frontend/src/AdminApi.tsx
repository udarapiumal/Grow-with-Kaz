export type AdminProduct = {
    id: number;
    slug: string;
    title: string;
    description: string;
    category: string;
    priceCents: number;
    imageUrl: string | null;
    active: boolean;
  };
  
  // `credentials: 'include'` sends the session cookie. Through the Vite proxy
  // this is same-origin anyway, but keeping it explicit means it still works
  // once frontend and backend are on different hosts in production.
  async function request(path: string, options: RequestInit = {}) {
    return fetch(path, { credentials: 'include', ...options });
  }
  
  export async function login(email: string, password: string): Promise<void> {
    const res = await request('/api/admin/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password }),
    });
    if (!res.ok) {
      const body = await res.json().catch(() => ({}));
      throw new Error(body.message ?? 'Could not sign in.');
    }
  }
  
  export async function logout(): Promise<void> {
    await request('/api/admin/logout', { method: 'POST' });
  }
  
  export async function me(): Promise<{ email: string } | null> {
    const res = await request('/api/admin/me');
    if (!res.ok) return null;
    return res.json();
  }
  
  export async function listProducts(): Promise<AdminProduct[]> {
    const res = await request('/api/admin/products');
    if (!res.ok) throw new Error('Could not load products.');
    return res.json();
  }
  
  export async function saveProduct(id: number | null, form: FormData): Promise<AdminProduct> {
    const res = await request(id ? `/api/admin/products/${id}` : '/api/admin/products', {
      method: id ? 'PUT' : 'POST',
      body: form, // browser sets the multipart Content-Type header itself
    });
    if (!res.ok) {
      const body = await res.json().catch(() => ({}));
      throw new Error(body.message ?? 'Could not save product.');
    }
    return res.json();
  }
  
  export async function deleteProduct(id: number): Promise<void> {
    const res = await request(`/api/admin/products/${id}`, { method: 'DELETE' });
    if (!res.ok) throw new Error('Could not delete product.');
  }