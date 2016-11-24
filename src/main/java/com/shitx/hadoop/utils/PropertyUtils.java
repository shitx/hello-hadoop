/**
 * 
 */
package com.shitx.hadoop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shitx
 *
 */
public class PropertyUtils {
	
	private static Properties properties ;
	
	public static Properties getInstance(){
		if(null == properties){
			properties = new Properties();
			InputStream inputStream = PropertyUtils.class.getResourceAsStream("/properties/hadoop.properties");
			try {
				properties.load(inputStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	
}
