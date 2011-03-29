package com.dds.init;

import java.net.UnknownHostException;

public class InitDDSTest {

	public static void main(String[] args) {
		(new Thread(new StartOneInstance())).start();
		(new Thread(new StartSecondInstance())).start();
	}
}

class StartOneInstance implements Runnable {

	@Override
	public void run() {
		try {
			InitDDS.start("");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}

class StartSecondInstance implements Runnable {

	@Override
	public void run() {
		try {
			InitDDS.start("");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
