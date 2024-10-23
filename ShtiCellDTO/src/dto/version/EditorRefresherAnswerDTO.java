package dto.version;

public class EditorRefresherAnswerDTO {
    private boolean userCantEditTheSheet;
    private boolean userNotOnLastSheetVersion;

    public EditorRefresherAnswerDTO(boolean userCantEditTheSheet, boolean userNotOnLastSheetVersion) {
        this.userCantEditTheSheet = userCantEditTheSheet;
        this.userNotOnLastSheetVersion = userNotOnLastSheetVersion;
    }

    public boolean isUserNotOnLastSheetVersion() {
        return userNotOnLastSheetVersion;
    }

     public boolean isUserCantEditTheSheet() {
        return userCantEditTheSheet;
    }

}
