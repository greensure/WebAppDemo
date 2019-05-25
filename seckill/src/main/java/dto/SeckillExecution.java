package dto;

import entity.SuccessKilled;
import Enum.SeckillStatEnum;

/**
 * @ClassName SeckillExecution  <br>
 * @Description 封装一个数据传输对象: 封装秒杀执行后的结果
 **/

public class SeckillExecution {
    private long seckillId;

    //秒杀执行结果状态
    private int state;

    //状态表示
    private String stateInfo;

    //秒杀成功对象——当秒杀成功的时候，把秒杀对象返回
    private SuccessKilled successKilled;

    /**
     * 当执行秒杀异常时，需要通知接口使用方，它可能会输出什么样的异常，新建如下异常类型：
     * 当前端验证执行了2次提交，可能是无心为知，也可能是使用了第三方的工具（外挂）去拿到秒杀接口重复执行
     **/
    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }
}
