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
	
	private String strvar="";//���PolicySearch����ӿ�����
	public  boolean useable = false;	
	
	public  soapSearch(){
		try{
			stub   = new PolicySearchStub();
			method = new PolicySearchStub.SearchPolicyInfo();
			resp   = new PolicySearchStub.SearchPolicyInfoResponse();
			
			//���PolicySearch����ӿ�����,��ʼ��strvar
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
			System.out.println("��SOAP��Search�ӿڳ�ʼ���쳣��");
		}
	}
	
	public boolean search(String contno){
		if(useable==false){
			return false;
		}
		try{
			String xmlstr=strvar.replaceFirst("123456", contno);
			//System.out.println("�����ͱ��ġ�" + xmlstr);
			method.setIn0(xmlstr);
			resp=stub.searchPolicyInfo(method);
			//System.out.println("�����ر��ġ�" + resp.getOut());
			Document document=DocumentHelper.parseText(resp.getOut());
			List     list = document.selectNodes("//ContNo");
			Iterator iter = list.iterator();
			if(iter.hasNext()) {
				Element element = (Element) iter.next();
				String values=element.getTextTrim();
				if( values.equals(contno.trim()) ){
					System.out.println("��������ѯ���Ѳ�ѯ����"+values);
					return true;
				}else{
					System.out.println("��������ѯ��δ��ѯ����"+values);
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("��SOAP��dosearch(String contno)�����쳣��");
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
