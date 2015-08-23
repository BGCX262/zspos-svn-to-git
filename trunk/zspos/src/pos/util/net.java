package pos.util;


import java.io.InputStream;
import java.net.URL;


public class net {
	public static boolean isonline(){
        try {  
			URL url = new URL("http://10.44.3.136:8080");
			InputStream in = url.openStream();
			in.close();
			System.out.println("【网络连接正常】");
			return true;

        } catch (Exception e) {  
        	System.out.println("【网络连接异常】"+e.getMessage());  
        	return false;
        }  
	}
//	public static void main(String[] args) {
//		net.isonline();
//	}	
}
