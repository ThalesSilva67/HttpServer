package framework.server.http;

import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private final StatusCode status;
    private final String body;
    private final ResponseHeaders httpHeaders = new ResponseHeaders();

    public HttpResponse(StatusCode status, String body) {
        this.status = status;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public ResponseHeaders getHeaders() {
        return httpHeaders;
    }

    public String toHttpString() {
        String finalBody = (body == null) ? "" : body;
        if (status == null) throw new IllegalStateException("Status code cannot be null");

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(status.CODE).append(" ").append(status.MESSAGE).append("\r\n");

        byte[] bodyBytes = finalBody.getBytes(StandardCharsets.UTF_8);
        sb.append(getHeaders().serialize(bodyBytes.length));
        sb.append("\r\n");
        sb.append(finalBody);

        return sb.toString();
    }

    public static HttpResponse ok(String body) {
        return build(StatusCode.OK, body, "text/plain; charset=utf-8");
    }

    public static HttpResponse notFound(String body) {
        return build(StatusCode.NOT_FOUND, body, "text/plain; charset=utf-8");
    }

    public static HttpResponse badRequest(String body) {
        return build(StatusCode.BAD_REQUEST, body, "text/plain; charset=utf-8");
    }

    public static HttpResponse okJson(String body) {
        return build(StatusCode.OK, body, "application/json; charset=utf-8");
    }

    public static HttpResponse notFoundJson(String body) {
        return build(StatusCode.NOT_FOUND, body, "application/json; charset=utf-8");
    }

    public static HttpResponse badRequestJson(String body) {
        return build(StatusCode.BAD_REQUEST, body, "application/json; charset=utf-8");
    }

    private static HttpResponse build(StatusCode status, String body, String contentType) {
        HttpResponse response = new HttpResponse(status, body);
        response.getHeaders().addHeader("Content-Type", contentType);
        return response;
    }

}
