package pos.soap.services;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pos.mysoap.services.mysoapUpdate;
import pos.soap.stub.SavePolicyInfoStub;

public class soapSave {
	private SavePolicyInfoStub stub=null;
	private SavePolicyInfoStub.SavePolicyInfo method=null;
	private SavePolicyInfoStub.SavePolicyInfoResponse resp=null;
	
	public boolean useable = false;
	
	public soapSave(){
		try{
			//reader = new SAXReader();
			stub = new SavePolicyInfoStub();
			method = new SavePolicyInfoStub.SavePolicyInfo();
			resp = new SavePolicyInfoStub.SavePolicyInfoResponse();
			
			useable=true;
		}catch(Exception e){
			System.out.println("������ʵ��webservicesʧ�ܡ�");
		}
	}
	
	public boolean save(Document document){
		if(useable==false){
			return false;
		}
		try{
			String xmlstr=document.asXML();
			//System.out.println("�����ͱ��ġ�" + xmlstr);
			method.setIn0( xmlstr );
			resp=stub.savePolicyInfo(method);
			//System.out.println("�����ر��ġ�" + resp.getOut());
			document=DocumentHelper.parseText(resp.getOut());
			List list = document.selectNodes("//mResultCode");
			Iterator iter = list.iterator();
			if(iter.hasNext()) {
				Element element = (Element) iter.next();
				String values=element.getTextTrim();
				if(values.equals("0")){
					System.out.println("������ʵ���̨���б��ɹ���");
					return true;
				}else{
					System.out.println("������ʵ���̨���б�ʧ�ܡ�");
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception e){
			System.out.println("������ʵ���̨���б��쳣��"+e.getMessage());
			return false;
		}
	}
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		try{
//
//			soapSave ob=new soapSave();
//			SAXReader saxReader = new SAXReader();
//			Document  document = saxReader.read("D:/save.xml");
//			System.out.println( ob.save(document) );
//			
//		}catch(Exception ex){
//			
//		}
//	}
	
}
