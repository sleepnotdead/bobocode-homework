package com.bobocode.homework.context;

import com.bobocode.homework.annotation.Bean;
import com.bobocode.homework.exception.NoSuchBeanException;
import com.bobocode.homework.exception.NoUniqueBeanException;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContexImpl implements ApplicationContext {

    public static final Map<String, Object> rootContextMap = new ConcurrentHashMap<>();

    public ApplicationContexImpl(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> beanClasses = reflections.getTypesAnnotatedWith(Bean.class);
        for (Class<?> beanClass : beanClasses) {
            Bean beanAnnotation = beanClass.getAnnotation(Bean.class);
            String beanName = beanAnnotation.value();
            if (beanName.isEmpty()) {
                beanName = beanClass.getSimpleName();
                beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
            }
            try {
                Constructor<?> constructor = beanClass.getConstructor();
                Object beanInstance = constructor.newInstance();
                rootContextMap.put(beanName, beanInstance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        int count = 0;
        T bean = null;
        for (Object value : rootContextMap.values()) {
            if (beanType.isAssignableFrom(value.getClass())) {
                bean = (T) value;
                count++;
            }
        }
        if (count == 0) {
            throw new NoSuchBeanException();
        } else if (count > 1) {
            throw new NoUniqueBeanException();
        }
        return bean;
    }

    @Override
    public <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException {
        Object bean = rootContextMap.get(name);
        if (bean == null) {
            throw new NoSuchBeanException();
        }

        return (T) bean;
    }

    @Override
    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        Map<String, T> beansOfType = new ConcurrentHashMap<>();
        for (Map.Entry<String, Object> entry : rootContextMap.entrySet()) {
            if (beanType.isAssignableFrom(entry.getValue().getClass())) {
                beansOfType.put(entry.getKey(), (T) entry.getValue());
            }
        }
        return beansOfType;
    }
}
