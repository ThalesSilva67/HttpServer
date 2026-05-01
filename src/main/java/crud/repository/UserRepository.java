package crud.repository;

import crud.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserRepository {
    private static final AtomicInteger idGenerator = new AtomicInteger();
    private static final Map<Integer, User> map = new ConcurrentHashMap<>();

    public static void save(String name, String email) {
        synchronized (UserRepository.class) {
            if (existsEmail(email)) throw new IllegalArgumentException("Email already exists");
            int id = idGenerator.incrementAndGet();
            User u = new User(String.valueOf(id), name, email);

            map.put(Integer.parseInt(u.getId()), u);
        }
    }

    public static List<String> findAll() {
        if (map.isEmpty()) return new ArrayList<>();
        StringBuilder sb = new StringBuilder("Users: \n");
        for (User user : map.values()) {
            sb.append(user).append("\n");
        }

        return List.of(sb.toString());
    }

    public static String findById(int id) {
        if (map.isEmpty()) return null;

        User user = map.get(id);
        if (user == null) return null;

        return user.toString();
    }

    private static Boolean existsEmail(String email) {
        if (map.isEmpty()) return false;

        return map.values().stream().anyMatch(u -> u.getEmail().equals(email));
    }

}
