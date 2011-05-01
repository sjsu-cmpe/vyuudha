package com.dds.metrics;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.yammer.metrics.*;
import com.yammer.metrics.core.TimerMetric;

public class DirectoryLister {
	private final TimerMetric timer = Metrics.newTimer(getClass(),
													   "directory-listing",
													   TimeUnit.MILLISECONDS,
													   TimeUnit.SECONDS);
	private final File directory;

	public DirectoryLister(File directory) {
		this.directory = directory;
	}

	public List<File> list() throws Exception {
		final File[] list = timer.time(new Callable<File[]>() {
			@Override
			public File[] call() throws Exception {
				return directory.listFiles();
			}
		});

		if (list == null) {
			return Collections.emptyList();
		}

		final List<File> result = new ArrayList<File>(list.length);
		for (File file : list) {
			result.add(file);
		}
		return result;
	}
}
