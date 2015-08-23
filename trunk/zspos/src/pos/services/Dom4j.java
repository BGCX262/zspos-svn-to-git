package pos.services;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4j {
	private Document document =null;
	private String   filename = "";
	
	public  boolean  useable =false;

	public Dom4j(String str) {
		try {
			filename = str;
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(filename);
			
			useable=true;
		} catch (Exception e) {
			System.out.println("Dom4j初始化异常！ ");
			JOptionPane.showMessageDialog( null, "系统配置文档不可用！");
			e.printStackTrace();
			useable=false;
		}
	}
	
	public boolean save(){
		if(useable==false){
			return false;
		}
		try {
			File fi=new File(filename);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GBK");
			XMLWriter output = new XMLWriter(new FileWriter(fi) ,format);
			output.write(document);
			output.close();
			
			return true;
		} catch (Exception e) {
			System.out.println("XML文件保存失败！");
			e.printStackTrace();
			useable =false;
			return false;
		}
	}
	public boolean write(String xpath, String values) {
		if(useable==false){
			return false;
		}
		try {
			List     list = document.selectNodes(xpath);
			Iterator iter = list.iterator();
			if(iter.hasNext()) {
				Element element = (Element) iter.next();
				element.setText(values);
			}
			return true;
		} catch (Exception e) {
			System.out.println("Dom4j写入数值异常！");
			e.printStackTrace();
			useable =false;
			return false;
		}
	}
	public Iterator readAll(String xpath) {
		if(useable==false){
			return null;
		}
		try {
			List     list = document.selectNodes(xpath);
			Iterator iter = list.iterator();
			return   iter;
		} catch (Exception e) {
			System.out.println("读取XML出错！");
			e.printStackTrace();
			useable =false;
			return null;
		}
	}
	public String readFirst(String xpath) {
		if(useable==false){
			return "";
		}
		try {
			String values="";//返回查询的值
			List     list = document.selectNodes(xpath);
			Iterator iter = list.iterator();
			if(iter.hasNext()) {
				Element element = (Element) iter.next();
				values=element.getTextTrim();
			}
			return values;
		} catch (Exception e) {
			System.out.println("读取XML出错！");
			e.printStackTrace();
			useable =false;
			return "";
		}
	}
	public Document getDocument(){
		return document;
	}
	
//	public static void main(String[] args) {
//	// TODO Auto-generated method stub
//	Dom4j ob=new Dom4j("prtstyle.xml");
//	//写测试
//	ob.write("//destinationport", "林润");
//	//读测试
//	System.out.println(ob.readFirst("//destinationport"));
//	ob.save();
//	}
	
}
