package com.dds.cluster;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.util.List;
import java.util.LinkedList;

public class ClusterCommunication extends ReceiverAdapter {
	JChannel channel;
	String user_name = System.getProperty("user.name", "n/a");
	final List<String> state = new LinkedList<String>();

	public void viewAccepted(View new_view) {
		System.out.println("** view: " + new_view);
	}

	public void receive(Message msg) {
		String line = msg.getSrc() + ": " + msg.getObject();
		System.out.println(line);
		synchronized (state) {
			state.add(line);
		}
	}

	public byte[] getState() {
		synchronized (state) {
			try {
				return Util.objectToByteBuffer(state);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public void setState(byte[] new_state) {
		try {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) Util.objectFromByteBuffer(new_state);
			synchronized (state) {
				state.clear();
				state.addAll(list);
			}
			System.out.println("received state (" + list.size()
					+ " messages in chat history):");
			for (String str : list) {
				System.out.println(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void start() throws Exception {
		channel = new JChannel();
		channel.setReceiver(this);
		channel.connect("ClusterName");
		channel.getState(null, 10000);
		eventLoop();
		channel.close();
	}

	private void eventLoop() {
		while (true) {
			try {
				String line = "I am alive with NodeID"; //Data (replication) + nodeid + gossip
				Message msg = new Message(null, null, line);
				channel.send(msg);
			} catch (Exception e) {
			}
		}
	}

	public void startCommunication() {
		try {
			new ClusterCommunication().start();
		} catch (Exception e) {
			
		}
	}
}
