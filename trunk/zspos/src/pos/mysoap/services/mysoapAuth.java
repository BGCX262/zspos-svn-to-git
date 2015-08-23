package pos.mysoap.services;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pos.mysoap.stub.MyServiceAuthStub;

public class mysoapAuth {
	private MyServiceAuthStub stub=null;
	private MyServiceAuthStub.Doauth method=null;
	private MyServiceAuthStub.DoauthResponse resp=null;
	
	public boolean useable = false;
	public mysoapAuth(){
		try{
			//reader = new SAXReader();
			stub   = new MyServiceAuthStub();
			method = new MyServiceAuthStub.Doauth();
			resp   = new MyServiceAuthStub.DoauthResponse();
			
			useable=true;
		}catch(Exception e){
			System.out.println("������POS��̨ʧ�ܡ�");
		}
	}
	
	public Document auth(Document document){
		if(useable==false){
			return null;
		}
		try{
			String xmlstr=document.asXML();
			//System.out.println("�����ͱ��ġ�" + xmlstr);
			method.setInput(xmlstr);
			resp=stub.doauth(method);
			//System.out.println("�����ر��ġ�" + resp.get_return());
			Document doc=DocumentHelper.parseText(resp.get_return());
			List list = doc.selectNodes("//responsecode");
			Iterator iter = list.iterator();
			if(iter.hasNext()) {
				Element element = (Element) iter.next();
				String values=element.getTextTrim();
				if(values.equals("0")){
					System.out.println("���û���֤�ɹ���");
					return doc;
				}else{
					System.out.println("���û���֤ʧ�ܡ�");
					return null;
				}
			}else{
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{

			mysoapAuth ob=new mysoapAuth();
			SAXReader saxReader = new SAXReader();
			Document  document = saxReader.read("xml/mysoap/save.xml");
			System.out.println( ob.auth(document).asXML() );
			
		}catch(Exception ex){
			
		}
	}	
	
	
}
