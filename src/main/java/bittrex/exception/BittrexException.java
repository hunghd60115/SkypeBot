package bittrex.exception;

/**
 * Created by Onsiter on 2017/12/04.
 */
    public class BittrexException extends Exception{

    public int code;

    public BittrexException() {
        super();
        this.code = 0;
    }

    public BittrexException(int code) {
        this.code = code;
    }

    public BittrexException(String message) {
        super(message);
    }
}
