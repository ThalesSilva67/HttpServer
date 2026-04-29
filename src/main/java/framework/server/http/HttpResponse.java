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
        if(status == null) throw new IllegalStateException("Status code cannot be null");

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(status.CODE).append(" ").append(status.MESSAGE).append("\r\n");

        byte[] bodyBytes = finalBody.getBytes(StandardCharsets.UTF_8);
        sb.append(getHeaders().serialize(bodyBytes.length));
        sb.append("\r\n");
        sb.append(finalBody);

        return sb.toString();
    }

    public static HttpResponse ok(String body) {
        HttpResponse response = new HttpResponse(StatusCode.OK, body);
        response.getHeaders().addHeader("Content-Type", "text/plain; charset=utf-8");
        return response;
    }

    public static HttpResponse notFound(String body) {
        HttpResponse response = new HttpResponse(StatusCode.NOT_FOUND, body);
        response.getHeaders().addHeader("Content-Type", "text/plain; charset=utf-8");
        return response;
    }

    public static HttpResponse badRequest(String body) {
        HttpResponse response = new HttpResponse(StatusCode.BAD_REQUEST, body);
        response.getHeaders().addHeader("Content-Type", "text/plain; charset=utf-8");
        return response;
    }
}
