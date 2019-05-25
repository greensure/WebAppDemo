package exception;

/**
 * @ClassName SeckillException  <br>
 * @Description 业务通用异常类
 * RuntimeException 运行期异常，spring的声明事务会帮做回滚
 **/

public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
