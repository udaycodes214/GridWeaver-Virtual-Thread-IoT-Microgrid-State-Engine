export default function ControlPanel({ onStart, onStop, onStorm, stormMode, status }) {
  return (
    <div className="panel control-panel">
      <div className="panel-header">
        <h3>Control panel</h3>
        <span className={`status-pill status-${status}`}>{status}</span>
      </div>
      <div className="button-row">
        <button onClick={() => onStart(100)}>Start 100 Nodes</button>
        <button onClick={() => onStart(1000)}>Start 1,000 Nodes</button>
        <button onClick={() => onStart(10000)}>Start 10,000</button>
        <button onClick={() => onStart(50000)}>Start 50,000</button>
        <button className="secondary" onClick={onStop}>Stop Simulation</button>
        <button className="secondary" onClick={() => onStorm(!stormMode)}>{stormMode ? 'Stop Storm' : 'Start Storm'}</button>
      </div>
    </div>
  );
}
