package serializer;

import crud.model.User;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonSerializerTest {
    @Test
    void shouldSerializeString() {
        String name = "Shin";
        String result = JsonSerializer.toJson(name);

        assertEquals("\"Shin\"", result);
    }

    @Test
    void shouldSerializeNumber() {
        int num = 17;
        String result = JsonSerializer.toJson(num);
        assertEquals("17", result);
    }

    @Test
    void shouldSerializeNull() {
        Object obj = null;
        String result = JsonSerializer.toJson(obj);
        assertEquals("null", result);
    }

    @Test
    void shouldSerializeMap() {
        Map<String, Object> map = Map.of("name", "Shin");
        String result = JsonSerializer.toJson(map);
        assertEquals("{\"name\": \"Shin\"}", result);
    }

    @Test
    void shouldSerializeList() {
        List<Object> list = List.of("Shin", 17);
        String result = JsonSerializer.toJson(list);
        assertEquals("[\"Shin\", 17]", result);
    }

    @Test
    void shouldSerializeUser() {
        User user = new User(1, "Shin", "nouzen@gmail.com");
        String expected = user.toJson();
        String result = JsonSerializer.toJson(user);
        assertEquals(expected, result);
    }

}