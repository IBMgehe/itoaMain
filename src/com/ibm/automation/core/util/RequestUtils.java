package com.ibm.automation.core.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

/**
 * 封装request get post 参数为javabean
 * 
 * @author hujin
 *
 */
public class RequestUtils {
	private static Logger logger = Logger.getLogger(RequestUtils.class);

	public static Object copyParams(Class targetClass, HttpServletRequest request){
        Object bean = null;
        try {
            bean = targetClass.newInstance();
            Map params = request.getParameterMap();
            Set set = params.entrySet();
            for(Iterator iterator = set.iterator();iterator.hasNext();){
                Entry entry = (Entry) iterator.next();
                String name = (String) entry.getKey();
                Object[] values = (Object[]) entry.getValue();
                
                if(values!=null){
                    if(values.length == 1){
                        BeanUtils.copyProperty(bean, name, values[0]);
                    }else {
                        BeanUtils.copyProperty(bean, name, values);
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }

	public static void main(String[] args) {

	}
}
