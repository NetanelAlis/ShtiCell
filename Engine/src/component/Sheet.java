package component;

public interface Sheet {

    int getVersion();
    Cell getCell(int row, int col);
    void setCell(int row, int col, String value);
}
