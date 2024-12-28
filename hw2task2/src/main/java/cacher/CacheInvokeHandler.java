package cacher;

import annotations.Cache;
import annotations.Setter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CacheInvokeHandler<T> implements MethodInterceptor {
  public CacheInvokeHandler(T object) {
    this.object = object;
  }

  @Override
  public Object intercept(Object o, Method method, Object[] args,
      MethodProxy methodProxy) throws Throwable {
    Class<?> methodClass = findFirstClassWithMethod(method);

    if (findAnnotationInHierarchy(methodClass, method, Setter.class)) {
      methodsCache.clear();
      return method.invoke(object, args);
    }

    if (findAnnotationInHierarchy(methodClass, method, Cache.class)) {
      String hashMethod = method.getName() + Arrays.toString(args);

      if (methodsCache.containsKey(hashMethod)) {
        return methodsCache.get(hashMethod);
      }

      Object methodOutput = method.invoke(object, args);
      methodsCache.put(hashMethod, methodOutput);

      return methodOutput;
    }

    return method.invoke(object, args);
  }

  private Class<?> findFirstClassWithMethod(Method method) {
    Class<?> curClass = object.getClass();

    while (curClass != null) {
      try {
        curClass.getDeclaredMethod(
            method.getName(), method.getParameterTypes());
        return curClass;
      } catch (NoSuchMethodException _) {
        curClass = curClass.getSuperclass();
      }
    }

    return null;
  }

  private boolean findAnnotationInHierarchy(Class<?> methodClass, Method method,
      Class<? extends Annotation> annotationToFind)
      throws NoSuchMethodException {
    if (methodClass.getMethod(method.getName(), method.getParameterTypes())
            .isAnnotationPresent(annotationToFind)) {
      return true;
    }

    for (Class<?> i : methodClass.getInterfaces()) {
      try {
        Method iMethod =
            i.getMethod(method.getName(), method.getParameterTypes());
        if (iMethod.isAnnotationPresent(annotationToFind)) {
          return true;
        }
      } catch (NoSuchMethodException _) {
      }
    }

    return false;
  }

  private final T object;
  private final HashMap<String, Object> methodsCache =
      new HashMap<String, Object>();
}

