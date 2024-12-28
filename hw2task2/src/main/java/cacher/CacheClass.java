package cacher;

import net.sf.cglib.proxy.Enhancer;

public class CacheClass {
    public static <T> T cache(T object) {
        return (T) Enhancer.create(object.getClass(), new CacheInvokeHandler<T>(object));
    }
}

