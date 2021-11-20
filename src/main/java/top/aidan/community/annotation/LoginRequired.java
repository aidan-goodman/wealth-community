package top.aidan.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Aidan
 * @createTime 2021/11/20 10:42
 * @GitHub github.com/huaxin0304
 * @Blog aidanblog.top
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {

}
