package com.dds.metrics;

import java.util.concurrent.Executors;
import java.io.File;
import java.util.List;
import java.util.concurrent.*;

import com.yammer.metrics.*;

public class StorageMetrics {
	private static final int WORKER_COUNT = 10;
	private static final BlockingQueue<File> JOBS = new LinkedBlockingQueue<File>();
	private static final ExecutorService POOL = Executors.newFixedThreadPool(WORKER_COUNT);

	public static class Job implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					final File file = JOBS.poll(1, TimeUnit.MINUTES);
					if (file.isDirectory()) {
						final List<File> contents = new DirectoryLister(file).list();
						JOBS.addAll(contents);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public static void main(String[] args) throws Exception {
		Metrics.enableConsoleReporting(10, TimeUnit.SECONDS);

		System.err.println("Scanning all files on your hard drive...");

		JOBS.add(new File("/"));
		for (int i = 0; i < WORKER_COUNT; i++) {
			POOL.submit(new Job());
		}

		POOL.awaitTermination(10, TimeUnit.DAYS);
	}
}

