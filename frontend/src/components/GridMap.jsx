import { MapContainer, TileLayer, CircleMarker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

const stateColors = {
  IDLE: '#5ab0ff',
  CHARGING: '#44d37a',
  DISCHARGING: '#ffb347',
  FAULT: '#ff6b6b'
};

export default function GridMap({ nodes }) {
  const visibleNodes = nodes.slice(0, 500);

  return (
    <div className="panel map-panel">
      <div className="panel-header">
        <div>
          <h3>Live Grid Map</h3>
          <div style={{
            marginTop: '5px',
            color: '#718696',
            fontSize: '10px'
          }}>
            Real-time IoT node distribution
          </div>
        </div>

        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '8px'
        }}>
          <span style={{
            width: '7px',
            height: '7px',
            borderRadius: '50%',
            background: '#55e39a',
            boxShadow: '0 0 10px rgba(85,227,154,.7)'
          }} />

          <span className="map-badge">
            {nodes.length.toLocaleString()} nodes
          </span>
        </div>
      </div>

      <div style={{
        position: 'relative',
        padding: '14px'
      }}>
        <MapContainer
          center={[28.55, 77.4]}
          zoom={11}
          scrollWheelZoom
          className="map-container"
        >
          <TileLayer
            attribution="&copy; OpenStreetMap contributors"
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />

          {visibleNodes.map((node) => {
            const color = stateColors[node.state] || '#5ab0ff';

            return (
              <CircleMarker
                key={node.nodeId}
                center={[node.latitude, node.longitude]}
                radius={6}
                pathOptions={{
                  color,
                  fillColor: color,
                  fillOpacity: 0.9,
                  weight: 2
                }}
              >
                <Popup>
                  <div style={{
                    minWidth: '160px',
                    lineHeight: '1.7'
                  }}>
                    <strong>{node.nodeId}</strong>
                    <br />
                    Battery: {node.batteryPercentage.toFixed(0)}%
                    <br />
                    Solar: {node.solarPower.toFixed(1)} kW
                    <br />
                    Demand: {node.powerDemand.toFixed(1)} kW
                    <br />
                    State: {node.state}
                    <br />
                    Zone: {node.zone}
                    <br />
                    Updated: {new Date(node.lastUpdated).toLocaleTimeString()}
                  </div>
                </Popup>
              </CircleMarker>
            );
          })}
        </MapContainer>

        <div style={{
          position: 'absolute',
          left: '28px',
          bottom: '28px',
          zIndex: 1000,
          display: 'flex',
          gap: '8px',
          padding: '8px 10px',
          border: '1px solid rgba(255,255,255,.14)',
          borderRadius: '10px',
          background: 'rgba(7,16,24,.88)',
          backdropFilter: 'blur(10px)',
          color: '#c8d8e4',
          fontSize: '9px'
        }}>
          <span>● Idle</span>
          <span style={{ color: '#55e39a' }}>● Charging</span>
          <span style={{ color: '#ffc15c' }}>● Discharging</span>
          <span style={{ color: '#ff6875' }}>● Fault</span>
        </div>
      </div>
    </div>
  );
}