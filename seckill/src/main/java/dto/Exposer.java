package dto;

/**
 * @ClassName Exposer  <br>
 * @Description 暴露秒少地址DTO
 * dto包：数据传输层，和entity类似，用于存放表示数据的类型， 用来关注外部和servic之间的数据传递；
 * <p>
 * Exposer实体类：用来暴露秒杀接口；
 * 从业务分析或业务实体上来考虑这个问题的时候，
 * 发现Exposer实体类字段大部分都是业务相关的，只是用来方便service返回的数据封装；
 **/

public class Exposer {
    //  是否开启秒杀
    private boolean exposed;

    //  一种加密措施
    private String md5;

    //  id
    private long seckillId;

    //  系统当前时间（毫秒）
    private long now;

    //  开始时间
    private long start;

    //  结束时间
    private long end;

    /**
     * @MethodName
     * @Description Exposer实体类 做不同构造是为了方便做对象初始化
     * boolean exposed：是否暴露接口，告诉接口使用方这个秒杀是否开启；
     * md5：加密措施；
     **/
    public Exposer(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    /**
     * @MethodName
     * @Description Exposer实体类 做不同构造是为了方便做对象初始化
     * long now：系统当前时间，用户调用这个接口的时候，秒杀还没有开始，就不能告诉用户这个秒杀地址，但是还要返回系统时间，
     * 系统时间方便用户的浏览器去控制服务器的时间来定位到秒杀的计时工作
     **/
    public Exposer(boolean exposed, long seckillId, long now, long start, long end) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    /**
     * @MethodName
     * @Description Exposer实体类 做不同构造是为了方便做对象初始化
     **/
    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
