package com.greensuresun.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: sunyuanyuan
 * @CreateDate: 2019/5/28
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: V0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface CsvField {
    /** JSON属性映射名称 **/
    public String name() default "";
}
