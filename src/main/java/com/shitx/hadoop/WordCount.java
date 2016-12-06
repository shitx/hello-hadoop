package com.shitx.hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

	public static class TokenizerMapper
	extends Mapper<Object, Text, Text, IntWritable>{
		//具体会有多少的mapper?2个因素：
		//1是文件个数，每个文件至少会分配1个map
		//2文件块大小和文件大小，如果文件超过了文件块大小，会被切分，让多个map做任务
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(Object key, Text value, Context context
				) throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			while (itr.hasMoreTokens()) {
				
				word.set(itr.nextToken());
				context.write(word, one);
			}
		}
	}

	public static class MyIntSumReducer  //这是在map的输出之后立刻执行的，它的结果被送入partition（如果有多个分区的话）
	//作用是为了减少网络传输数据量。在传送给reduce之前做一个规约。这里可以将它和reduce方法设置为一样，因为都是对某个key的value的值相加。
	extends Reducer<Text,IntWritable,Text,IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context
				) throws IOException, InterruptedException {
			System.out.println("dddddddddd="+key.toString());
			int sum = 0;
			for (IntWritable val : values) { 
				System.out.println("vvvvv111111="+val.get());  //应该全是1
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static class IntSumReducer
	extends Reducer<Text,IntWritable,Text,IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context
				) throws IOException, InterruptedException {
			int sum = 0;
			System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrditer="+key.toString());
			for (IntWritable val : values) {
				System.out.println("vvvvv222222="+val.get()); //可能是1，也可能是2，3... 因为在map的输出端已经做过一次规约
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}

	public static class IntPartion extends Partitioner<Text, IntWritable>{
		//它是在map和combine之后执行，它决定了数据被分配到哪一个reduce。我们要保证每个相同的key是被分配到同一个reduce的。hash就是其中的一种。默认是根据key的hashcode来分。
		//但是当只有1个分区时，hash没有意义，所有的数据都被送到同一个reduce。具体多少个取决于setNumReducer。如果设置为1（默认）则该方法不会被调用。否则会调用默认的或者我们自定义的partition方法来传递数据
		@Override
		public int getPartition(Text key, IntWritable value, int numPartitions) {
			// TODO Auto-generated method stub
			int partition = 0;
			
			if(numPartitions >= 2){
				partition = Math.abs(key.hashCode()) % numPartitions;
				System.out.println("partition="+partition);
			}
			return partition;
		} 
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		//在本地运行模式下，必须指定hadoop可执行文件所在的位置。运行时，通过该命令将这个任务提交到本地的这个hadoop。单机模式。  conf.set("hadoop.home.dir", "E:\\qutke\\software\\hadoop-common-2.2.0-bin"); 
		//当然这种指定方式也不是必须的，只要我们配置了JAVA_HOME,HADOOP_HOME。  这样默认找到的hadoop,java命令都是和对应的环境联系好的，因此就不用再把路径显示加入了。
		//这种执行方式和在命令行里设置任务，比如 hadoop -jar xx.jar inputpath outputpath是一个道理。
		//注意不要开着命令行进入对应target执行hadoop任务，会导致无法找不到主类（偶尔），可以关掉命令行再以这种方式运行。
		//这种默认执行方式是新起一个单节点的hadoop来运行任务，如果要将让它在集群上运行，在集群上通过命令行的方式，hadoop -jar，这样就是将对应的任务提交给hadoop集群了。

		//开发模式：
		//【1】 本地：  请下载https://github.com/srccodes/hadoop-common-2.2.0-bin 【hadoop dll包和运行时环境】【没有提交到云端，其实是在本地的一个hadoop虚拟云环境里跑任务】
		//【2】在服务器上运行：  在服务器上搭建和启动hadoop集群，把jar包拷贝过去，hadoop -jar方式提交给这个集群。
		//【3】 提交给云端服务器运行： 【登录到云端服务器，然后把jar包拷贝过去，然后提交，其实和【2】是一样的】
		//【4】采用eclipse的插件方式提交任务。
		// 要注意： hadoop集群有自己的jar包，如果在本地跑时，所有相关的jar包要和集群保持一致，否则后续会有严重问题。【如果有测试环境，测试环境和生产环境一致，那么可以将任务提交给测试环境，然后运行，提交方式其实也是把jar包拷贝过去，
		//在本地开发时，要么有自己相同版本的单节点集群，要么在本地搞一个虚拟的环境，不用装集群，只要有对应的包即可，但一定要相同版本】
		//    conf.set("hadoop.home.dir", "E:\\qutke\\software\\hadoop-common-2.2.0-bin"); 
		//	System.setProperty("hadoop.home.dir", "E:\\qutke\\software\\hadoop-common-2.2.0-bin");
		//	conf.set("hadoop.home.dir", "E:\\qutke\\software\\hadoop-common-2.2.0-bin");
		//	conf.addResource("classpath:/hadoop/core-site.xml");
		//	conf.addResource("classpath:/hadoop/hdfs-site.xml");
		//	conf.addResource("classpath:/hadoop/mapred-site.xml");
		Job job = Job.getInstance(conf, "word count");
		job.setJarByClass(WordCount.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(MyIntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setPartitionerClass(IntPartion.class);
		job.setNumReduceTasks(1);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		//    job.waitForCompletion(true);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
