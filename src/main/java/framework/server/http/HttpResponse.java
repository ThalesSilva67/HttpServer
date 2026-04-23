package framework.server.http;

import java.nio.charset.StandardCharsets;

public class HttpResponse {
    private StatusCode status;
    private String body;
    private final ResponseHeaders httpHeaders = new ResponseHeaders();

    public HttpResponse(StatusCode status, String body) {
        this.status = status;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ResponseHeaders getHeaders() {
        return httpHeaders;
    }

    public StatusCode getStatus() {
        return status;
    }

    public void setStatus(StatusCode status) {
        this.status = status;
    }


    public String toHttpString() {
        String finalBody = (body == null) ? "" : body;
        if(status == null) throw new IllegalStateException("Status code cannot be null");

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(status.CODE).append(" ").append(status.MESSAGE).append("\r\n");

        byte[] bodyBytes = finalBody.getBytes(StandardCharsets.UTF_8);
        sb.append(httpHeaders.serialize(bodyBytes.length));
        sb.append("\r\n");
        sb.append(finalBody);

        return sb.toString();
    }

    public static HttpResponse ok(String body) {
        HttpResponse response = new HttpResponse(StatusCode.OK, body);
        response.httpHeaders.addHeader("Content-Type", "text/plain");
        return response;
    }

    public static HttpResponse notFound(String body) {
        HttpResponse response = new HttpResponse(StatusCode.NOT_FOUND, body);
        response.httpHeaders.addHeader("Content-Type", "text/plain");
        return response;
    }

    public static HttpResponse badRequest(String body) {
        HttpResponse response = new HttpResponse(StatusCode.BAD_REQUEST, body);
        response.httpHeaders.addHeader("Content-Type", "text/plain");
        return response;
    }
}
