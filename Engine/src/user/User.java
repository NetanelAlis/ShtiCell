package user;

import dto.permission.ReceivedRequestForTableDTO;
import user.permission.request.PermissionRequest;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class User {
    private String userName;
    private Map<String,List<PermissionRequest>> permissionsRequests;
    private ReadWriteLock permissionsRequestsReadWriteLock;

    public User(String userName) {
        this.userName = userName;
        permissionsRequests = new LinkedHashMap<>();
        this.permissionsRequestsReadWriteLock = new ReentrantReadWriteLock();
    }

    public void createPermissionRequest(PermissionRequest requestedPermission, String sheetName) {
        this.permissionsRequestsReadWriteLock.writeLock().lock();
        try {
        List<PermissionRequest> requests = permissionsRequests.get(sheetName);

        if(requests == null) {
            requests = new ArrayList<>();
        }

        requests.add(requestedPermission);
        this.permissionsRequests.put(sheetName, requests);

    } finally {
            this.permissionsRequestsReadWriteLock.writeLock().unlock();
        }
    }

    public List<ReceivedRequestForTableDTO> getAllRequestsAsReceivedRequestDTO(){
        List<ReceivedRequestForTableDTO> receivedRequestForTableDTOsToReturn = new LinkedList<>();

        this.permissionsRequestsReadWriteLock.readLock().lock();
        try {
            permissionsRequests.forEach((sheetName, requests) -> {
                requests.forEach((permissionRequest) -> {
                    receivedRequestForTableDTOsToReturn.add(new ReceivedRequestForTableDTO(permissionRequest.getRequestedPermissionType().getType(),
                            permissionRequest.getSenderName(), sheetName, permissionRequest.getRequestNumber()));
                });
            });
        } finally {
            this.permissionsRequestsReadWriteLock.readLock().unlock();
        }

        return receivedRequestForTableDTOsToReturn;
    }

    public String getUserName() {
        return userName;
    }

    public void removeRequestForSheetPermission(String sheetName, int requestNumber) {
        this.permissionsRequestsReadWriteLock.writeLock().lock();
        try {
        List<PermissionRequest> requests = this.permissionsRequests.get(sheetName);

        if(requests != null) {
            requests.removeIf(request -> request.getRequestNumber() == requestNumber);
        }

    } finally {
            this.permissionsRequestsReadWriteLock.writeLock().unlock();
        }
    }
}
