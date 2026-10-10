export default function StatisticsCards({ stats }) {
  const items = [
    { label: 'Total Nodes', value: stats.totalNodes ?? 0, accent: 'blue' },
    { label: 'Online Nodes', value: stats.onlineNodes ?? 0, accent: 'green' },
    { label: 'Charging', value: stats.charging ?? 0, accent: 'mint' },
    { label: 'Discharging', value: stats.discharging ?? 0, accent: 'amber' },
    { label: 'Faults', value: stats.faults ?? 0, accent: 'red' },
    { label: 'Total Solar', value: `${(stats.totalSolarGeneration ?? 0).toFixed(1)} kW`, accent: 'purple' },
    { label: 'Total Demand', value: `${(stats.totalDemand ?? 0).toFixed(1)} kW`, accent: 'cyan' }
  ];

  return (
    <div className="stats-grid">
      {items.map((item) => (
        <div key={item.label} className={`stat-card accent-${item.accent}`}>
          <div className="stat-label">{item.label}</div>
          <div className="stat-value">{item.value}</div>
        </div>
      ))}
    </div>
  );
}
