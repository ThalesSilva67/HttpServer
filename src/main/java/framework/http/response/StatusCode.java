package framework.http.response;

public enum StatusCode {
    OK(200, "OK"),
    NOT_FOUND(404, "NOT FOUND"),
    BAD_REQUEST(400, "BAD REQUEST");

    public final int CODE;
    public final String MESSAGE;
    StatusCode(int i, String s) {
        this.CODE = i;
        this.MESSAGE = s;
    }
}
