import StatisticsCards from './StatisticsCards.jsx';
import ControlPanel from './ControlPanel.jsx';
import GridMap from './GridMap.jsx';
import NodeList from './NodeList.jsx';
import EventLog from './EventLog.jsx';

export default function Dashboard({ nodes, stats, events, zones, status, stormMode, filter, setFilter, onStart, onStop, onStorm }) {
  return (
    <>
      <StatisticsCards stats={stats} />
      <div className="content-grid">
        <div className="left-column">
          <ControlPanel onStart={onStart} onStop={onStop} onStorm={onStorm} stormMode={stormMode} status={status} />
          <GridMap nodes={nodes} />
          <NodeList nodes={nodes} filter={filter} onFilterChange={setFilter} />
        </div>
        <div className="right-column">
          <section className="panel">
            <div className="panel-header">
              <h3>Grid Summary</h3>
            </div>
            <div className="zone-grid">
              {(zones || []).map((zone) => (
                <div key={zone.zoneName} className="zone-card">
                  <h4>{zone.zoneName}</h4>
                  <p>Nodes: {zone.nodeCount}</p>
                  <p>Generation: {zone.generation.toFixed(1)} kW</p>
                  <p>Demand: {zone.demand.toFixed(1)} kW</p>
                  <p>Balance: {zone.balance.toFixed(1)} kW</p>
                  <span className={`zone-status ${zone.status.toLowerCase()}`}>{zone.status}</span>
                </div>
              ))}
            </div>
          </section>
          <EventLog events={events} />
        </div>
      </div>
    </>
  );
}
