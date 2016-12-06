/**
 * 
 */
package Files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.common.primitives.Bytes;
import com.sun.tools.classfile.Opcode.Set;
import com.sun.xml.internal.ws.util.ReadAllStream;



/**
 * @author shitx
 *
 */
public class Password {
	int partitions = 10;
	int batch = 50000000;
	private static Map<Integer, HashSet<String>> code = new HashMap<Integer,HashSet<String>>();
	private String prefix = "G:/data/163密码/";
	private String partition_prefix = "G:/data/163密码/partitions/partion_";
	private String fileName = "163.txt";
	
	@SuppressWarnings("resource")
	public void read(String filename){
		int total = 0;
		File file = new File(filename);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null){
				String[] sets = line.split(",");
				String mail = null;
				if(null != sets && sets.length >= 3){
					 mail = sets[3];
					 total += 1;
					 Integer partition = Math.abs(mail.hashCode() % partitions);
					 System.out.println(partition);
					 HashSet<String> mails = code.get(partition);
					if(mails == null){
						mails = new HashSet<>();
					}
					mails.add(mail);
					code.put(partition, mails);
				}
				
				if (total % batch == 0){
					write();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void ReadAllStream(String filename){
		File file = new File(filename);
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			Long filelen = file.length();
			byte[] bytes = new byte[filelen.intValue()];
			inputStream.read(bytes);
			System.out.println(bytes.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public void write(){
		for(Map.Entry<Integer, HashSet<String>> entry : code.entrySet()){
			Integer partition = entry.getKey();
			String path = partition_prefix + partition;
			File file = new File(path);
			BufferedWriter bufferedWriter = null;
			try {
				bufferedWriter = new BufferedWriter(new FileWriter(file, true));
				for(String mail: entry.getValue()){
					bufferedWriter.write(mail);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	String getFilePath(){
		return this.prefix + this.fileName;
	}
	
	public static void main(String[]args){
		Password password = new Password();
		String filename = password.getFilePath();
		password.read(filename);
//		password.ReadAllStream(filename);
	}
}
