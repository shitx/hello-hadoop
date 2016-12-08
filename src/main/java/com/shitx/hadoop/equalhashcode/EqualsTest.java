/**
 * 
 */
package com.shitx.hadoop.equalhashcode;

import java.util.HashSet;  
import java.util.LinkedList;  
import java.util.Set;  
  
//http://blog.csdn.net/afgasdg/article/details/6889383 

public class EqualsTest {  
    public static void main(String[] args) {  
        LinkedList<Student> list = new LinkedList<Student>();  
        Set<Student> set = new HashSet<Student>();  
        Student stu1  = new Student(3,"张三");  
        Student stu2  = new Student(3,"张三");  
        System.out.println("stu1 == stu2 : "+(stu1 == stu2));  
        System.out.println("stu1.equals(stu2) : "+stu1.equals(stu2));  
        list.add(stu1);  
        list.add(stu2);  
        System.out.println("list size:"+ list.size());  
          
        set.add(stu1);  
        set.add(stu2);  
        System.out.println("set size:"+ set.size());  
    }  
  
} 