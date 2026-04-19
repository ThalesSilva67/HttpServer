package framework.server.http;

public class HttpRequest {
    private final String method;
    private final String path;
    private final String version;
    private final HttpHeaders headers;

    public HttpRequest(String method, String path, String version, HttpHeaders headers) {
        if(method == null) throw new IllegalArgumentException("Method cannot be null");
        if(path == null) throw new IllegalArgumentException("Path cannot be null");
        if(version == null) throw new IllegalArgumentException("Version cannot be null");
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;

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

    public HttpHeaders getHeaders() {
        return headers;
    }
}
