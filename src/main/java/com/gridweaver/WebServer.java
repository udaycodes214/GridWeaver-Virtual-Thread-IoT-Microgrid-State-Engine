package com.gridweaver;

import com.gridweaver.engine.MicrogridSnapshot;
import com.gridweaver.engine.MicrogridStateEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WebServer {

    private static final int PORT = 8080;

    public static void start(MicrogridStateEngine engine) throws IOException {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(PORT),
                0
        );

        // =========================
        // GET /api/grid
        // =========================
        server.createContext("/api/grid", exchange -> {

            addCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 204, "");
                return;
            }

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            MicrogridSnapshot snapshot = engine.snapshot();

            String json = """
                    {
                      "timestamp": %d,
                      "generation": %.2f,
                      "load": %.2f,
                      "battery": %.2f,
                      "gridImport": %.2f,
                      "balance": %.2f,
                      "status": "%s",
                      "devices": %d
                    }
                    """.formatted(
                    snapshot.timestamp(),
                    snapshot.generationKw(),
                    snapshot.loadKw(),
                    snapshot.batteryPowerKw(),
                    snapshot.gridImportKw(),
                    snapshot.balanceKw(),
                    snapshot.status(),
                    snapshot.onlineDevices()
            );

            send(exchange, 200, json);
        });

        // =========================
        // GET /api/health
        // =========================
        server.createContext("/api/health", exchange -> {

            addCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 204, "");
                return;
            }

            send(
                    exchange,
                    200,
                    "{\"status\":\"UP\",\"service\":\"GridWeaver\"}"
            );
        });

        // =========================
        // POST /api/sensor
        // =========================
        server.createContext("/api/sensor", exchange -> {

            addCorsHeaders(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 204, "");
                return;
            }

            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                send(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            String body = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            String deviceId = extractString(body, "deviceId");
            Double powerKw = extractNumber(body, "powerKw");

            if (deviceId == null || powerKw == null) {
                send(
                        exchange,
                        400,
                        "{\"error\":\"deviceId and powerKw are required\"}"
                );
                return;
            }

            engine.submitSensorUpdate(deviceId, powerKw);

            String response = """
                    {
                      "success": true,
                      "deviceId": "%s",
                      "powerKw": %.2f
                    }
                    """.formatted(deviceId, powerKw);

            send(exchange, 200, response);
        });

        server.setExecutor(null);
        server.start();

        System.out.println(
                "GridWeaver API started on http://localhost:" + PORT
        );

        System.out.println(
                "Grid API: http://localhost:" + PORT + "/api/grid"
        );

        System.out.println(
                "Sensor API: http://localhost:" + PORT + "/api/sensor"
        );
    }

    private static void addCorsHeaders(HttpExchange exchange) {

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin",
                "*"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, POST, OPTIONS"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Headers",
                "Content-Type"
        );
    }

    private static String extractString(String json, String key) {

        Pattern pattern = Pattern.compile(
                "\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]+)\""
        );

        Matcher matcher = pattern.matcher(json);

        return matcher.find() ? matcher.group(1) : null;
    }

    private static Double extractNumber(String json, String key) {

        Pattern pattern = Pattern.compile(
                "\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)"
        );

        Matcher matcher = pattern.matcher(json);

        return matcher.find()
                ? Double.parseDouble(matcher.group(1))
                : null;
    }

    private static void send(
            HttpExchange exchange,
            int status,
            String response
    ) throws IOException {

        byte[] data =
                response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json; charset=UTF-8"
        );

        exchange.sendResponseHeaders(
                status,
                data.length
        );

        try (OutputStream output =
                     exchange.getResponseBody()) {

            output.write(data);
        }
    }
}