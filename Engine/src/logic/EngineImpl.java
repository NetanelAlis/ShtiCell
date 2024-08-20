package logic;

import component.api.Sheet;
import component.impl.SheetImpl;

public class EngineImpl implements Engine {

    Sheet sheet = new SheetImpl();

    @Override
    public boolean loadXmlFile(String filePath) {
        return true;
    }

    @Override
    public void showSheet() {

    }

    @Override
    public void showCellData(String cellId) {

    }

    @Override
    public void updateCellData(String cellId) {

    }

}
