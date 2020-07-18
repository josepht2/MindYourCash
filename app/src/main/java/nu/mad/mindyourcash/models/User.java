package nu.mad.mindyourcash.models;

public class User {

    public String username;
    public String token;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
