package framework.server.server;

import framework.server.http.HttpParser;
import framework.server.http.HttpRequest;
import framework.server.http.HttpResponse;
import framework.server.http.Router;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        ExecutorService executor = Executors.newFixedThreadPool(20);
        Router router = new Router();
        router.addRoute("GET","/", req -> HttpResponse.ok("hello world"));
        router.addRoute("GET","/users", req -> {
            String query = req.getQueryParams().get("id");
            return HttpResponse.ok("resultado");
        });
        router.addRoute("GET","/user", req -> {
            String id = req.getQueryParams().get("id");
            if(id == null || id.isEmpty()) {
                return HttpResponse.badRequest("id is required");
            }
            return HttpResponse.ok("User ID: " + id);
        });

        while (true) {
            Socket clientSocket = serverSocket.accept();

                try (Socket accept = clientSocket;
                     InputStreamReader isr = new InputStreamReader(accept.getInputStream(), StandardCharsets.UTF_8);
                     BufferedReader br = new BufferedReader(isr);
                     OutputStream os = accept.getOutputStream()) {

                    HttpRequest request = HttpParser.parseHttpRequest(br);
                    HttpResponse response = router.handle(request);

                    byte[] bytes = response.toHttpString().getBytes();
                    os.write(bytes);
                    os.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

}