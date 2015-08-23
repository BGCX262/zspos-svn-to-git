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
			System.out.println("Dom4j��ʼ���쳣�� ");
			JOptionPane.showMessageDialog( null, "ϵͳ�����ĵ������ã�");
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
			System.out.println("XML�ļ�����ʧ�ܣ�");
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
			System.out.println("Dom4jд����ֵ�쳣��");
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
			System.out.println("��ȡXML����");
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
			String values="";//���ز�ѯ��ֵ
			List     list = document.selectNodes(xpath);
			Iterator iter = list.iterator();
			if(iter.hasNext()) {
				Element element = (Element) iter.next();
				values=element.getTextTrim();
			}
			return values;
		} catch (Exception e) {
			System.out.println("��ȡXML����");
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
//	//д����
//	ob.write("//destinationport", "����");
//	//������
//	System.out.println(ob.readFirst("//destinationport"));
//	ob.save();
//	}
	
}
