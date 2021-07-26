package cn.com.koriesh.memo.common;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil {

    public static final SpringContextUtil instance = new SpringContextUtil();

    private ApplicationContext _context;

    private SpringContextUtil() {

    }

    public void init(ApplicationContext context) {
        _context = context;
    }

    public ApplicationContext getContext() {
        return _context;
    }

    public <T> T getBean(Class<T> requiredType) {
        return _context.getBean(requiredType);
    }

}
