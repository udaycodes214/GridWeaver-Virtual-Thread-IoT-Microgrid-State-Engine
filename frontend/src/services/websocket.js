import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export function connectWebSocket({
  onNodes,
  onStats,
  onEvents,
  onGrid,
  onStatus
}) {
  const client = new Client({
    webSocketFactory: () => new SockJS('http://localhost:8080/ws'),

    reconnectDelay: 4000,

    onConnect: () => {
      onStatus('connected');

      client.subscribe('/topic/nodes', (message) => {
        onNodes(JSON.parse(message.body));
      });

      client.subscribe('/topic/stats', (message) => {
        onStats(JSON.parse(message.body));
      });

      client.subscribe('/topic/events', (message) => {
        onEvents(JSON.parse(message.body));
      });

      client.subscribe('/topic/grid', (message) => {
        onGrid(JSON.parse(message.body));
      });
    },

    onDisconnect: () => {
      onStatus('offline');
    },

    onStompError: () => {
      onStatus('offline');
    }
  });

  client.activate();

  return client;
}