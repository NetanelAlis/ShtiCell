package manager;

import user.User;

import java.util.*;

public class UserManager {
    private final Map<String, User> usersMap;
    
    public UserManager() {
        usersMap = new LinkedHashMap<>();
    }
    
    public synchronized void addUser(String userName, User user) {
        this.usersMap.put(userName, user);
    }
    
    public synchronized void removeUser(String userName) {
        this.usersMap.remove(userName);
    }
    
    public synchronized Map<String, User> getUsers() {
        return Collections.unmodifiableMap(this.usersMap);
    }
    
    public synchronized User getUser(String userName) { return this.usersMap.get(userName); }
    
    public boolean isUserExists(String userName) {
        return usersMap.containsKey(userName);
    }
    
}
