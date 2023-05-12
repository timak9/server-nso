package com.example.demo;//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;

import java.net.SocketOption;
import java.util.Date;
import java.text.SimpleDateFormat;


import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.example.demo.All_workers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class MyHttpServer {

    public static void main(String[] args) throws IOException {

        int port = process.env.PORT;
        //int port = Integer.parseInt(System.getenv("PORT"));
        //int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        All_workers workers_data = new All_workers();
        System.out.println("Server started on port " + port);
        server.createContext("/enter", new EntryHandler(workers_data));
        server.createContext("/exit", new ExitHandler(workers_data));
        server.createContext("/info", new InfoHandler(workers_data));
        server.createContext("/error", new ErrorHandler());
        server.setExecutor(null);
        server.start();
    }

    static class EntryHandler implements HttpHandler {
        private All_workers workers_data;
        public EntryHandler(All_workers allWorkers){
            this.workers_data = allWorkers;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH:mm");
            String formattedDate = formatter.format(now);

            String requestMethod = exchange.getRequestMethod();
            if ("GET".equals(requestMethod)) {
                String idStr = exchange.getRequestURI().getQuery().replace("id=", "");
                int id = Integer.parseInt(idStr);
                Worker curr_worker = workers_data.FindWorker(id);
                int res = curr_worker.addEntryLog(formattedDate);
                if (res == 0) {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(500, -1);
                    OutputStream os = exchange.getResponseBody();
                    String response = "";
                    os.write(response.getBytes());
                    os.close();
                    return;
                }
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                String response = "{\n" +
                        "  \"status\": 200,\n" +
                        "  \"message\": \"OK\"\n" +
                        "}";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

    }

    static class ExitHandler implements HttpHandler {
        private All_workers workers_data;
        public ExitHandler(All_workers allWorkers){
            this.workers_data = allWorkers;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH:mm");
            String formattedDate = formatter.format(now);

            String requestMethod = exchange.getRequestMethod();
            if ("GET".equals(requestMethod)) {
                String idStr = exchange.getRequestURI().getQuery().replace("id=", "");
                int id = Integer.parseInt(idStr);
                Worker curr_worker = workers_data.FindWorker(id);
                int res = curr_worker.addExitLog(formattedDate);
                if (res == 0) {
                    exchange.sendResponseHeaders(500, -1);
                    OutputStream os = exchange.getResponseBody();
                    String response = "";
                    os.write(response.getBytes());
                    os.close();
                    return;
                }
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                String response = "{\n" +
                        "  \"status\": 200,\n" +
                        "  \"message\": \"OK\"\n" +
                        "}";
                exchange.sendResponseHeaders(200, response.getBytes().length);

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    static class InfoHandler implements HttpHandler {

        private All_workers workers_data;
        public InfoHandler(All_workers allWorkers){
            super();
            this.workers_data = allWorkers;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.contains("id=")) {
                // handle the request for a specific ID
                String idStr = exchange.getRequestURI().getQuery().replace("id=", "");
                int id = Integer.parseInt(idStr);
                Worker worker_id = workers_data.FindWorker(id);
                String message = worker_id.PrintWorklogs();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                String response = message;
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } else {
                String message = workers_data.Print_All_Worklogs();
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                String response = message;
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    static class ErrorHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "An error occurred. Please try again later.";
            exchange.sendResponseHeaders(500, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}



