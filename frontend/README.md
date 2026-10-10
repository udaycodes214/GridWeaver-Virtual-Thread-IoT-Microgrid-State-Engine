# GridWeaver frontend

A responsive, dark energy-monitoring dashboard built with React and Vite. The
overview shows summary counts and sample IoT node data. The data is currently
defined in `src/App.jsx`; the frontend does not yet connect to the backend.

## Run locally

```sh
npm install
npm run dev
```

Vite prints the local development URL after it starts.

## Validate

```sh
npm run lint
npm run build
```

## Project files

- `src/App.jsx` contains the sample data and dashboard components.
- `src/App.css` contains dashboard layout and responsive styles.
- `src/index.css` contains global page styles.
- `index.html` sets the browser title and page metadata.
