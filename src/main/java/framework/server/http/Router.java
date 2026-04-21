package framework.server.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Router {
    private List<Route> routes = new ArrayList<>();

    public void addRoute(String method, String path, Function<HttpRequest, HttpResponse> handler) {
        routes.add(new Route(method, path, handler));
    }

    public HttpResponse handle(HttpRequest request) {
        List<RouteCandidate> candidates = new ArrayList<>();
        int bestScore = -1;

        for (Route route : routes) {
            if (!route.getMethod().equalsIgnoreCase(request.getMethod())) continue;

            Map<String, String> params = route.match(request.getPath());

            if (params != null) {
                int score = route.getScore();
                if (score > bestScore) {
                    bestScore = score;
                    candidates.clear();
                    candidates.add(new RouteCandidate(route, params));
                } else if (score == bestScore) {
                    candidates.add(new RouteCandidate(route, params));
                }
            }

        }

        if (candidates.isEmpty()) {
            return HttpResponse.notFound("Not Found");
        }
        RouteCandidate candidate = candidates.getFirst();
        HttpRequest enriched = request.withPathParams(candidate.getParams());
        return candidate.getRoute().getHandler().apply(enriched);
    }
}
