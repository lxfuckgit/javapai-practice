package com.javapai.practice.jdk.thread;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TestCallable {
	public static void main(String[] args) {
		// 0.产生线程实例.
		Callable<String> call = new TestCallableImpl("线程1");

		// 1.执行Callable方式，需要FutureTask实现类的支持，用于接收运算结果.
		FutureTask<String> task = new FutureTask<String>(call);
		new Thread(task).start();

		// 2.接收线程运算后的结果.
		try {
			String result = task.get(); // FutureTask 可用于闭锁
			System.out.println(result);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}

class TestCallableImpl implements Callable<String> {
	private String taskNum;

	TestCallableImpl(String taskNum) {
		this.taskNum = taskNum;
	}

	public String call() throws Exception {
		System.out.println(">>>" + taskNum + "任务启动");
		Date dateTmp1 = new Date();
		Thread.sleep(1000);
		Date dateTmp2 = new Date();
		long time = dateTmp2.getTime() - dateTmp1.getTime();
		System.out.println(">>>" + taskNum + "任务终止");
		return taskNum + "任务返回运行结果,当前任务时间【" + time + "毫秒】";
	}
}
