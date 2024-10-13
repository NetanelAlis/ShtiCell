package user;

import dto.ReceivedRequestDTO;
import user.permission.PermissionType;
import user.request.api.PermissionRequestInOwner;
import java.util.*;

public class User {
    private String userName;
    private Set<PermissionRequestInOwner> permissionsRequests;

    public User(String userName) {
        this.userName = userName;
        permissionsRequests = new LinkedHashSet<>();
    }

    public void createPermissionRequest(PermissionType requestedPermission, String sheetName, String requestedUserName) {;
        this.permissionsRequests.add(PermissionRequestInOwner.createPermissionRequestInOwner(requestedPermission, sheetName, requestedUserName));
    }

    public Set<ReceivedRequestDTO> getAllRequestsAsReceivedRequestDTO(){
        LinkedHashSet<ReceivedRequestDTO> receivedRequestDTOsToReturn = new LinkedHashSet<>();

        permissionsRequests.forEach(permissionRequest -> {
            receivedRequestDTOsToReturn.add(new ReceivedRequestDTO(permissionRequest.getRequestedPermissionType(),
                    permissionRequest.getRequesterUserName(), permissionRequest.getSheetName()));
        });

        return receivedRequestDTOsToReturn;
    }

    public String getUserName() {
        return userName;
    }

}
