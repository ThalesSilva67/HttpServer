package framework.server.http;

public class RequestHeaders extends HttpHeaders{

    @Override
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
}
