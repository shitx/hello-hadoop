/**
 * 
 */
package com.shitx.hadoop;

import java.util.Date;import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;



/**
 * @author shitx
 *
 */
public class LanSpeed {
	
	public static void TreeMapTest(){

		
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("aa", "s");
		treeMap.put("c", "d");
		treeMap.put("a", "s");
		treeMap.put("b", "d");
		
		for(Entry<String , String> entry : treeMap.entrySet()){
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
		System.out.println(treeMap.firstKey());
		System.out.println(treeMap.lastKey());
	}
	
	public static void main(String args[]){
//		int j = 0;
//		//		long begin= System.nanoTime();
//		long begin = new Date().getTime();
//		System.out.println("begin=" + begin);
//
//
//		for(int i = 0; i < 100000000; i ++){
//			j = i * i;
//		}
//
//		//		long end = System.nanoTime();
//		long end = new Date().getTime();
//		System.out.println("end=" + end);
//		System.out.println((end - begin) + "ms");
		
		TreeMapTest();

	}
}
