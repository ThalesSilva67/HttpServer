package framework.http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        ExecutorService executor = Executors.newFixedThreadPool(20);
        Map<String, Function<String, String>> routes = new HashMap<>();
        routes.put("/", req -> "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: 11\r\n" +
                "\r\nhello world");
        routes.put("/sobre", req -> "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: 9\r\n" +
                "\r\nsobre nos");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            executor.submit(() -> {
                try (Socket accept = clientSocket;
                     InputStreamReader isr = new InputStreamReader(accept.getInputStream(), StandardCharsets.UTF_8);
                     BufferedReader br = new BufferedReader(isr);
                     OutputStream os = accept.getOutputStream()) {

                    System.out.printf("Cliente conectado:  %s:%d\n", accept.getInetAddress().getHostAddress(), accept.getPort());
                    String line = br.readLine();
                    String[] parts;
                    String path = "";

                    if (line != null) {
                        parts = line.split(" ");
                        if (parts.length >= 3) {
                            System.out.printf("Método: %s\n", parts[0]);
                            System.out.printf("Caminho: %s\n", parts[1]);
                            System.out.printf("Versão: %s\n", parts[2]);
                        }
                        if (parts.length >= 2) {
                            path = parts[1];
                        }

                    } else {
                        Function<String, String> handler = routes.getOrDefault(path, req -> "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Type: text/plain\r\n" +
                                "Content-Length: 9\r\n" +
                                "\r\n" +
                                "Not Found");
                        os.write(handler.apply(path).getBytes(StandardCharsets.UTF_8));
                        os.flush();

                    }

                    while (true) {
                        if (line == null) break;
                        if (line.isEmpty()) break;
                        System.out.println(line);
                        line = br.readLine();
                    }

                    if (path == null || path.isEmpty()) {
                        Function<String, String> handler = routes.getOrDefault(path, req -> "HTTP/1.1 400 Bad Request\r\n" +
                                "Content-Type: text/plain\r\n" +
                                "Content-Length: 11\r\n" +
                                "\r\n" +
                                "Bad Request");
                        os.write(handler.apply(path).getBytes(StandardCharsets.UTF_8));
                        os.flush();
                    } else {
                        os.write(routes.get(path).apply(line).getBytes(StandardCharsets.UTF_8));
                        os.flush();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}