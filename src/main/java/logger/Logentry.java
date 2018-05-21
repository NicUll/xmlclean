package logger;


public class Logentry {

    private LOGTYPE type;
    private String value;
    private int ID;

    public Logentry(int ID, LOGTYPE type, String value) {
        this.type = type;
        this.value = value;
        this.ID = ID;
    }

    public LOGTYPE getType() {
        return type;
    }

    public void setType(LOGTYPE type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
