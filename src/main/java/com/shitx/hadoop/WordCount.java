package com.shitx.hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{

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

  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
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
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}