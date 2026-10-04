import { useState, type FormEvent } from 'react';
import { ApiError, sendContactMessage, type ContactPayload, type FieldErrors } from './Api';


const inquiryTypes = [
  { value: 'JOB', label: 'Job opportunity' },
  { value: 'FREELANCE', label: 'Freelance video project' },
  { value: 'BRAND', label: 'Brand collaboration' },
  { value: 'SPEAKING', label: 'Speaking or workshop' },
  { value: 'OTHER', label: 'Something else' },
];

const empty: ContactPayload = {
  name: '',
  email: '',
  company: '',
  inquiryType: '',
  message: '',
  website: '',
};

export default function Contact() {
  const [form, setForm] = useState<ContactPayload>(empty);
  const [errors, setErrors] = useState<FieldErrors>({});
  const [banner, setBanner] = useState<string | null>(null);
  const [sent, setSent] = useState<string | null>(null);
  const [sending, setSending] = useState(false);

  function update<K extends keyof ContactPayload>(key: K, value: string) {
    setForm((prev) => ({ ...prev, [key]: value }));
    setErrors((prev) => ({ ...prev, [key]: undefined }));
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSending(true);
    setBanner(null);
    setErrors({});

    try {
      const message = await sendContactMessage(form);
      setSent(message);
      setForm(empty);
    } catch (error) {
      if (error instanceof ApiError) {
        setBanner(error.message);
        setErrors(error.fieldErrors);
      } else {
        setBanner('Something went wrong. Try again.');
      }
    } finally {
      setSending(false);
    }
  }

  if (sent) {
    return (
      <section className="contact">
        <div className="contact__done">
          <h1>Message sent</h1>
          <p>{sent}</p>
          <button className="btn btn--ghost" onClick={() => setSent(null)}>
            Send another
          </button>
        </div>
      </section>
    );
  }

  return (
    <section className="contact">
      <div className="contact__intro">
        <h1 className="contact__title">Let's make your next project stand out.</h1>
        <p className="contact__lead">
          Reach out about job opportunities, freelance video projects, brand
          collaborations and speaking gigs.
        </p>
        <ul className="contact__list">
          <li>Video editing and post-production</li>
          <li>Motion graphics and creative direction</li>
          <li>Sports highlights and branded content</li>
          <li>Creator partnerships and consulting</li>
        </ul>
      </div>

      <form className="form" onSubmit={handleSubmit} noValidate>
        <h2 className="form__title">Send an inquiry</h2>
        <p className="form__note">Share a few details and I will follow up by email.</p>

        {banner && <p className="form__banner" role="alert">{banner}</p>}

        <div className="form__row">
          <Field label="Name" error={errors.name}>
            <input
              id="name"
              value={form.name}
              onChange={(e) => update('name', e.target.value)}
              autoComplete="name"
            />
          </Field>

          <Field label="Email" error={errors.email}>
            <input
              id="email"
              type="email"
              value={form.email}
              onChange={(e) => update('email', e.target.value)}
              autoComplete="email"
            />
          </Field>
        </div>

        <Field label="Company" hint="optional" error={errors.company}>
          <input
            id="company"
            value={form.company}
            onChange={(e) => update('company', e.target.value)}
            autoComplete="organization"
          />
        </Field>

        <Field label="Inquiry type" error={errors.inquiryType}>
          <select
            id="inquiryType"
            value={form.inquiryType}
            onChange={(e) => update('inquiryType', e.target.value)}
          >
            <option value="">Select one</option>
            {inquiryTypes.map((type) => (
              <option key={type.value} value={type.value}>{type.label}</option>
            ))}
          </select>
        </Field>

        <Field label="Message" error={errors.message}>
          <textarea
            id="message"
            rows={6}
            value={form.message}
            onChange={(e) => update('message', e.target.value)}
            placeholder="Tell me about your project, role or timeline."
          />
        </Field>

        {/* Honeypot: hidden from people, irresistible to bots. */}
        <input
          className="form__trap"
          tabIndex={-1}
          autoComplete="off"
          aria-hidden="true"
          value={form.website}
          onChange={(e) => update('website', e.target.value)}
        />

        <button className="btn btn--solid btn--block" disabled={sending}>
          {sending ? 'Sending…' : 'Send inquiry'}
        </button>
      </form>
    </section>
  );
}

function Field({
  label,
  hint,
  error,
  children,
}: {
  label: string;
  hint?: string;
  error?: string;
  children: React.ReactElement<{ id: string }>;
}) {
  const id = children.props.id;
  return (
    <p className="field">
      <label htmlFor={id}>
        {label} {hint && <span className="field__hint">({hint})</span>}
      </label>
      {children}
      {error && <span className="field__error">{error}</span>}
    </p>
  );
}