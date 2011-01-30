package com.dds.partitioning;

import java.io.IOException;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import com.dds.utils.Helper;
import com.dds.utils.MurmurHashFunction; //HashFuction class should be imported, from there, Murmurhash or other hash functions can be derived

public class ConsistentHashing {
	 private final MurmurHashFunction hashFunction; //Fix this CH should be independent of Hashfunction
	 private final int numberOfReplicas;
	 private final SortedMap<Integer, T> circle = new TreeMap<Integer, T>();
	 Helper helperObj = new Helper();

	 public ConsistentHashing(MurmurHashFunction hashFunction, int numberOfReplicas, Collection<T> nodes) throws IOException {
	   this.hashFunction = hashFunction;
	   this.numberOfReplicas = numberOfReplicas;

	   for (T node : nodes) {
	     add(node);
	   }
	 }

	 public void add(T node) throws IOException {
	   for (int i = 0; i < numberOfReplicas; i++) {
	     circle.put(hashFunction.hash(helperObj.getBytes(node), i), node);
	   }
	 }

	 public void remove(T node) throws IOException {
	   for (int i = 0; i < numberOfReplicas; i++) {
	     circle.remove(hashFunction.hash(helperObj.getBytes(node), i));
	   }
	 }

	 public T get(Object key) throws IOException {
	   if (circle.isEmpty()) {
	     return null;
	   }
	   int hash = hashFunction.hash(helperObj.getBytes(key), 1); //Fix this int 1 there
	   if (!circle.containsKey(hash)) {
	     SortedMap<Integer, T> tailMap = circle.tailMap(hash);
	     hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
	   }
	   return circle.get(hash);
	 }

	 class T{
		 //TO-DO: This will be the node, which is com.dds.cluster.Node.java
	 }
}
