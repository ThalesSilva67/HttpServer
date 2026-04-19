package framework.server.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Router {
    private Map<String, Function<HttpRequest, HttpResponse>> routes = new ConcurrentHashMap<>();

    public void addRoute(String method, String path, Function<HttpRequest, HttpResponse> handler) {
        routes.put(method.toUpperCase() + " " + path, handler);
    }

    public HttpResponse handle(HttpRequest request) {
        String key = request.getMethod().toUpperCase() + " " + request.getPath();
        Function<HttpRequest, HttpResponse> handler = routes.get(key);
        if(handler != null) {
            return handler.apply(request);
        }
        return HttpResponse.notFound("Not Found");
    }
}
