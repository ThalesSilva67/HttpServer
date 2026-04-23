package framework.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;;

public class HttpParser {
    private static final String SPACE = "\\s+";
    private static final int MAX_BODY_SIZE = 10 * 1024 * 1024;

    public static HttpRequest parseHttpRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
        RequestHeaders headers = new RequestHeaders();
        Map<String, String> queryParams = new HashMap<>();

        if (line == null || line.isEmpty()) throw new IllegalArgumentException("Empty request");

        String[] parts = line.split(SPACE);
        if (parts.length < 3) throw new IllegalArgumentException("Invalid request");

        String method = parts[0];
        String path = parts[1];
        String version = parts[2];

        String rawPath = path.contains("?") ? path.substring(0, path.indexOf("?")) : path;
        String query = path.contains("?") ? path.substring(path.indexOf("?") + 1) : null;

        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                if (pair.isEmpty()) continue;
                String[] keyValue = pair.split("=", 2);

                if (keyValue[0].isEmpty()) continue;

                if (keyValue.length < 2) {
                    queryParams.put(keyValue[0].trim(), "");
                } else {
                    queryParams.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }

        line = br.readLine();
        while (line != null && !line.isEmpty()) {
            int headerParts = line.indexOf(":");
            if (headerParts == -1) throw new IllegalArgumentException("Invalid header format");

            String key = line.substring(0, headerParts).trim();
            String value = line.substring(headerParts + 1).trim();

            if (key.isEmpty()) throw new IllegalArgumentException("Invalid header");
            headers.addHeader(key, value);

            line = br.readLine();
        }

        String body = null;
        if (headers.getHeaders().get("Content-Length") != null) {
            int len = parseAndValidateContent(headers.getHeaders().get("Content-Length").trim());
            if (len > 0) {
                char[] buffer = new char[len];
                int totalRead = 0;
                while (totalRead < len) {
                    int read = br.read(buffer, totalRead, len - totalRead);
                    if (read == -1) throw new IOException("Unexpected end of stream");
                    totalRead += read;

                }
                body = new String(buffer);
            } else if (len == 0) {
                body = "";
            }
        }

        return new HttpRequest(method, rawPath, version, body, headers, queryParams);
    }

    private static int parseAndValidateContent(String contentLength) {
        try {
            int len = Integer.parseInt(contentLength);
            if (len < 0) {
                throw new IllegalArgumentException("Content-Length cannot be negative");
            }
            if (len > MAX_BODY_SIZE) {
                throw new IllegalArgumentException("Content length too large");
            }
            return len;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid content length", e);
        }
    }
}
