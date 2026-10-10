export default function EventLog({ events }) {
  const visibleEvents = (events || []).slice(0, 14);

  return (
    <div className="panel">
      <div className="panel-header">
        <div>
          <h3>Live Activity</h3>

          <div style={{
            marginTop: '5px',
            color: '#718696',
            fontSize: '10px'
          }}>
            Real-time system events
          </div>
        </div>

        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '6px',
          color: '#637888',
          fontSize: '9px'
        }}>
          <span style={{
            width: '6px',
            height: '6px',
            borderRadius: '50%',
            background: '#55e39a',
            boxShadow: '0 0 8px rgba(85,227,154,.6)'
          }} />

          LIVE
        </div>
      </div>

      {visibleEvents.length === 0 ? (
        <div style={{
          padding: '48px 20px',
          textAlign: 'center'
        }}>
          <div style={{
            width: '42px',
            height: '42px',
            margin: '0 auto 13px',
            display: 'grid',
            placeItems: 'center',
            border: '1px solid rgba(92,225,230,.15)',
            borderRadius: '50%',
            background: 'rgba(92,225,230,.04)',
            color: '#5ce1e6',
            fontSize: '17px'
          }}>
            ≋
          </div>

          <div style={{
            color: '#c5d5df',
            fontSize: '12px',
            fontWeight: '700'
          }}>
            Waiting for activity
          </div>

          <div style={{
            marginTop: '6px',
            color: '#627787',
            fontSize: '10px',
            lineHeight: '1.5'
          }}>
            System events will appear here
            <br />
            when the simulation starts.
          </div>
        </div>
      ) : (
        <div className="event-log">
          {visibleEvents.map((event, index) => (
            <div
              key={`${event}-${index}`}
              className="event-item"
            >
              <div style={{
                display: 'flex',
                alignItems: 'flex-start',
                gap: '9px'
              }}>
                <span style={{
                  flex: '0 0 6px',
                  width: '6px',
                  height: '6px',
                  marginTop: '5px',
                  borderRadius: '50%',
                  background:
                    index === 0
                      ? '#55e39a'
                      : '#5ce1e6',
                  boxShadow:
                    index === 0
                      ? '0 0 9px rgba(85,227,154,.6)'
                      : '0 0 8px rgba(92,225,230,.35)'
                }} />

                <span style={{
                  color: index === 0
                    ? '#d9e9f1'
                    : '#9eafbb'
                }}>
                  {event}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}