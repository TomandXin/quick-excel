package com.tom.excel.common;

import com.sun.tools.corba.se.idl.toJavaPortable.DefaultFactory;

import java.util.concurrent.*;

/**
 * 线程工具类
 *
 * @author tomxin
 * @date 2018-11-13
 * @since v1.0.0
 */
public class ThreadPoolFactory {

    private static ExecutorService FIX_Thread_POOl = new ThreadPoolExecutor(
            20,
            100,
            1,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(256)
    );

    /**
     * 运行任务
     *
     * @param task
     */
    public static void execute(Runnable task) {
        FIX_Thread_POOl.execute(task);
    }
}
