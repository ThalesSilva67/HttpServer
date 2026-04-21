package framework.server.http;

import java.util.Map;

public class RouteCandidate {
    private final Route route;
    private final Map<String, String> params;

    public RouteCandidate(Route route, Map<String, String> params) {
        this.route = route;
        this.params = params;
    }

    public Route getRoute() {
        return route;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
