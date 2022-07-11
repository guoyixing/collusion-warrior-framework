package io.github.guoyixing.collusion.error;

/**
 * @author 敲代码的旺财
 * @date 2022/7/8 9:55
 */
public class EsSyncException extends RuntimeException {

    public EsSyncException() {
    }

    public EsSyncException(String message) {
        super(message);
    }

    public EsSyncException(String message, Throwable cause) {
        super(message, cause);
    }

    public EsSyncException(Throwable cause) {
        super(cause);
    }

    public EsSyncException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
