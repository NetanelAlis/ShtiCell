package logic.function.returnable;

import component.api.CellType;

public enum NoValue implements Returnable {

    UNDEFINED(){
        @Override
        public String getFunctionName() {
            return "!UNDEFINED!";
        }

        @Override
        public Returnable invoke() {
            return null;
        }

        @Override
        public CellType returnType() {
            return CellType.STRING;
        }

        @Override
        public Object getValue() {
            return null;
        }
    },

    NAN(){
        @Override
        public String getFunctionName() {
            return "NaN";
        }

        @Override
        public Returnable invoke() {
            return null;
        }

        @Override
        public CellType returnType() {
            return CellType.NOVALUE;
        }

        @Override
        public Object getValue() {
            return Double.NaN;
        }
    },
    ;

}
