package github.rebelhero.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池工具类
 *
 * @author rebelhero
 * @date 2020/11/30
 */
@Slf4j
public class ThreadPoolFactoryUtils {


    /**
     * 核心线程数
     */
    private static final int CORE_SIZE = 5;

    /**
     * 最大线程数
     */
    private static final int MAX_SIZE = 10;

    /**
     * 存活时间， 单位：秒
     */
    private static final int KEEP_ALIVE_TIME = 60;

    /**
     * 阻塞队列大小
     */
    private static final int BLOCKING_QUEUE_SIZE = 50;


    /**
     * key -> threadNamePrefix, value -> threadPool
     */
    private static final Map<String, ExecutorService> THREAD_POOLS = new ConcurrentHashMap<>();

    private ThreadPoolFactoryUtils() {

    }

    /**
     * 获取线程池，如果没有则创建后返回。
     */
    public static ExecutorService getThreadPool(String threadNamePrefix, Boolean daemon) {
        ExecutorService executorService = THREAD_POOLS.get(threadNamePrefix);
        if (ObjectUtil.isNull(executorService) || executorService.isShutdown() || executorService.isTerminated()) {
            log.info("创建线程池ing");
            ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
            BlockingQueue<Runnable> blockingDeque = new ArrayBlockingQueue<>(BLOCKING_QUEUE_SIZE);
            // 创建线程池。
            executorService = new ThreadPoolExecutor(CORE_SIZE, MAX_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                    blockingDeque, threadFactory);
            THREAD_POOLS.put(threadNamePrefix, executorService);
        }
        return executorService;
    }


    /**
     * 创建线程工厂。
     */
    private static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon) {
        return StrUtil.isNotBlank(threadNamePrefix) ? new ThreadFactoryBuilder()
                .setNameFormat(threadNamePrefix + "-%d")
                .setDaemon(ObjectUtil.isNotNull(daemon) ? daemon : false)
                .build() : Executors.defaultThreadFactory();
    }

    /**
     * 关闭所有已有线程池。
     */
    public static void shutDownAllThreadPool() {
        log.info("关闭所有线程池");
        for (Map.Entry<String, ExecutorService> entry : THREAD_POOLS.entrySet()) {
            String threadPoolName = entry.getKey();
            ExecutorService executorService = entry.getValue();
            // 线程池现有的任务还可以执行，但不接受新任务。
            executorService.shutdown();
            log.info("关闭线程池：{}， 调用shutNow后是否成功，{}", threadPoolName, executorService.isTerminated());
            try {
                // 等待现有任务执行完成。
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.info("任务时间过长，强制关闭");
                // 如果超时，强制关闭那些执行任务过长的线程池
                executorService.shutdownNow();
            }

        }
    }


}
