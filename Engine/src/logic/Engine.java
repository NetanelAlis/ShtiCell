package logic;

public interface Engine {

    boolean loadXmlFile(String filePath);
    void showSheet();
    void showCellData(String cellId);
    void updateCellData(String cellId);
}
