package in.stockpe.stocksocket.truedata.http.exception;

public class TrueDataException extends Throwable {
    private static final long serialVersionUID = 1L;
    public String message;
    public String code;

    public TrueDataException(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public TrueDataException(String message) {
        this.message = message;
    }

    public String toString() {
        return "TrueDataException [message=" + this.message + ", code=" + this.code + "]";
    }
}
