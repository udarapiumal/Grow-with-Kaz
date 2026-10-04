import { Link } from 'react-router-dom';

const stats = [
  { value: '3M', label: 'Store visits' },
  { value: '764K', label: 'Downloads' },
  { value: '28M', label: 'Video views' },
  { value: '301K', label: 'Subscribers' },
];

const featured = [
  {
    tag: 'Audio',
    price: '$39',
    title: '1,300+ Cinematic Sound Effects',
    blurb: 'Whooshes, risers, impacts and drones, cleaned and named so you can find them fast.',
  },
  {
    tag: 'Transitions',
    price: '$50',
    title: 'The Ultimate Effects Pack',
    blurb: 'A Premiere Pro plugin with 700+ transitions you preview and drop in from one panel.',
  },
  {
    tag: 'Color',
    price: '$25',
    title: 'FILM LUTs',
    blurb: '26 cinematic looks, built for both standard and log footage.',
  },
];

export default function Home() {
  return (
    <>
      <section className="hero">
        <p className="hero__kicker">Presets, transitions, digital assets</p>
        <h1 className="hero__title">
          Grow With <span>Kaz</span>
        </h1>
        <p className="hero__role">Senior video specialist at FloSports</p>
        <p className="hero__text">
          I edit sports and brand films by day and build the tools I wish I had while
          doing it. Everything here is what I actually use, packaged so you can drop it
          straight into a timeline.
        </p>
        <div className="hero__actions">
          <Link to="/products" className="btn btn--solid">Browse products</Link>
          <a className="btn btn--ghost" href="https://www.youtube.com/@GrowWithKaz" target="_blank" rel="noreferrer">
            Watch on YouTube
          </a>
        </div>

        <dl className="stats">
          {stats.map((stat) => (
            <div key={stat.label} className="stats__item">
              <dt>{stat.value}</dt>
              <dd>{stat.label}</dd>
            </div>
          ))}
        </dl>
      </section>

      <section className="section">
        <h2 className="section__title">Top digital assets</h2>
        <p className="section__lead">
          The three packs creators reach for first.
        </p>

        <div className="cards">
          {featured.map((item) => (
            <article key={item.title} className="card">
              <div className="card__top">
                <span className="card__tag">{item.tag}</span>
                <span className="card__price">{item.price}</span>
              </div>
              <h3 className="card__title">{item.title}</h3>
              <p className="card__blurb">{item.blurb}</p>
              <Link to="/products" className="card__link">Learn more</Link>
            </article>
          ))}
        </div>
      </section>

      <section className="section band">
        <h2 className="band__title">Working on something bigger?</h2>
        <p className="band__text">
          I take on freelance edits, brand films and creative direction. Tell me what you
          have in mind and I will reply by email.
        </p>
        <Link to="/contact" className="btn btn--solid">Get in touch</Link>
      </section>
    </>
  );
}