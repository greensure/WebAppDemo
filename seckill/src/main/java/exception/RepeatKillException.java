package exception;

/**
 * @ClassName RepeatKillException  <br>
 * @Description 重复秒杀异常（运行期异常）
 * <p>
 * 当执行秒杀异常时，需要通知接口使用方，它可能会输出什么样的异常，新建如下异常类型：
 * 当前端验证执行了2次提交，可能是无心为知，也可能是使用了第三方的工具（外挂）去拿到秒杀接口重复执行
 **/

public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
