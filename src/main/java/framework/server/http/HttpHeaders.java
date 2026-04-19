package framework.server.http;


import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;

public class HttpHeaders {
    private TreeMap<String,String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final Set<String> PROTECTED_HEADERS = Set.of(CONTENT_LENGTH.toLowerCase());

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public void addHeader(String key, String value) {
        if(!PROTECTED_HEADERS.contains(key.toLowerCase())) {
            headers.put(key, value);
        } else {
            throw new IllegalArgumentException("Header " + key + " is protected");
        }
    }

}
