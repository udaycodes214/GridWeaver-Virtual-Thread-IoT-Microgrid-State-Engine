export default function NodeList({ nodes, filter, onFilterChange }) {
  const filtered =
    filter === 'ALL'
      ? nodes
      : nodes.filter((node) => node.state === filter);

  return (
    <div className="panel">
      <div className="panel-header">
        <div>
          <h3>Node Monitor</h3>

          <div style={{
            marginTop: '5px',
            color: '#718696',
            fontSize: '10px'
          }}>
            Live status of connected grid nodes
          </div>
        </div>

        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '9px'
        }}>
          <span style={{
            color: '#718696',
            fontSize: '9px'
          }}>
            FILTER
          </span>

          <select
            value={filter}
            onChange={(event) => onFilterChange(event.target.value)}
          >
            <option value="ALL">All Nodes</option>
            <option value="CHARGING">Charging</option>
            <option value="DISCHARGING">Discharging</option>
            <option value="IDLE">Idle</option>
            <option value="FAULT">Fault</option>
          </select>
        </div>
      </div>

      {filtered.length === 0 ? (
        <div style={{
          padding: '55px 20px',
          textAlign: 'center'
        }}>
          <div style={{
            width: '44px',
            height: '44px',
            margin: '0 auto 14px',
            display: 'grid',
            placeItems: 'center',
            border: '1px solid rgba(91,168,255,.16)',
            borderRadius: '12px',
            background: 'rgba(91,168,255,.05)',
            color: '#6faee8',
            fontSize: '18px'
          }}>
            ◉
          </div>

          <div style={{
            color: '#c9d7e1',
            fontSize: '12px',
            fontWeight: '700'
          }}>
            No nodes available
          </div>

          <div style={{
            marginTop: '6px',
            color: '#64798a',
            fontSize: '10px'
          }}>
            Start a simulation to populate the grid
          </div>
        </div>
      ) : (
        <div style={{ overflowX: 'auto' }}>
          <div
            className="node-list"
            style={{
              minWidth: '650px',
              padding: '10px 18px 18px'
            }}
          >
            <div
              className="node-row"
              style={{
                background: 'transparent',
                border: 'none',
                color: '#607686',
                fontSize: '8px',
                fontWeight: '800',
                letterSpacing: '.08em',
                textTransform: 'uppercase'
              }}
            >
              <span>Node</span>
              <span>Battery</span>
              <span>Solar</span>
              <span>Demand</span>
              <span>State</span>
              <span>Zone</span>
            </div>

            {filtered.slice(0, 12).map((node) => {
              const battery = Number(node.batteryPercentage ?? 0);

              return (
                <div
                  key={node.nodeId}
                  className="node-row"
                >
                  <span style={{
                    color: '#e0ebf2',
                    fontWeight: '700'
                  }}>
                    {node.nodeId}
                  </span>

                  <span style={{
                    color:
                      battery < 20
                        ? '#ff9a9f'
                        : battery < 50
                          ? '#ffd078'
                          : '#91e8b6',
                    fontWeight: '700'
                  }}>
                    {battery.toFixed(0)}%
                  </span>

                  <span>
                    {Number(node.solarPower ?? 0).toFixed(1)} kW
                  </span>

                  <span>
                    {Number(node.powerDemand ?? 0).toFixed(1)} kW
                  </span>

                  <span>
                    <span
                      className={`state-pill state-${node.state.toLowerCase()}`}
                    >
                      {node.state}
                    </span>
                  </span>

                  <span style={{
                    color: '#8da0ae'
                  }}>
                    {node.zone}
                  </span>
                </div>
              );
            })}
          </div>
        </div>
      )}

      {filtered.length > 12 && (
        <div style={{
          padding: '11px 18px',
          borderTop: '1px solid rgba(145,190,220,.07)',
          color: '#617687',
          fontSize: '9px'
        }}>
          Showing <strong style={{ color: '#a9bac7' }}>12</strong> of{' '}
          <strong style={{ color: '#a9bac7' }}>
            {filtered.length.toLocaleString()}
          </strong>{' '}
          nodes
        </div>
      )}
    </div>
  );
}