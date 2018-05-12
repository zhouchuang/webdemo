package user.zc.aop;

import java.lang.annotation.*;

/**
 * 子对象级联映射
 *
 * @author zhouchuang
 * @create 2018-02-13 14:51
 */
@Retention(RetentionPolicy.RUNTIME)//注解会在class中存在，运行时可通过反射获取
@Target(ElementType.FIELD)//目标是方法
@Documented//文档生成时，该注解将被包含在javadoc中，可去掉
public @interface StorageChild {
    String service() default "";
    String refColumn() default  "";  //关联主对象的哪列
    String revColumn() default "";   //反向关联列名
}
