package com.javapai.practice.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

public class TestAtomic {
	
	@Test
	public void testIntOperation() {
		Operation op = new Operation();
		ExecutorService service = Executors.newCachedThreadPool();

		for (int i = 0; i < 10; i++) {
			 service.execute(new IntOperationTask(op));
		}
	}
	
	@Test
	public void testLongOperation() {
		Operation op = new Operation();
		ExecutorService service = Executors.newCachedThreadPool();

		for (int i = 0; i < 10; i++) {
			service.execute(new LongOperationTask(op));
		}
	}

	static class IntOperationTask implements Runnable {
		private Operation operation;

		public IntOperationTask(Operation op) {
			operation = op;
		}

		public void run() {
			while (true) {
				int oldNum, newNum;
				for (int i = 0; i < 32; i++) {
					oldNum = 1 << i;
					newNum = operation.assignInt(oldNum);
					if (oldNum != newNum) {
						int bits = 0;
						for (int j = 0; j < 32; j++) {
							if (0 != (newNum & (1 << j)))
								bits++;
						}

						if (1 != bits) {
							System.out.printf("[int TEST] It is no atomic operation." + " old:x new:xn", oldNum,
									newNum);
							System.exit(0);
						}
						// else
						// System.out.printf("[int TEST] It is no synchronousoperation." +
						// " old:x new:xn",oldNum, newNum);
					}
				}
			}
		}
	}

	static class LongOperationTask implements Runnable {
		private Operation operation;

		public LongOperationTask(Operation op) {
			operation = op;
		}

		public void run() {
			while (true) {
				long oldNum, newNum;
				long one = 1;
				for (int i = 0; i < 64; i++) {
					oldNum = one << i;
					newNum = operation.assignLong(oldNum);
					if (oldNum != newNum) {
						int bits = 0;
						for (int j = 0; j < 64; j++) {
							if (0 != (newNum & (one << j)))
								bits++;
						}

						if (1 != bits) {
							System.out.printf("[long TEST] It is no atomic operation. " + "old:6x new:6xn", oldNum,
									newNum);
							System.exit(0);
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		Operation op = new Operation();
		ExecutorService service = Executors.newCachedThreadPool();

		for (int i = 0; i < 10; i++) {
			// service.execute(new IntOperationTask(op));
			service.execute(new LongOperationTask(op));
		}
	}

}

class Operation {
	private int num = 0;
	private long bigNum = 0;

	public int assignInt(int n) {
		num = n;
		Thread.yield();
		return num;
	}

	public long assignLong(long n) {
		bigNum = n;
		Thread.yield();
		return bigNum;
	}
}
