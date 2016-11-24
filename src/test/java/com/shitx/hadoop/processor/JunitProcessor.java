/**
 * 
 */
package com.shitx.hadoop.processor;

import org.apache.hadoop.mapreduce.Job;

import junit.framework.TestCase;

/**
 * @author shitx
 *
 */


public class JunitProcessor extends TestCase{
	private TestProcessor testProcessor = new TestProcessor();
	
	public void test(){
		Job job= testProcessor.getJob();
	}
	
}
