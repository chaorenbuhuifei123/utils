package mc.exception;

public class ExpectedException extends Exception {

    public ExpectedException() {
    }

    public ExpectedException(String code, String message) {
        super(message);
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
