package com.bobocode.homework.context;

import com.bobocode.homework.exception.NoSuchBeanException;
import com.bobocode.homework.exception.NoUniqueBeanException;

import java.util.Map;

public interface ApplicationContext {

    <T> T getBean(Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException;

    <T> T getBean(String name, Class<T> beanType) throws NoSuchBeanException;

    <T> Map<String, T> getAllBeans(Class<T> beanType);
}
