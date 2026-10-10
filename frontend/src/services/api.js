const API_BASE = 'http://localhost:8080';

export async function apiFetch(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  });

  if (!response.ok) {
    throw new Error(`Request failed: ${response.status}`);
  }

  const text = await response.text();
  return text ? JSON.parse(text) : null;
}

export function fetchInitialData() {
  return Promise.all([
    apiFetch('/api/nodes'),
    apiFetch('/api/nodes/stats'),
    apiFetch('/api/events'),
    apiFetch('/api/grid/zones')
  ]).then(([nodes, stats, events, zones]) => ({ nodes, stats, events, zones }));
}

export function startSimulation(count = 100) {
  return apiFetch(`/api/simulator/start?count=${count}`, { method: 'POST' });
}

export function stopSimulation() {
  return apiFetch('/api/simulator/stop', { method: 'POST' });
}

export function toggleStorm(enabled) {
  return apiFetch(`/api/simulator/storm?enabled=${enabled}`, { method: 'POST' });
}
