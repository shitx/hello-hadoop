/**
 * 
 */
package com.shitx.hadoop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.hadoop.fs.Path;

import sun.tools.tree.ThisExpression;

/**
 * @author shitx
 *
 */
public class MergeSort {
	
	public static void readDataTest() throws IOException{
		String path = new MergeSort().getClass().getResource("/datas/merge/data.txt").getPath();
		System.out.println(path);
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
			String line = null;
			while((line = bufferedReader.readLine()) != null){
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String [] args){
		try {
			readDataTest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
