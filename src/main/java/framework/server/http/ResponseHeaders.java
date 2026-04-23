package framework.server.http;

import java.util.Set;

public class ResponseHeaders extends HttpHeaders {
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final Set<String> PROTECTED_HEADERS = Set.of(CONTENT_LENGTH.toLowerCase());

    @Override
    public void addHeader(String key, String value) {
        if (PROTECTED_HEADERS.contains(key.toLowerCase())) {
            throw new IllegalArgumentException("Header " + key + " is protected");
        }
        headers.put(key, value);
    }

    String serialize(int contentLength) {
        StringBuilder sb = new StringBuilder();

        headers.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\r\n"));
        sb.append(CONTENT_LENGTH).append(": ").append(contentLength).append("\r\n");

        return sb.toString();
    }
}
