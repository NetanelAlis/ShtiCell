package component;

public class Sheet {
    private String sheetName;
    private String  xmlPath;
    private Layout  layout;
    private int sheetVersion;
//    private Map<String, Cell>;

    private class Layout {
        private int numberOfrows;
        private int numberOfcolumn;
        private int rowHeight;
        private int columnWidth;
    }
}
