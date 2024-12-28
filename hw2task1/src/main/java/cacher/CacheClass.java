package cacher;

import java.lang.reflect.Proxy;

public class CacheClass {
  public static <T> T cache(T object) {
    return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(),
        object.getClass().getInterfaces(), new CacheInvokeHandler<T>(object));
  }
}

