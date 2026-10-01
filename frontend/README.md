# GridWeaver Frontend

Browser dashboard for the GridWeaver Java 21 IoT Microgrid project.

## Quick run in VS Code

1. Open this `GridWeaver-Frontend` folder in VS Code.
2. Install the **Live Server** extension if you do not already have it.
3. Right-click `index.html`.
4. Select **Open with Live Server**.
5. The dashboard opens in your browser.

No Node.js or npm is required for this frontend.

## Demo mode

The frontend works immediately in demo mode. Click **Simulate Sensor Update** to change the microgrid values.

## Connecting a backend

Open `app.js` and change:

```js
const DEMO_MODE = false;
const API_URL = "http://localhost:8080/api/grid";
```

Your backend endpoint should return JSON in this shape:

```json
{
  "generation": 32.5,
  "load": 28.0,
  "battery": 5.0,
  "gridImport": 0.0,
  "balance": 9.5,
  "status": "SURPLUS",
  "devices": 5
}
```
