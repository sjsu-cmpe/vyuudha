package com.dds.failureHandling.permanent;

//SOURCE: mauisched PROJECT ON SOURCEFORGE, NEED TO CLEAN IT UP

import java.io.*;
import java.util.*;

public class MerkleTree extends TreeMap {

	private int size = 0;

	private Hashtable revhash = new Hashtable();

	public MerkleTree() {
		super();
	}

	public synchronized void clear() {
		super.clear();
		revhash = new Hashtable();
		size = 0;
	}

	public int htSize() {
		return size;
	}

	public synchronized void htPut(Object treekey, Object hashkey, Object val) {
		if (revhash.containsKey(hashkey)) {
			throw new RuntimeException("hashkey '" + hashkey
					+ "' already exists in HashTree!");
		}

		Node node = null;
		if (super.containsKey(treekey)) {
			node = (Node) super.get(treekey);
		} else {
			node = new Node(treekey);
			super.put(treekey, node);
		}

		node.put(hashkey, val);
		revhash.put(hashkey, node);
		++size;
	}

	public synchronized Object htRemove(Object hashkey) {
		Node node = (Node) revhash.remove(hashkey);
		if (node == null) {
			return null;
		}

		Object val = node.remove(hashkey);
		if (node.size() == 0) {
			super.remove(node.key());
		}
		--size;
		return val;
	}

	public synchronized Object htGet(Object hashkey) {
		Node node = (Node) revhash.get(hashkey);
		if (node == null) {
			return null;
		}
		return node.get(hashkey);
	}

	public synchronized int size() {
		return super.size();
	}

	public synchronized boolean htContainsKey(Object hashkey) {
		return htGet(hashkey) != null;
	}

	public synchronized Object[] htInFix() {
		Object[] vals = new Object[size];

		Object[] nodes = values().toArray();
		int len = nodes.length;
		for (int i = len - 1, j = 0; i >= 0; i--) {// store highest priority 1st
			Node node = (Node) nodes[i];
			Object[] objs = node.inFix();
			System.arraycopy(objs, 0, vals, j, objs.length);
			j += objs.length;
		}

		return vals;
	}

	private static class Node implements Serializable {

		// Hash of ID -> Integer
		private Hashtable hash = new Hashtable();
		// Hash of Integer -> value
		private Map tree = Collections.synchronizedMap(new TreeMap());
		private Object parentkey;
		int counter = 0;

		public Node(Object parentkey) {
			if (parentkey == null) {
				throw new IllegalArgumentException("null key!");
			}
			this.parentkey = parentkey;
		}

		public Object key() {
			return parentkey;
		}

		public int size() {
			return hash.size();
		}

		public void put(Object key, Object val) {
			Integer i = new Integer(counter++);
			hash.put(key, i);
			tree.put(i, val);
		}

		public Object get(Object key) {
			Object obj = hash.get(key);
			return (obj == null) ? null : tree.get(obj);
		}

		public Object remove(Object key) {
			Object obj = hash.remove(key);
			if (obj == null) {
				return null;
			}
			return tree.remove(obj);
		}

		public boolean containsKey(Object key) {
			return hash.containsKey(key);
		}

		public Object[] inFix() {
			return tree.values().toArray();
		}
	}
}
