package framework.server.http;

import java.io.BufferedReader;
import java.io.IOException;;

public class HttpParser {
    private static final String SPACE = "\\s+";

    public static HttpRequest parseHttpRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
        HttpHeaders headers = new HttpHeaders();

        if (line == null || line.isEmpty()) throw new IllegalArgumentException("Empty request");

        String[] parts = line.split(SPACE);
        if(parts.length < 3) throw new IllegalArgumentException("Invalid request");

        String method = parts[0];
        String path = parts[1];
        String version = parts[2];

        line = br.readLine();
        while (line != null && !line.isEmpty()) {
            int headerParts = line.indexOf(":");
            if(headerParts == -1) throw new IllegalArgumentException("Invalid header format");

            String key = line.substring(0, headerParts).trim();
            String value = line.substring(headerParts + 1).trim();

            if(key.isEmpty()) throw new IllegalArgumentException("Invalid header");
            headers.addHeader(key, value);
            line = br.readLine();
        }

        return new HttpRequest(method, path, version, headers);
    }
}
