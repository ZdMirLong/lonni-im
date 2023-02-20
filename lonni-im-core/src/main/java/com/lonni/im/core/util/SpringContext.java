package com.lonni.im.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * spring相关操作类
 *
 * @author: Lonni
 * @date: 2022/5/27 0027 14:25
 */
@Slf4j
@Component
public class SpringContext implements BeanFactoryPostProcessor, ApplicationContextAware, DisposableBean {
    private static ConfigurableListableBeanFactory beanFactory;

    private static ApplicationContext context;

    public SpringContext() {

        log.info("进入SpringContext..");
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        SpringContext.beanFactory = configurableListableBeanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.context = applicationContext;
    }


    /**
     * 获取指定配置文件的值
     *
     * @param property
     * @return
     * @see org.springframework.beans.factory.annotation.Value
     */
    public static String getProperty(String property) {
        ApplicationContext context = SpringContext.context;
        Environment environment = context.getEnvironment();
        return environment.getProperty(property);
    }

    /**
     * 获取当前context环境
     *
     * @return
     */
    public static ApplicationContext getContext() {
        return SpringContext.context;
    }

    /**
     * 获取当前使用的profiles变量
     *
     * @return
     */
    public static String[] getActiveProfiles() {
        ApplicationContext context = SpringContext.context;
        Environment environment = context.getEnvironment();
        String[] activeProfiles = environment.getActiveProfiles();
        return activeProfiles;
    }


    /**
     * 查询继承了某个类的所有对象 无效
     *
     * @param zclass
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeanOfType(Class zclass) {
        Map beansOfType = beanFactory.getBeansOfType(zclass);
        return beansOfType;
    }

    public static <T> T getBean(String name) throws BeansException {
        return (T) beanFactory.getBean(name);
    }

    public static <T> T getBean(Class<T> clz) throws BeansException {
        T result = beanFactory.getBean(clz);
        return result;
    }

    public static boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.isSingleton(name);
    }

    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.getType(name);
    }

    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.getAliases(name);
    }

    public static <T> T getAopProxy(T invoker) {
        return (T) AopContext.currentProxy();
    }


    /**
     * 手动注册bean到spring容器中
     *
     * @param beanName  bean的名称
     * @param beanClass bean的实例对象
     * @param isDestroy 是否将当前bean注销掉,用于动态加载已经存在的bean
     * @param <T>
     * @return 返回创建后的bean
     */
    public static <T> T registerSingletonBean(String beanName, T beanClass, Boolean isDestroy) {
        //先删除调
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
        if (isDestroy) {
            //销毁指定实例 execute是上文注解过的实例名称 @Bean(name="execute")
            if (beanFactory.containsBean(beanName)) {
                beanFactory.destroySingleton(beanName);
            }
        }
        //注册
        beanFactory.registerSingleton(beanName, beanClass);
        return getBean(beanName);
    }

    /**
     * 从容器中移除bean
     *
     * @param beanName bean的名称
     */
    public static void removeBean(String beanName) {
        BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory) ((AbstractRefreshableApplicationContext) context).getBeanFactory();
        beanDefReg.removeBeanDefinition(beanName);
    }

    /**
     * 向容器中添加bean对象
     * 注意:此方法需要先判断容器中是否有bean的定义对象 如果有不添加
     * 没有就添加定义对象 在注册到容器中;
     * 加入成功后可通过 ApplicationContext.getBean获取对象
     *
     * @param beanName        bean的名称
     * @param beanClass       bean的class对象
     * @param constructValues 构造函数的参数值,没有可不传
     * @return boolean
     */
    public static boolean addBean(String beanName, Class beanClass, Object... constructValues) {

        BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory) ((AbstractRefreshableApplicationContext) context).getBeanFactory();
        if (beanDefReg.containsBeanDefinition(beanName)) {
            return false;
        }
        BeanDefinitionBuilder beanDefBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
        for (Object constructValue : constructValues) {
            beanDefBuilder.addConstructorArgValue(constructValue);
        }
        BeanDefinition beanDefinition = beanDefBuilder.getBeanDefinition();
        beanDefReg.registerBeanDefinition(beanName, beanDefinition);
        return true;
    }


    /**
     * 发布事件
     *
     * @param event
     */
    public static void publishEvent(ApplicationEvent event) {
        if (context == null) {
            return;
        }
        context.publishEvent(event);
    }

    @Override
    public void destroy() throws Exception {
        log.info("清除上下文...");
        beanFactory = null;
        context = null;
    }

    /**
     * 判断是否是测试环境
     *
     * @return
     */
    public static boolean isTest() {
        try {
            String property = context.getEnvironment().getProperty("chenyun.isTest");
            return Boolean.parseBoolean(property);
        } catch (Exception ex) {
            return false;
        }
    }


    /**
     * 获取包含某个注解的所有bean对象
     * @param var1
     * @return
     */
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> var1) {
        Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(var1);
        return beansWithAnnotation;
    }

}
