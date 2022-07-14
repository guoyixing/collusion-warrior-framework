package io.github.guoyixing.collusion.error;

/**
 * @author 敲代码的旺财
 * @date 2022/7/14 17:17
 */
public class EsCompositionException extends RuntimeException {

    public EsCompositionException() {
    }

    public EsCompositionException(String message) {
        super(message);
    }

    public EsCompositionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EsCompositionException(Throwable cause) {
        super(cause);
    }

    public EsCompositionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
