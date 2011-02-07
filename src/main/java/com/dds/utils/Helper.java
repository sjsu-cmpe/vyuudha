package com.dds.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Helper {
	public byte[] getBytes(Object obj) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
			bos.close();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
		byte[] data = bos.toByteArray();
		return data;
	}
	
	/**
	 * Takes a byte array as input and returns an Object.
	 * Need to add a type cast in the calling function.
	 * 
	 * eg. (String) getObject(byteArray);
	 * 
	 * @param bytes
	 * @return Generic Object
	 */
	public Object getObject(byte[] bytes) {

        Object data = null;
    	ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    	ObjectInputStream ois = null;

    	try {
			ois = new ObjectInputStream(bis);
			data = ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
        
        return data;
	}
}
