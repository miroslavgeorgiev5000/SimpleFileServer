import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import java.io.IOException;
import java.nio.file.Path;

public class CommonFuncs{
	public static void writeFile(String name, String contents) throws Exception{
		File myFoo = new File(name);
		FileOutputStream fooStream = new FileOutputStream(myFoo, false); // true to append
																		 // false to overwrite.
		byte[] myBytes = contents.getBytes();
		fooStream.write(myBytes);
		fooStream.close();
	}

	public static void appendFile(String name, String contents){
		try {
			Files.write(Paths.get(name), (contents+"\n").getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			e.printStackTrace();
			//exception handling left as an exercise for the reader
		}
	}
	public static byte[] readFile(String aFileName) throws IOException {
		Path path = Paths.get(aFileName);
		return Files.readAllBytes(path);
	}
	public static int countLines(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	public static String toHex(byte[] bytes) {
			BigInteger bi = new BigInteger(1, bytes);
			return String.format("%0" + (bytes.length << 1) + "X", bi);
	}

	public static String getKey(HashMap<String,String> a, String v){
		for(String key : a.keySet()){
			if(a.get(key).equals(v)){
				return key;
			}
		}
		return "";
	}

	public static String genCode(){
		Random ran = new Random();
		String out="";
		for(int k=0;k<6;k++){
			out+=ran.nextInt(9);
		}
		return out;
	}
	public static String remSlashes(String in){
		String out = in.replaceAll("%2F","/");
		out = out.replaceAll("%2f","/");
		return out.replaceAll("/+","/");
	}
}