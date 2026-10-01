/*
  GridWeaver frontend.
  DEMO_MODE = true means it works immediately without a backend.
  When your Spring Boot REST API is ready, set DEMO_MODE = false and
  update API_URL to the endpoint returning:
  { generation, load, battery, gridImport, balance, status, devices }
*/
const DEMO_MODE = false;
const API_URL = "http://localhost:8080/api/grid";

const initial = {
  generation: 32.5,
  load: 28.0,
  battery: 5.0,
  gridImport: 0.0,
  balance: 9.5,
  status: "SURPLUS",
  devices: 5
};

let state = {...initial};

const $ = id => document.getElementById(id);
const kw = n => `${Number(n).toFixed(2)} kW`;

function render() {
  $("generation").textContent = kw(state.generation);
  $("load").textContent = kw(state.load);
  $("battery").textContent = kw(state.battery);
  $("gridImport").textContent = kw(state.gridImport);
  $("balance").textContent = `${state.balance >= 0 ? "+" : ""}${kw(state.balance)}`;
  $("status").textContent = state.status;
  $("statusText").textContent =
    state.status === "GRID_IMPORT" ? "The microgrid needs power from the external grid." :
    state.status === "BALANCED" ? "Generation and consumption are approximately balanced." :
    "The microgrid currently has available power.";

  $("flowSolar").textContent = kw(state.generation);
  $("flowBattery").textContent = kw(state.battery);
  $("flowLoad").textContent = kw(state.load);
  $("flowBalance").textContent = kw(state.balance);
  $("deviceCount").textContent = `${state.devices} devices`;
  $("devicesInfo").textContent = state.devices;
}

function calculate() {
  state.balance = state.generation + state.battery - state.load;
  state.gridImport = Math.max(0, -state.balance);
  state.status = state.balance > 0.01 ? "SURPLUS" :
                 state.balance < -0.01 ? "GRID_IMPORT" : "BALANCED";
  render();
}

async function loadFromBackend() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("API error");
    state = await response.json();
    $("dot").style.background = "#47d7a1";
    $("connectionText").textContent = "Backend connected";
    $("modeInfo").textContent = "Live API";
    render();
  } catch (error) {
    $("connectionText").textContent = "Backend unavailable";
    $("modeInfo").textContent = "Demo fallback";
  }
}

$("simulate").addEventListener("click", async () => {

  const generation = +(30 + Math.random() * 6).toFixed(2);
  const load = +(25 + Math.random() * 7).toFixed(2);

  try {

    // Update solar sensor
    await fetch("http://localhost:8080/api/sensor", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        deviceId: "solar-02",
        powerKw: generation - 18.5
      })
    });

    // Update load sensor
    await fetch("http://localhost:8080/api/sensor", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        deviceId: "load-02",
        powerKw: load - 20
      })
    });

    // Wait for asynchronous virtual-thread updates
    setTimeout(loadFromBackend, 150);

  } catch (error) {

    console.error("Sensor update failed:", error);

    alert(
      "Could not connect to GridWeaver backend.\n" +
      "Make sure the Java server is running."
    );
  }
});

$("reset").addEventListener("click", () => {
  state = {...initial};
  render();
});

render();

if (!DEMO_MODE) {
  loadFromBackend();
  setInterval(loadFromBackend, 2000);
}
