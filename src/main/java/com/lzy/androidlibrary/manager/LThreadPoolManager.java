package com.lzy.androidlibrary.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 线程执行器
 *
 * @author linzhiyong
 * @time 2016年11月15日12:51:50
 * @email wflinzhiyong@163.com
 * @desc
 */
public class LThreadPoolManager {

	/** 线程执行器 **/
	private static ExecutorService executorService = null;

	/** 固定5个线程 **/
	private static int nThreads = 5;

	/** 单例 **/
	private static LThreadPoolManager taskExecutorPool = null;

	/** 初始化线程池 **/
	static {
		taskExecutorPool = new LThreadPoolManager(nThreads * getNumCores());
	}

	/** 构造函数 **/
	protected LThreadPoolManager(int threads) {
		// executorService = Executors.newFixedThreadPool(threads);
		executorService = Executors.newScheduledThreadPool(threads);
	}

	/**
	 * 取得单例
	 * 
	 * @return
	 */
	public static LThreadPoolManager getInstance() {
		return taskExecutorPool;
	}

	/**
	 * 取得线程执行器
	 * 
	 * @return
	 */
	public ExecutorService getExecutorService() {
		return executorService;
	}

	public ScheduledExecutorService getScheduledExcutorService() {
		return (ScheduledExecutorService) executorService;
	}

	public static int getNumCores() {
		int threadCount = Runtime.getRuntime().availableProcessors();
		return threadCount;
	}
}