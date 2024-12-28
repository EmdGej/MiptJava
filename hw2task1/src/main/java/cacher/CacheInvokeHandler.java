package cacher;

import annotations.Cache;
import annotations.Setter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public class CacheInvokeHandler<T> implements InvocationHandler {
  public CacheInvokeHandler(T object) {
    this.object = object;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args)
      throws Throwable {
    if (findAnnotationInHierarchy(method, Setter.class)) {
      methodsCache.clear();
      return method.invoke(object, args);
    }

    if (findAnnotationInHierarchy(method, Cache.class)) {
      String hashMethod = method.getName() + args;

      if (methodsCache.containsKey(hashMethod)) {
        return methodsCache.get(hashMethod);
      }

      Object methodOutput = method.invoke(object, args);
      methodsCache.put(hashMethod, methodOutput);

      return methodOutput;
    }

    return method.invoke(object, args);
  }

  private boolean findAnnotationInHierarchy(
      Method method, Class<? extends Annotation> annotationToFind)
      throws NoSuchMethodException {
    Class<?> curClass = object.getClass();

    if (curClass.getMethod(method.getName(), method.getParameterTypes())
            .isAnnotationPresent(annotationToFind)) {
      return true;
    }

    while (curClass != null) {
      for (Class<?> i : curClass.getInterfaces()) {
        try {
          Method iMethod =
              i.getMethod(method.getName(), method.getParameterTypes());
          if (iMethod.isAnnotationPresent(annotationToFind)) {
            return true;
          }
        } catch (NoSuchMethodException _) {
        }
      }

      curClass = curClass.getSuperclass();
    }

    return false;
  }

  private final T object;
  private final HashMap<String, Object> methodsCache =
      new HashMap<String, Object>();
}

