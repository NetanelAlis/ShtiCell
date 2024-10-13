package managers;

import user.User;
import java.util.*;

public class UserManager {

    private Map<String, User> userMap;

    public UserManager() {
        this.userMap = new HashMap<>();
    }

    public synchronized void addUser(String username, User user) {
        userMap.put(username, user);
    }

    public synchronized void removeUser(String username) {
        userMap.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(userMap.keySet());
    }

    public boolean isUserExists(String username) {
        return userMap.containsKey(username);
    }

    public synchronized User getUser(String username) {
        return this.userMap.get(username);
    }
}
