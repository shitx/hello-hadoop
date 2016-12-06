/**
 * 
 */
package collections;

import java.util.TreeMap;

public class TMap  {     
    public static void main(String[] args) {     
        TreeMap<String , Double> map =  new TreeMap<String , Double>();     
        map.put("ccc" , 89.0);     
        map.put("aaa" , 80.0);     
        map.put("zzz" , 80.0);     
        map.put("bbb" , 89.0);     
        System.out.println(map);     
    }     
} 