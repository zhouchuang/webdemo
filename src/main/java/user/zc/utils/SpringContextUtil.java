package user.zc.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * SpingMVC工具类 可以获取实例
 *
 * @author zhouchuang
 * @create 2018-02-13 15:06
 */
public class SpringContextUtil implements ApplicationContextAware{
    private static ApplicationContext applicationContext; // Spring应用上下文环境
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }
}
