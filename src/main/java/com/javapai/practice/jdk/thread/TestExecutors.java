package com.javapai.practice.jdk.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TestExecutors {
	/**
	 * 1、固定线程池创建.<br>
	 * 2、默认拒绝策略.<br>
	 */
	@Test
	public void testFixedThreadPool() {
//		ExecutorService pool1 = Executors.newFixedThreadPool(10);

		LinkedBlockingDeque<Runnable> deque = new LinkedBlockingDeque<>(3);
		ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 4, 0, TimeUnit.MILLISECONDS, deque);
		
		//当执行第8次循环时，线程数量=maximumPoolSize，线程池执行了默认拒绝策略AbortPolicy: 丢弃新任务，并抛出异常。所以设置线程数和队列容量的时候需根据实际需求指定。
		for (int i = 0; i < 8; i++) {
			System.out.println("PoolSize: " + pool.getLargestPoolSize());
			System.out.println("dequeSize: " + deque.size());
			int b = i;
			pool.submit(() -> {
				System.out.println(b + "is run...");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
			});
		}
		pool.shutdown();
	}

	@Test
	public void testSingleThreadExecutor() {
		Executors.newSingleThreadExecutor();
	}

	@Test
	public void testCachedThreadPool() {
		Executors.newCachedThreadPool();
	}

	@Test
	public void testScheduledThreadPool() {
		Executors.newScheduledThreadPool(10);
	}

}
