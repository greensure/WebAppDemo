package dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import entity.Seckill;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import utils.JedisUtils;
import java.util.UUID;
import java.util.function.Function;

/**
 * @Description:
 * @Author: sunyuanyuan
 * @CreateDate: 2019/4/18
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
public class RedisDao {
    /**
     * JedisPool相当于数据库的连接池<--JedisPool类似于数据库连接池的ConnectionPool
     * Jedis相当于数据库的Connection
     */
    private final JedisPool jedisPool;

    /**
     * 初始化JedisPool
     */
    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    /**
     * 因为引入了protostuff依赖，可以直接用他的API，所以全局的定义一个RuntimeSchema;
     * 为什么叫RuntimeSchema呢？
     * 因为google的protobuffer需要我们自己写schema，组装成文件来告诉序列化，这样非常的不友好；
     * protostuff依赖动态的做了整个过程，性能没有什么损耗
     */
    // Seckill.class类的字节码，通过反射可以拿到字节码有哪些属性、方法
    // RuntimeSchema基于Seckill.class去做一个模式即schema，当在创建一个对象的时候，会根据schema来赋予相应的值，这是序列化的本质，
    // 即通过字节码、字节码对应的对象有哪些属性，会把字节码的数据传递给那些属性，这样就可以帮助我们序列化好这个对象
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    /**
     * 通过seckillId去拿到Redis缓存中的Seckill对象，不用去访问db，而是访问Redis
     * @param seckillId
     * @return
     */
    public Seckill getSeckill(long seckillId){
        return getSeckill(seckillId,null);
    }

    /**
     * Description:  从redis获取信息seckillId
     * get -> byte[] -> 反序列化 -> Object(Seckill)
     * @param seckillId id
     * @return 如果不存在，则返回null
     * @classname: RedisDao
     */
    private Seckill getSeckill(long seckillId, Jedis jedis) {
        boolean hasJedis = jedis != null;
        // redis操作逻辑
        try {
            if (!hasJedis){
                /**
                 * 类似数据库连接池，需要在finally中关闭
                 */
                jedis = jedisPool.getResource();
                 }
            try {
                String key = getSeckillRedisKey(seckillId);
                /**
                 * private Seckill getSeckill(long seckillId, Jedis jedis)方法返回的是一个对象，
                 * Redis，Jedis并没有实现内部序列化操作
                 *
                 * 可以通过protostuff依赖去写序列化，把一个对象转化成字节数组传到Redis中:
                 * get -> byte[] -> 反序列化 -> Object(Seckill)
                 *
                 * 得知这个对象的class，protostuff依赖内部会有schema去描述整个class是什么结构，但是整个class必须是pojo（即有get,set方法的标准的java对象），
                 * 而不能是String, Long这样的类型
                 */
                //采用自定义序列化-->把对象转换成二进制数据
                //jedis.get()是2套 api：一个是String, 一个是字节数组，由于我们存的是一个对象，所以此处肯定是一个字节数组
                //key.getBytes()传递一个字节数组
                byte[] bytes = jedis.get(key.getBytes());

                /**
                 * 若字节数组不空，则从缓存重获取到了，获取到之后要用protostuff去转换
                 * protostuff只需要做两件事情：
                 * 1、告诉protostuff这个对象的schema是什么，上面已经创建好一个全局的schema:
                 * private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
                 * 2、把这个对象的字节数据给protostuff,通过ProtostuffIOUtil工具类mergeFrom()方法把bytes字节数组存放数据，空对象seckill会按照schema把数据传到空对象seckill里
                 */
                /**
                 * 这样反序列化的好处：比原生的JDK序列化空间一般情况下可以压缩到原来的1/10至1/5，压缩速度是2个数量级的快，同时更节省CPU
                 */
                if (bytes != null){
                    //创建一个空对象：new了一个空对象，且任何属性都没有赋值-》由于已经有schema了，直接调用默认的构造方法就可以创建空对象
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    //上一行代码被调用运行后：seckill被反序列化
                    return seckill;
                }
            }finally {
                if (!hasJedis){
                    jedis.close();
                }
            }
        }catch (Exception e){

        }
        return null;
    }

    /**
     * Description: 从缓存获取，如果没有，则从数据库获取
     * 会用到分布式锁
     * set Object(Seckill) -> 序列化 -> byte[]
     * @param: seckillId     id
     * @param getDataFromDb 从数据库获取的方法
     * @return: 返回商品信息
     */
    public Seckill getOrPutSeckill(long seckillId, Function<Long, Seckill> getDataFromDb){
        Jedis jedis = jedisPool.getResource();
        //构造key
        String lockKey = "seckill:locks:getSeckill:" + seckillId;
        String lockRequestId = UUID.randomUUID().toString();

        try {
            //循环直到取到数据
            while (true){
                Seckill seckill = getSeckill(seckillId, jedis);
                if (seckill != null){
                    return seckill;
                }
                // 尝试获取锁。
                // 锁过期时间是防止程序突然崩溃来不及解锁，而造成其他线程不能获取锁的问题。过期时间是业务容忍最长时间。
                boolean getLock = JedisUtils.tryGetDistributedLock(jedis, lockKey, lockRequestId, 1000);
                if (getLock) {
                    // 获取到锁，从数据库拿数据, 然后存redis
                    seckill = getDataFromDb.apply(seckillId);
                    putSeckill(seckill, jedis);
                    return seckill;
                }
                //从获取不到锁，睡一下，等会再出发。sleep的时间需要斟酌，主要看业务处理速度
                try {
                    Thread.sleep(100);
                }catch (InterruptedException ignored){

                }
            }

        }catch (Exception ignored){

        }
        finally {
            //无论如何，最后要去解锁
            JedisUtils.releaseDistributedLock(jedis, lockKey, lockRequestId);
            jedis.close();
        }
        return null;
    }
    
    /**
     * Description: Redis命名规范化
     * @param: seckillId 商品id
     * @return: redis的key
     * @auther: sunyuanyuan
     * @date: 2019/4/18 
     * @classname: RedisDao
     */
    private String getSeckillRedisKey(long seckillId){
        //构造一个key， Redis命名：前缀 + 冒号（代表分隔符）+ id,因为是缓存seckill所以如下命名
        return "seckill: " + seckillId;
    }

    public String putSeckill(Seckill seckill){
        return putSeckill(seckill, null);
    }

    /**
     *  putSeckill  ---》set方法
     * @param seckill
     * @param jedis
     * @return
     */
    public String putSeckill(Seckill seckill, Jedis jedis) {
        boolean hasJedis = jedis != null;
        try {
            if (!hasJedis){
                jedis = jedisPool.getResource();
            }

          try {
              String key = getSeckillRedisKey(seckill.getSeckillId());
              //LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)缓存器大小设置为默认大小-》当前对象特别大的时候会有一个缓冲的过程
              byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
              //超时缓存，1小时 <- 一般的缓存不会永久缓存，缓存一段时间，时间到了就超时
              int timeout = 60 * 60;
              //setex()超时缓存
              //setex返回错误会告知错误信息，返回正确会告知正确信息
              String result = jedis.setex(key.getBytes(), timeout, bytes);
              return result;
          }finally {
                if (!hasJedis){
                    jedis.close();
                }
          }
        }catch (Exception e){

        }
        return null;
    }


}
