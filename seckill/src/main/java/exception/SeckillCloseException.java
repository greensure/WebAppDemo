package exception;

/**
 * @ClassName SeckillCloseException  <br>
 * @Description 秒杀关闭异常
 **/

public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

//    TODO Throwable作用？
    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
