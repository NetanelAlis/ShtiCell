package user;

import dto.permission.ReceivedPermissionRequestDTO;
import user.request.PermissionRequest;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class User {
    private final String userName;
    private final Map<String, List<PermissionRequest>> permissionRequests;
    private final ReadWriteLock permissionRequestsLock;
    
    public User(String userName) {
        this.userName = userName;
        this.permissionRequests = new LinkedHashMap<>();
        this.permissionRequestsLock = new ReentrantReadWriteLock();
    }
    
    public String getUserName() { return this.userName; }
    
    public List<ReceivedPermissionRequestDTO> getPermissionRequests() {
        List<ReceivedPermissionRequestDTO> receivedPermissionRequests = new LinkedList<>();
        
        this.permissionRequestsLock.readLock().lock();
        try {
            this.permissionRequests.forEach((sheetName, requests) ->
                    requests.forEach((request) ->
                            receivedPermissionRequests.add(new ReceivedPermissionRequestDTO(
                                    request.getSenderName(),
                                    sheetName,
                                    request.getRequestedPermission().getPermission(),
                                    request.getRequestID()
                            ))
                    )
            );
        } finally {
            this.permissionRequestsLock.readLock().unlock();
        }
        
        return receivedPermissionRequests;
    }
    
    public void createPermissionRequest(PermissionRequest requestedPermission, String sheetName, String sender) {
        this.permissionRequestsLock.writeLock().lock();
        try {
            this.permissionRequests.putIfAbsent(sheetName, new LinkedList<>());
            List<PermissionRequest> requestsList = this.permissionRequests.get(sheetName);
            
            requestsList.add(requestedPermission);
        } finally {
            this.permissionRequestsLock.writeLock().unlock();
        }
    }
    
    public void removePermissionRequest(String sheetName, int requestID) {
        this.permissionRequestsLock.writeLock().lock();
        try {
            List<PermissionRequest> requestList = this.permissionRequests.get(sheetName);
            
            if (requestList != null) {
                requestList.removeIf(request -> request.getRequestID() == requestID);
            }
        } finally {
            this.permissionRequestsLock.writeLock().unlock();
        }
    }
}
