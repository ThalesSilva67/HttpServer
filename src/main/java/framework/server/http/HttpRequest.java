package framework.server.http;

import java.util.Collections;
import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final String version;
    private final String body;
    private final RequestHeaders headers;
    private  final Map<String, String> queryParams;
    private  final Map<String, String> pathParams;

    private HttpRequest(String method, String path, String version, String body, RequestHeaders headers, Map<String, String> queryParams, Map<String, String> pathParams) {
        if(method == null) throw new IllegalArgumentException("Method cannot be null");
        if(path == null) throw new IllegalArgumentException("Path cannot be null");
        if(version == null) throw new IllegalArgumentException("Version cannot be null");
        this.method = method;
        this.path = path;
        this.version = version;
        this.body = body;
        this.headers = headers;
        this.queryParams = Collections.unmodifiableMap(queryParams);
        this.pathParams = Collections.unmodifiableMap(pathParams);

    }

    public HttpRequest(String method, String path, String version,
                       RequestHeaders headers, Map<String, String> queryParams) {
        this(method, path, version, null, headers, queryParams, Map.of());
    }

    public HttpRequest(String method, String path, String version, String body,
                       RequestHeaders headers, Map<String, String> queryParams) {
        this(method, path, version, body, headers, queryParams, Map.of());
    }


    public HttpRequest withPathParams(Map<String, String> params) {
        return new HttpRequest(this.method, this.path, this.version, this.body, this.headers, this.queryParams, params);
    }

    public String getMethod() {
        return method;
    }

    public String getVersion() {
        return version;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Map<String, String> getPathParams() {
        return pathParams;
    }

    public RequestHeaders getHeaders() {
        return headers;
    }
}
