package framework.server.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Route {
    private final String method;
    private final String pathPattern;
    private final int score;
    private final List<String> parts;
    private final Function<HttpRequest, HttpResponse> handler;

    public Route(String method, String pathPattern, Function<HttpRequest, HttpResponse> handler) {
        this.method = method;
        this.pathPattern = pathPattern;
        this.handler = handler;
        this.parts = splitPath(pathPattern);
        this.score = calculateScore(parts);
    }

    public Map<String, String> match(String path) {
        List<String> requestParts = splitPath(path);

        if(parts.size() != requestParts.size()) return null;

        Map<String, String> params = new HashMap<>();
        for(int i = 0; i < parts.size(); i++) {
            String routePart = parts.get(i);
            String requestPart = requestParts.get(i);

            if(routePart.startsWith("{") && routePart.endsWith("}")) {
                String key = routePart.substring(1, routePart.length() - 1);
                params.put(key, requestPart);
            } else {
                if(!routePart.equals(requestPart)) return null;
            }
        }

        return params;
    }

    private int calculateScore(List<String> parts) {
        int score = 0;
        for(String part : parts) {
            if(part.startsWith("{") && part.endsWith("}")) {
                score += 1;
            } else {
                score += 2;
            }
        }

        return score;
    }

    private static List<String> splitPath(String path) {
        return Arrays.stream(path.split("/+")).filter(p -> !p.isEmpty()).toList();
    }

    public String getMethod() {
        return method;
    }

    public String getPathPattern() {
        return pathPattern;
    }

    public Function<HttpRequest, HttpResponse> getHandler() {
        return handler;
    }

    public int getScore() {
        return score;
    }

    public List<String> getParts() {
        return parts;
    }
}
