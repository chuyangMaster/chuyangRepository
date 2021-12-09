package com.xd.util;

public class ServiceFactory {
    /**
     * @param service 目标类对象
     * @return 返回代理类对象
     */
    public static Object getService(Object service){
        return new TransactionInvocationHandler(service).getProxy();
    }
}
