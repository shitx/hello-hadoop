/**
 * 
 */
package com.shitx.hadoop;

import java.io.IOException;

import org.apache.commons.io.output.NullWriter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author shitx
 *
 */
public class SortInt {

	private static class SortIntMapper extends Mapper<Object, Text, IntWritable, Text>{
		public static IntWritable data = new IntWritable();
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException{
			if(value.toString().length() > 0){
				System.out.println("xxxxxxxxxxxx="+value.toString());
				data.set(Integer.parseInt(value.toString()));
				context.write(data, new Text("1"));
			}
		}
	}

	private static class SortIntReducer extends Reducer<IntWritable, Text, IntWritable, Text>{
		private static int cnt = 0;

		@SuppressWarnings("unused")
		public void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
			if(key.toString().length() > 0){
				for(Text value:values){
					System.out.println("yyyyyyy="+key.toString() + "  value=" + value.toString());
					context.write(key, new Text(String.valueOf(cnt)));
					cnt  += 1;
				}
			}
		}
	}

	public static void main(String[] args){
		String inputPath = "E:\\hadooptest\\sort\\input";
		String outputPath = "E:\\hadooptest\\sort\\output";
		Configuration conf = new Configuration();
		Job job = null;
		try {
			job = Job.getInstance(conf, "sortInt");
			job.setMapperClass(SortIntMapper.class);
			job.setReducerClass(SortIntReducer.class);
			job.setJarByClass(SortInt.class);
			job.setOutputKeyClass(IntWritable.class);
			job.setOutputValueClass(Text.class);
			FileInputFormat.addInputPath(job, new Path(inputPath));
			FileOutputFormat.setOutputPath(job, new Path(outputPath));
			System.exit(job.waitForCompletion(true) ? 0 : 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
