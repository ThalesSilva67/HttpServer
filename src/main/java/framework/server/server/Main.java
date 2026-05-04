package framework.server.server;

import crud.model.User;
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
            List<User> users = UserRepository.findAll();
            if (users.isEmpty()) return HttpResponse.notFound("Not users found");
            StringBuilder sb = new StringBuilder();
            sb.append("[\n");
            for (int i = 0; i < users.size(); i++) {
                sb.append(users.get(i).toJson());
                if(i < users.size()-1) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            sb.append("]");

            return HttpResponse.ok(sb.toString());
        });

        router.addRoute("GET", "/users/{id}", req -> {
            try {
                int id = Integer.parseInt(req.getPathParams().get("id"));
                User user = UserRepository.findById(id);

                if (user == null) return HttpResponse.notFound("Not user found");

                return HttpResponse.ok(user.toJson());
            } catch (NumberFormatException e) {
                return HttpResponse.badRequest("Invalid user id");
            }
        });

        router.addRoute("PUT", "/users/{id}", req -> {
            try {
                int id = Integer.parseInt(req.getPathParams().get("id"));
                User oldUser = UserRepository.findById(id);
                if (oldUser == null) return HttpResponse.notFound("Not user found");

                Map<String, String> data = JsonParser.parse(req.getBody());
                String name = data.get("name");
                String email = data.get("email");

                if (name == null || name.isBlank()) return HttpResponse.badRequest("field name is required");
                if (email == null || email.isBlank()) return HttpResponse.badRequest("field email is required");

                User update = UserRepository.update(oldUser.getId(), name, email);

                if (update == null) return HttpResponse.badRequest("Email already in use");

                return HttpResponse.ok(update.toString());
            } catch (NumberFormatException e) {
                return HttpResponse.badRequest("Invalid user id");
            }
        });

        router.addRoute("DELETE", "/users/{id}", req -> {
            try {
                int id = Integer.parseInt(req.getPathParams().get("id"));
                User removed = UserRepository.delete(id);
                if (removed == null) return HttpResponse.notFound("Not user found");

                return HttpResponse.ok("User deleted");
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