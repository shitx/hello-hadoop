package com.shitx.hadoop;
import java.util.HashMap;

public class TreadLocalTest {

    public static ThreadLocal<HashMap> map0 = new ThreadLocal<HashMap>(){ 
        @Override 
        protected HashMap initialValue() { 
            System.out.println(Thread.currentThread().getName()+"initialValue"); 
            return new HashMap(); 
        } 
    }; 
    
    public static ThreadLocal<String> tlS = new ThreadLocal<String>(){
    	protected String initialValue() {
    		return "initStr";
    	};
    };
    
    public void run(){ 
        Thread[] runs = new Thread[3]; 
        for(int i=0;i<runs.length;i++){ 
            runs[i]=new Thread(new T1(i)); 
        } 
        for(int i=0;i<runs.length;i++){ 
            runs[i].start(); 
        } 
    } 
    public static class T1 implements Runnable{ 
        int id; 
        public T1(int id0){ 
            id = id0; 
        } 
        public void run() { 
            System.out.println(Thread.currentThread().getName()+":start"); 
            HashMap map = map0.get(); //得到本线程的threadLocalMap<ThreadLocal<T=HashMap>,Object>,里头对应的Object,注意这里的Object其实就是initialValue里返回的HashMap
            //每个线程有自己的threadLocalMap，键是threadlocal对象，值就是保存的值，这个值是initialValue里返回的，一般是new出我们要的空对象。  这里可以保存很多threadlocal对象和value
            //只要线程调用该threadlocal的get或者set方法，就会在当前线程的threadLocalMap里加入该threadlocal，使用起来更加方便。线程之间相互不影响。
            //本质上来说，在线程里定义个局部变量，自己使用，是一样的
            String data = tlS.get();
            for(int i=0;i<10;i++){ 
                map.put(i, i+id*100); 
                try{ 
                    Thread.sleep(100); 
                }catch(Exception ex){ 
                } 
            } 
            tlS.set(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getName()+':'+tlS.get()); 
            System.out.println(Thread.currentThread().getName()+':'+map); 
        } 
    } 
    
    public static void hashMapTest(){
    	HashMap<String, String> hashMap = new HashMap<>();
    }
    
    /** 
     * Main 
     * @param args 
     */ 
    public static void main(String[] args){ 
        TreadLocalTest test = new TreadLocalTest(); 
        test.run(); 
    }

}