package dto;

/**
 * @ClassName SeckillResult  <br>
 * @Description 所有AJAX请求返回类型， 封装json结果
 * dto用来封装所有的传递的数据，主要职责是web层到service层之间的数据传递，需新建一个SeckilledResult泛型类，需要这样的VO用来封装数据结果， 即封装json结果;
 * @Author sunyuanyuan
 * @Date 2018/9/7
 * @Version V1.0
 **/
public class SeckillResult<T> {
    private boolean success;

    private T data; //TODO 了解T的作用？

    private String error;

    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
