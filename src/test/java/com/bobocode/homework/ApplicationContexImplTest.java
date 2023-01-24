package com.bobocode.homework;

import com.bobocode.homework.context.ApplicationContexImpl;
import com.bobocode.homework.exception.NoSuchBeanException;
import com.bobocode.homework.exception.NoUniqueBeanException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContexImplTest {

    @Test
    public void testGetBean_validInput_returnsBean() throws NoSuchBeanException, NoUniqueBeanException {
        ApplicationContexImpl context = new ApplicationContexImpl("com.bobocode.homework");
        BeanA bean = context.getBean(BeanA.class);
        assertNotNull(bean);
    }

    @Test
    public void testGetBean_noBeanFound_throwsNoSuchBeanException() {
        ApplicationContexImpl context = new ApplicationContexImpl("com.bobocode.homework");
        assertThrows(NoSuchBeanException.class, () -> context.getBean(BeanB.class));
    }

    @Test
    public void testGetBean_multipleBeansFound_throwsNoUniqueBeanException() {
        ApplicationContexImpl context = new ApplicationContexImpl("com.bobocode.homework");
        ApplicationContexImpl.rootContextMap.put("beanD", new BeanC());
        assertThrows(NoUniqueBeanException.class, () -> context.getBean(BeanC.class));
    }

    @Test
    public void testGetBean_validNameAndType_returnsBean() throws NoSuchBeanException {
        ApplicationContexImpl context = new ApplicationContexImpl("com.bobocode.homework");
        BeanA bean = context.getBean("beanA", BeanA.class);
        assertNotNull(bean);
    }

    @Test
    public void testGetBean_invalidName_throwsNoSuchBeanException() {
        ApplicationContexImpl context = new ApplicationContexImpl("com.bobocode.homework");
        assertThrows(NoSuchBeanException.class, () -> context.getBean("invalidBean", BeanA.class));
    }

    @Test
    public void testGetAllBeans_validType_returnsMap() {
        ApplicationContexImpl context = new ApplicationContexImpl("com.bobocode.homework");
        Map<String, BeanA> beans = context.getAllBeans(BeanA.class);
        assertEquals(1, beans.size());
        assertTrue(beans.containsKey("beanA"));
    }

    @Test
    public void testGetAllBeans_invalidType_returnsEmptyMap() {
        ApplicationContexImpl context = new ApplicationContexImpl("com.bobocode.homework");
        Map<String, BeanB> beans = context.getAllBeans(BeanB.class);
        assertTrue(beans.isEmpty());
    }
}
