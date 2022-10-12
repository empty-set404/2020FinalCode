package com.coder.mybatis.pool;

import com.coder.mybatis.mapping.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MiniDataSource extends AbstractDataSource {

    /**
     * 空闲连接池
     */
    private LinkedBlockingDeque<Connection> idleConnectionPool;

    /**
     * 活跃连接池
     */
    private LinkedBlockingDeque<Connection> activeConnectionPool;

    /**
     * 连接池锁
     */
    private Lock lock = new ReentrantLock();

    /**
     * 最大连接数
     */
    private final int maxSize;

    /**
     * 最大等待时间, 单位：秒
     */
    private Long maxTimeOut;


    private static volatile MiniDataSource singleInstance;

    private MiniDataSource(Environment environment) {
        super(environment);
        this.idleConnectionPool = new LinkedBlockingDeque<>();
        this.activeConnectionPool = new LinkedBlockingDeque<>();
        this.maxSize = 2 << 4;
    }


    /**
     * 单例模式，连接池中有一个
     */
    public static MiniDataSource getInstance(Environment environment) {
        if (singleInstance == null) {
            synchronized (MiniDataSource.class) {
                if (singleInstance == null) {
                    singleInstance = new MiniDataSource(environment);
                }
            }
        }
        return singleInstance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = this.idleConnectionPool.poll();
        if (connection != null) {
            return connection;
        }

        // 加锁
        this.lock.lock();

        try {
            // 判断是否达到最大连接数
            if ((this.activeConnectionPool.size() + 1) <= this.maxSize) {
                // 创建连接
                final String driver = environment.getDriver();
                Class.forName(driver);
                connection = DriverManager.getConnection(environment.getUrl(), environment.getUsername(), environment.getPassword());
                this.activeConnectionPool.offer(connection);
                return connection;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 释放
            this.lock.unlock();
        }

        // 连接已达到最大连接数, 进行阻塞等待状态
        try {
            connection = this.idleConnectionPool.poll(this.maxTimeOut, TimeUnit.SECONDS);
            if (connection == null) {
                throw new RuntimeException("等待连接超时");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    /**
     * 释放连接
     */
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            this.activeConnectionPool.remove(connection);
            this.idleConnectionPool.offer(connection);
        }
    }

}
