package serializer;

import crud.model.User;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class JsonSerializer {

    public static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj instanceof String s) {
            return String.format("\"%s\"", escape(s));
        } else if (obj instanceof Number n) {
            return n.toString();
        } else if (obj instanceof Map<?, ?> map) {
            StringBuilder sb = new StringBuilder("{");
            StringJoiner joiner = new StringJoiner(", ");
             for(Map.Entry<?, ?> entry : map.entrySet()) {
                 String key = escape(entry.getKey().toString());
                 String value = toJson(entry.getValue());
                 joiner.add("\"" + key + "\": " + value);
             }
             sb.append(joiner);
             sb.append("}");
             return sb.toString();
        } else if (obj instanceof List<?> list) {
            StringJoiner joiner = new StringJoiner(", ");
            for(int i = 0; i < list.size(); i++) {
                String value = toJson(list.get(i));
                joiner.add(value);
            }

            return "[" + joiner.toString() + "]";
        } else if (obj instanceof User u) {
            return u.toJson();
        }

        throw new IllegalArgumentException("Unsupported type: " + obj.getClass());
    }

    private static String escape(String value) {
        return value.replace("\"", "\\\"");
    }
}
