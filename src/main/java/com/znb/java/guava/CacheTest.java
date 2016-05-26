package com.znb.java.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;
import com.google.common.cache.RemovalNotification;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangnaibin@xiaomi.com
 * @time 2016-03-11 上午11:18
 */
public class CacheTest {
    public void init() throws Exception{
        // 初始化时定义加载数据源
        LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
                // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                // 设置写缓存访问4秒钟过期
                .expireAfterAccess(4, TimeUnit.SECONDS)
                // 设置缓存容器的初始容量为10
                .initialCapacity(10)
                // 设置要统计缓存的命中率
                .recordStats()
                // 设置缓存的移除通知
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> notification) {

                    }
                })
                .removalListener(RemovalListeners.asynchronous(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> notification) {

                    }
                }, new Executor() {
                    @Override
                    public void execute(Runnable command) {

                    }
                }))
                // 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(100)
                // 设置写缓存后8秒钟过期
                .expireAfterWrite(10, TimeUnit.SECONDS)
                // build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String o) throws Exception {
                        return null;
                    }
                });
        // 获取命中率统计情况
        System.out.println(cache.stats().toString());

        // 为key加载新值
        cache.refresh("key");
        // get是定义数据加载源
        Cache<String, Object> cache1 = CacheBuilder.newBuilder().maximumSize(1000).build();
        Object value = cache1.get("key", new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });

        cache.size();
        cache.get("");
        cache.invalidate("");
        cache.invalidateAll();
//        RemovalListeners.asynchronous();
//        cache.invalidateAll(keys);
//        CacheBuilder.newBuilder().ticker();
//        CacheBuilder.newBuilder().refreshAfterWrite()
    }
}
