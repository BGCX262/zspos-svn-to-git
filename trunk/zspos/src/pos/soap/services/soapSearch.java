package pos.soap.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import pos.soap.stub.PolicySearchStub;

public class soapSearch {

	private PolicySearchStub stub=null;
	private PolicySearchStub.SearchPolicyInfo method=null;
	private PolicySearchStub.SearchPolicyInfoResponse resp=null;
	
	private String strvar="";//针对PolicySearch特殊接口设置
	public  boolean useable = false;	
	
	public  soapSearch(){
		try{
			stub   = new PolicySearchStub();
			method = new PolicySearchStub.SearchPolicyInfo();
			resp   = new PolicySearchStub.SearchPolicyInfoResponse();
			
			//针对PolicySearch特殊接口设置,初始化strvar
			File file = new File ("xml/soap/search.xml"); 
			FileReader fileReader=new FileReader(file); 
			BufferedReader bufReader=new BufferedReader(fileReader); 
			String temp = "";
			while((temp = bufReader.readLine()) != null)
			{
				strvar = strvar + temp;
			}
			
			useable=true;
		}catch(Exception e){
			System.out.println("【SOAP的Search接口初始化异常】");
		}
	}
	
	public boolean search(String contno){
		if(useable==false){
			return false;
		}
		try{
			String xmlstr=strvar.replaceFirst("123456", contno);
			//System.out.println("【发送报文】" + xmlstr);
			method.setIn0(xmlstr);
			resp=stub.searchPolicyInfo(method);
			//System.out.println("【返回报文】" + resp.getOut());
			Document document=DocumentHelper.parseText(resp.getOut());
			List     list = document.selectNodes("//ContNo");
			Iterator iter = list.iterator();
			if(iter.hasNext()) {
				Element element = (Element) iter.next();
				String values=element.getTextTrim();
				if( values.equals(contno.trim()) ){
					System.out.println("【保单查询：已查询到】"+values);
					return true;
				}else{
					System.out.println("【保单查询：未查询到】"+values);
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("【SOAP的dosearch(String contno)方法异常】");
			return false;
		}	
	}
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		soapSearch ob=new soapSearch();
//		System.out.println( ob.search("66232000036503") );
//	}	
//	
}
