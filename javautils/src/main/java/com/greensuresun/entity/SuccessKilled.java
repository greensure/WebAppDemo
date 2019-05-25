package com.greensuresun.entity;

import java.util.Date;

/**
 * @ClassName SuccessKilled  <br>
 * @Description entity业务数据的封装
 **/
public class SuccessKilled {

    private long seckillId;
    private long userPhone;
    private short state;
    private Date createTime;

    public SuccessKilled(long seckillId, long userPhone) {
        this.seckillId = seckillId;
        this.userPhone = userPhone;
    }

    public SuccessKilled(long userPhone) {
        this.userPhone = userPhone;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }
}
