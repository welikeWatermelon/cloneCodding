package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException(){}

    public NotEnoughStockException(String s) {
        super(s);
    }

    public NotEnoughStockException(String s, Throwable cause) {
        super(s, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}
