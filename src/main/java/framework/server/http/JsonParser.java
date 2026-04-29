package framework.server.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JsonParser {
    public static Map<String, String> parse(String body) {
        if (body == null || body.isEmpty()) return Collections.emptyMap();

        if (!body.startsWith("{") || !body.endsWith("}")) return Collections.emptyMap();

        Map<String, String> jsonMap = new HashMap<>();
        String json = body.substring(1, body.length() - 1).trim();
        String[] pairs = json.split(",");

        for (String pair : pairs) {
            pair = pair.trim();
            if (pair.isEmpty()) continue;

            int idx = pair.indexOf(":");
            if (idx == -1) continue;

            String key = removeQuotes(pair.substring(0, idx));
            String value = removeQuotes(pair.substring(idx + 1));
            jsonMap.put(key, value);
        }


        return jsonMap;
    }

    private static String removeQuotes(String str) {
        str = str.trim();
        if (str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }
}