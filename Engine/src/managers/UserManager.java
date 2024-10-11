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

    public synchronized Map<String, User> getUsers() {
        return Collections.unmodifiableMap(userMap);
    }

    public boolean isUserExists(String username) {
        return userMap.containsKey(username);
    }
}
