package logic.function.returnable;

public class String implements Returnable {

    private java.lang.String value;

    public String(java.lang.String str){
        this.value = str;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public Returnable invoke() {
        return this;
    }

    @Override
    public java.lang.String getFunctionName() {
        return "";
    }

}
