package aplication.model;

public class User {
    private final int id;
    private final String name;
    private final String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return this.id + " - " + this.name + " (" + this.email + ")";
    }

    public String toJson() {
        return "{" +
                "\n" +
                String.format("\"id\": %d", this.id) + ", " +
                "\n" +
                String.format("\"name\": \"%s\"", escape(this.name)) + ", " +
                "\n" +
                String.format("\"email\": \"%s\"", escape(this.email)) +
                "\n" +
                "}";
    }

    private String escape(String value) {
        return value.replace("\"", "\\\"");
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

}
