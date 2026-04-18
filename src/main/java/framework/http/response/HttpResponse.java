package framework.http.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private StatusCode status;
    private String body;
    private Map<String, String> headers = new HashMap<>();
}
