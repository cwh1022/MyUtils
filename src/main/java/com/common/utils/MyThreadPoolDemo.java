package com.common.utils;

import java.util.concurrent.*;

/**
 * Created on 2019/12/2
 *
 * @author connor.chen
 * <p>
 * 第4种获得|使用java多线程的方式，线程池
 */
public class MyThreadPoolDemo {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 1L
                , TimeUnit.SECONDS, new LinkedBlockingQueue<>(3)
                , Executors.defaultThreadFactory()
                , new ThreadPoolExecutor.CallerRunsPolicy());
        try {
            for (int i = 0; i < 8; i++) {
                executorService.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    private static void threadPoolInit() {
        ExecutorService threadPool = Executors.newFixedThreadPool(5); // 一池5个处理线程
//        ExecutorService threadPool = Executors.newSingleThreadExecutor(); // 一池1个处理线程
//        ExecutorService threadPool = Executors.newCachedThreadPool(); // 一池N个处理线程

        // 模拟10个用户来办理业务，每个用户就是一个来自外部的请求线程
        try {
            for (int i = 0; i < 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
