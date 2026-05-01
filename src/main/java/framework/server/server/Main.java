package framework.server.server;

import crud.repository.UserRepository;
import framework.server.http.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        ExecutorService executor = Executors.newFixedThreadPool(20);
        Router router = new Router();

        router.addRoute("POST", "/users", req -> {
            Map<String, String> data = JsonParser.parse(req.getBody());
            String name = data.get("name");
            String email = data.get("email");

            if (name == null || name.isBlank()) return HttpResponse.badRequest("field name is required");
            if (email == null || email.isBlank()) return HttpResponse.badRequest("field email is required");

            UserRepository.save(name, email);

            return HttpResponse.ok("User created");
        });

        router.addRoute("GET", "/users", req -> {
            List<String> list = UserRepository.findAll();

            if (list.isEmpty()) return HttpResponse.notFound("Not users found");

            return HttpResponse.ok(list.toString());
        });

        router.addRoute("GET", "/users/{id}", req -> {
            try {
                int id = Integer.parseInt(req.getPathParams().get("id"));
                String user = UserRepository.findById(id);

                if (user == null) return HttpResponse.notFound("Not user found");

                return HttpResponse.ok(user);
            } catch (NumberFormatException e) {
                return HttpResponse.badRequest("Invalid user id");
            }
        });

        executor.execute(() -> {
            while (true) {
                try (Socket accept = serverSocket.accept();
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
        });
    }

}