package dto.version;

public class EditorRefresherAnswerDTO {
    private final boolean isInReaderMode;
    private final boolean shouldSendNotification;
    private final int latestVersion;
    
    public EditorRefresherAnswerDTO(boolean isInReaderMode, boolean shouldSendNotification, int latestVersion) {
        this.isInReaderMode = isInReaderMode;
        this.shouldSendNotification = shouldSendNotification;
        this.latestVersion = latestVersion;
    }
    
    public boolean isInReaderMode() { return this.isInReaderMode; }
    
    public boolean shouldSendNotification() { return this.shouldSendNotification; }
    
    public int getLatestVersion() { return this.latestVersion; }
}
