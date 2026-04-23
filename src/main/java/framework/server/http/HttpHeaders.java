package framework.server.http;


import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;

public abstract class HttpHeaders {
    protected final TreeMap<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public abstract void addHeader(String key, String value);
}
