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
        Route bestRoute = null;
        Map<String, String> bestParams = null;
        int bestScore = -1;

        for (Route route : routes) {
            if (!route.getMethod().equalsIgnoreCase(request.getMethod())) continue;

            Map<String, String> params = route.match(request.getPath());

            if (params != null) {
                int score = route.getScore();
                if (score > bestScore) {
                    bestRoute = route;
                    bestParams = params;
                    bestScore = score;
                }
            }

        }

        if (bestRoute != null) {
            HttpRequest enriched = request.withPathParams(bestParams);
            return bestRoute.getHandler().apply(enriched);
        }

        return HttpResponse.notFound("Not Found");
    }
}
