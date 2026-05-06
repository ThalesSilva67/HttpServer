package aplication.repository;

import aplication.model.User;

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
            User u = new User(id, name, email);

            map.put(u.getId(), u);
        }
    }

    public static List<User> findAll() {
        if (map.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(map.values());
    }

    public static User findById(int id) {
        if (map.isEmpty()) return null;

        return map.get(id);
    }

    public static User update(int id, String name, String email) {
        User oldUser = findById(id);
        if (oldUser == null) return null;

        Boolean emailFlag = existsEmailExcludingId(email, oldUser.getId());
        if (emailFlag) return null;

        User newUser = new User(oldUser.getId(), name, email);
        map.put(oldUser.getId(), newUser);

        return newUser;
    }

    public static User delete(int id) {
        if (map.isEmpty()) return null;

        return map.remove(id);
    }

    private static Boolean existsEmail(String email) {
        if (map.isEmpty()) return false;

        return map.values().stream().anyMatch(u -> u.getEmail().equals(email));
    }

    private static Boolean existsEmailExcludingId(String email, int id) {
        if (map.isEmpty()) return false;
        for (User user : map.values()) {
            if (user.getEmail().equals(email) && user.getId() != id) {
                return true;
            }
        }
        return false;
    }

}
