package com.mycompany.test;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;  
public class EnhancerTest {
	public String process(){  
        return "process finished";  
    }  
      
      
    public static void main(String[] args) {  
        Enhancer enhancer = new Enhancer();  
        enhancer.setSuperclass(EnhancerTest.class);  
        enhancer.setUseCache(false);  
        enhancer.setCallback(new MethodInterceptor() {  
            public Object intercept(Object obj, Method method, Object[] args,  
                    MethodProxy proxy) throws Throwable {  
                System.out.printf("do somthing before method-%s \n", method.getName());  
                Object result = proxy.invokeSuper(obj, args);  
                System.out.printf("do somthing after method-%s \n", method.getName());  
                return result;  
            }  
        });  
          
        // construct Test object  
        EnhancerTest test = (EnhancerTest) enhancer.create();  
        System.out.printf("result: %s\n", test.process());  
    }  
}
