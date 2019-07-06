-- 秒杀存储执行过程
DELIMITER $$ --console换行符；转为$$
-- 定义存储过程
-- in输入参数-参数传递进来之后用于存储过程的使用，
-- out输出参数-在存储过程中是不能使用的，但是可以给out参数赋值，赋值后在调用端可以拿到
--row_count()：返回上亿条修改类型sql(delete,insert,update)影响行数
--row_count:0未修改数据， >0表示修改的行数，<0代表sql错误或者未修改sql
CREATE PROCEDURE execute_seckill(IN v_seckill_id bigint, IN v_phone bigint, IN v_kill_time TIMESTAMP, OUT r_result int)
  BEGIN
    DECLARE insert_count int DEFAULT 0;
    start TRANSACTION;
    -- 插入用户购买明细
    INSERT ignore INTO success_killed(seckill_id, user_phone, create_time) VALUES (v_seckill_id, v_phone, v_kill_time);
    -- 返回上亿条修改类型sql(delete,insert,update)影响行数
    -- row_count:0未修改数据， >0表示修改的行数，<0代表sql错误或者未修改sql
    -- 将影响函数赋值给insert_count
    SELECT ROW_COUNT() INTO insert_count;
    IF(insert_count = 0) THEN
      ROLLBACK;
      -- 数据字典定义-1表示重复秒杀
      SET r_result = -1
    ELSEIF(insert_count < 0) THEN
      ROLLBACK;
       -- 数据字典定义-1表示系统异常、出错
      SET r_result = -2
    ELSE
    -- 执行更新库存的操作
      UPDATE seckill SET NUMBER = NUMBER - 1
      WHERE seckill_id = v_seckill_id AND end_time > v_kill_time AND start_time < v_kill_time AND NUMBER > 0;
      SELECT ROW_COUNT() INTO insert_count;
      --插入未成功，影响了0行数据
      IF(insert_count = 0) THEN
        ROLLBACK;
        SET r_result = 0;
      -- sql出错或等待行级锁超时了
      ELSEIF(insert_count < 0) THEN
        ROLLBACK;
        set r_result = -2;
      ELSE
        COMMIT;
        set r_result = 1;
      END if;
    END if;
  END;
$$
--存储过程定义结束

--验证存储过程是否有错
DELIMITER ;
SET @r_result = -3;
--执行存储过程
call execute_seckill(1002,13535458888,now(),@r_result);
--获取结果
SELECT @r_result;

--存储过程
--1.存储过程优化：让事务行级锁所持有的时间尽可能的短
--2.不要过度依赖存储过程
--3.简单的逻辑可以应用存储过程
--4.QPS:一个秒杀单6000/qps