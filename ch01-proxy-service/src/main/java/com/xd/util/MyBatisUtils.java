package com.xd.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtils {
    private static SqlSessionFactory factory = null;

    private MyBatisUtils() {
    }

    static {
        String config = "mybatis.xml";
        try {
            InputStream in = Resources.getResourceAsStream(config);
            factory = new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    1、ThreadLocal可以当作一个容器，里面可以放当前线程的局部变量，保证了线程隔离。
    2、ThreadLocal是Thread所属的，ThreadLocal其实是一个空壳子，ThreadLocal有一个内部类叫ThreadLocalMap
    给ThreadLocal存和取，其实是给ThreadLocalMap存和取。
    3、ThreadLocalMap不是一个Map，它是一个Entry数组，Entry的key是ThreadLocal的一个弱引用，value是保存的值。
    4、可以将多线程共享的数据放ThreadLocal里，然后每次从ThreadLocal中存取，这样保证一个线程操作的是一个对象，不会拿到其它线程的对象
    5、ThreadLocal是基于当前线程的存取机制。只要当前线程存在，ThreadLocal里保存的就还在；线程销毁了，ThreadLocal里面存的也销毁了
    6、思考：为什么将SqlSession放到ThreadLocal里？
            SqlSession是一个局部变量，不存在线程安全问题，但是假如在A类中获取了SeqSession对象，并把这个SqlSession对象
            存到ThreadLocal里，在B类中就可以直接从ThreadLocal中取，这样保证了A类、B类操作的是一个SqlSession对象，干的
            是同一个事务。否则B类中又创建了一个新的SqlSession对象。
    * */
    private static ThreadLocal<SqlSession>  t = new ThreadLocal<>();

    public static SqlSession getSqlSession() {
        SqlSession sqlSession = t.get();
        if (sqlSession == null) {
            sqlSession = factory.openSession();

            t.set(sqlSession);
        }
        return sqlSession;
    }

    // 写这个方法的目的，主要是
    public static void MyClose(SqlSession sqlSession){
        if (sqlSession != null) {
            // 数据库虽然访问完了，但是线程没有销毁，只会返还给了线程池。
            sqlSession.close();

            // 必须加，容易往
            // 目的是将ThreadLocal中保存的SqlSession对象剥离开，强制清空ThreadLocal
            t.remove();
            /*
             *  思考：为什么一定要手动将ThreadLocal里保存的SqlSession对象剥离？
             *  线程死亡了，ThreadLocal也就销毁了，它里面保存的session对象也就销毁了，为什么还要清空？
             *  因为这个线程不是新建出来的，而是从tomcat的线程池中拿的。当线程的run()方法执行完，不会销毁该线程
             *  只是把该线程返还给线程池。如果不手动清空，会导致下一次拿到该线程里面保存了SqlSession对象。造成
             *  SqlSession状态混乱。
             */
        }
    }
}