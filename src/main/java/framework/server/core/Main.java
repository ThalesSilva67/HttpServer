package framework.server.core;

import aplication.model.User;
import aplication.repository.UserRepository;
import framework.server.http.*;
import framework.server.routing.Router;
import framework.server.json.JsonParser;
import framework.server.json.JsonSerializer;

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

            if (name == null || name.isBlank()) return HttpResponse.badRequestJson(JsonSerializer.toJson("field name is required"));
            if (email == null || email.isBlank()) return HttpResponse.badRequestJson(JsonSerializer.toJson("field email is required"));

            UserRepository.save(name, email);

            return HttpResponse.okJson(JsonSerializer.toJson("User created"));
        });

        router.addRoute("GET", "/users", req -> {
            List<User> users = UserRepository.findAll();
            if (users.isEmpty()) return HttpResponse.notFoundJson(JsonSerializer.toJson("Not users found"));
            return HttpResponse.okJson(JsonSerializer.toJson(users));
        });

        router.addRoute("GET", "/users/{id}", req -> {
            try {
                int id = Integer.parseInt(req.getPathParams().get("id"));
                User user = UserRepository.findById(id);

                if (user == null) return HttpResponse.notFoundJson(JsonSerializer.toJson("Not user found"));

                return HttpResponse.okJson(JsonSerializer.toJson(user));
            } catch (NumberFormatException e) {
                return HttpResponse.badRequestJson(JsonSerializer.toJson("invalid user id"));
            }
        });

        router.addRoute("PUT", "/users/{id}", req -> {
            try {
                int id = Integer.parseInt(req.getPathParams().get("id"));
                User oldUser = UserRepository.findById(id);
                if (oldUser == null) return HttpResponse.notFoundJson(JsonSerializer.toJson(JsonSerializer.toJson("Not user found")));

                Map<String, String> data = JsonParser.parse(req.getBody());
                String name = data.get("name");
                String email = data.get("email");

                if (name == null || name.isBlank()) return HttpResponse.badRequestJson(JsonSerializer.toJson("field name is required"));
                if (email == null || email.isBlank()) return HttpResponse.badRequestJson(JsonSerializer.toJson("field email is required"));

                User update = UserRepository.update(oldUser.getId(), name, email);

                if (update == null) return HttpResponse.badRequestJson(JsonSerializer.toJson("Email already in use"));

                return HttpResponse.okJson(JsonSerializer.toJson(update));
            } catch (NumberFormatException e) {
                return HttpResponse.badRequestJson(JsonSerializer.toJson("invalid user id"));
            }
        });

        router.addRoute("DELETE", "/users/{id}", req -> {
            try {
                int id = Integer.parseInt(req.getPathParams().get("id"));
                User removed = UserRepository.delete(id);
                if (removed == null) return HttpResponse.notFoundJson(JsonSerializer.toJson("Not user found"));

                return HttpResponse.okJson(JsonSerializer.toJson("User deleted"));
            } catch (NumberFormatException e) {
                return HttpResponse.badRequestJson(JsonSerializer.toJson("invalid user id"));
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