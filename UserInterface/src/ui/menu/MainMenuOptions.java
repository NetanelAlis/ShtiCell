package ui.menu;

import java.util.Arrays;
import java.util.List;

public enum MainMenuOptions implements MenuItem{

    INITIALIZER{
        @Override
        protected void executeOption() {
        }
    },
    LOAD_XML_FILE{
        @Override
        protected void executeOption() {
        }
    },
    SHOW_SHEET{
        @Override
        protected void executeOption() {

        }
    },
    SHOW_SINGLE_CELL{
        @Override
        protected void executeOption() {

        }
    },
    UPDATE_SINGLE_CELL{
        @Override
        protected void executeOption() {

        }
    },
    SHOW_VERSION{
        @Override
        protected void executeOption() {

        }
    },
    EXIT{
        @Override
        protected void executeOption() {

        }
    };

    @Override
    public List<MenuItem> menuItems() {
        return Arrays.asList(values());
    }

    @Override
    public String getMenutemName() {
        return this.name().replace("_", " ").toLowerCase();
    }

}
