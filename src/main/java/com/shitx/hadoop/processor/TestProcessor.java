/**
 * 
 */
package com.shitx.hadoop.processor;

import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shitx.hadoop.mapper.TestMapper;
import com.shitx.hadoop.reducer.TestReducer;
import com.shitx.hadoop.utils.PropertyUtils;
import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider.Text;

/**
 * @author shitx
 *
 */
public class TestProcessor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Properties properties = PropertyUtils.getInstance();
	private Configuration conf = new Configuration();
	
	public Job getJob(){
		logger.info(properties.toString());
		Job job = null;
		try {
			job = Job.getInstance(conf, "测试job");
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		
		job.setJarByClass(this.getClass());
		job.setMapperClass(TestMapper.class);
		job.setReducerClass(TestReducer.class);
		
		//输入输出格式
		job.setMapOutputKeyClass(Text.class); //map的输出keY
		job.setMapOutputValueClass(Text.class);//map的输出value格式
		
		job.setOutputKeyClass(Text.class); //设置该job最终的输出key格式
		job.setOutputKeyClass(Text.class); //该job最终的输出value格式
		
		//设置每个N行一个map
		job.getConfiguration().set(NLineInputFormat.LINES_PER_MAP, "500" );
		
		//设置输出路径
		logger.info(properties.getProperty("hadoop.ip"));
		Path path = new Path((String) properties.getProperty("hadoop.ip"));
		logger.info("setOutputPath=",properties.getProperty("hadoop.ip"));
		FileOutputFormat.setOutputPath(job, path);
		return job;
	}
}
