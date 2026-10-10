import { useEffect, useState } from 'react';
import { fetchInitialData, startSimulation, stopSimulation, toggleStorm } from './services/api.js';
import { connectWebSocket } from './services/websocket.js';
import Dashboard from './components/Dashboard.jsx';

const defaultStats = {
  totalNodes: 0,
  onlineNodes: 0,
  charging: 0,
  discharging: 0,
  faults: 0,
  totalSolarGeneration: 0,
  totalDemand: 0
};

function App() {
  const [nodes, setNodes] = useState([]);
  const [stats, setStats] = useState(defaultStats);
  const [events, setEvents] = useState([]);
  const [zones, setZones] = useState([]);
  const [status, setStatus] = useState('offline');
  const [stormMode, setStormMode] = useState(false);
  const [filter, setFilter] = useState('ALL');

  useEffect(() => {
    let client;

    fetchInitialData().then(({ nodes: initialNodes, stats: initialStats, events: initialEvents, zones: initialZones }) => {
      setNodes(initialNodes || []);
      setStats(initialStats || defaultStats);
      setEvents(initialEvents || []);
      setZones(initialZones || []);
      setStatus('connected');
    }).catch(() => setStatus('offline'));

    client = connectWebSocket({
      onNodes: (nextNodes) => setNodes(Array.isArray(nextNodes) ? nextNodes : []),
      onStats: (nextStats) => setStats(nextStats || defaultStats),
      onEvents: (nextEvents) => setEvents(nextEvents || []),
      onGrid: (nextZones) => setZones(nextZones || []),
      onStatus: setStatus
    });

    return () => client && client.deactivate();
  }, []);

  const handleStart = async (count) => {
    try {
      await startSimulation(count);
      setStatus('running');
    } catch (error) {
      setStatus('offline');
    }
  };

  const handleStop = async () => {
    try {
      await stopSimulation();
      setStatus('stopped');
    } catch (error) {
      setStatus('offline');
    }
  };

  const handleStorm = async (enabled) => {
    try {
      await toggleStorm(enabled);
      setStormMode(enabled);
      setStatus(enabled ? 'storm' : 'running');
    } catch (error) {
      setStatus('offline');
    }
  };

  return (
    <div className="app-shell">
      <header className="topbar">
        <div>
          <div className="eyebrow">VIRTUAL THREAD IOT MICROGRID ENGINE</div>
          <h1>GRIDWEAVER</h1>
        </div>
        <div className="header-status">
          <span className={`status-dot status-${status}`} />
          <span>{status.toUpperCase()}</span>
        </div>
      </header>

      <Dashboard
        nodes={nodes}
        stats={stats}
        events={events}
        zones={zones}
        status={status}
        stormMode={stormMode}
        filter={filter}
        setFilter={setFilter}
        onStart={handleStart}
        onStop={handleStop}
        onStorm={handleStorm}
      />
    </div>
  );
}

export default App;
