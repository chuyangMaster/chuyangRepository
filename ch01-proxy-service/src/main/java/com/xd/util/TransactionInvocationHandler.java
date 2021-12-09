package com.xd.util;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionInvocationHandler implements InvocationHandler {
    // 目标对象
    private Object target;

    public TransactionInvocationHandler(Object target){
        this.target = target;
    }

    /**
     * 代理类执行的方法
     * @param proxy 代理对象
     * @param method 目标方法
     * @param args 目标方法的参数
     * @return 代理类执行后的结果
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SqlSession sqlSession = null;
        Object obj = null;
        // 这里try-catch主要是为了提交、回滚事务
        try{
            // 这里获取sqlSession主要是为了提交事务
            sqlSession = MyBatisUtils.getSqlSession();
            // 目标方法：处理业务逻辑
            obj = method.invoke(target, args);
            sqlSession.commit();
        }catch (Exception e){
            sqlSession.rollback();
            e.printStackTrace();
        }
        return obj;
    }

    // 获取代理类对象
    public Object getProxy(){
        // this就是当前的handle，即代理方法
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }

}
