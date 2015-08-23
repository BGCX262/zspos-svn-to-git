package pos.mysoap.services;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import pos.mysoap.stub.MyServiceSaveStub;

public class mysoapSave {
	private MyServiceSaveStub stub=null;
	private MyServiceSaveStub.Dosave method=null;
	private MyServiceSaveStub.DosaveResponse resp=null;
	
	public boolean useable = false;
	public mysoapSave(){
		try{
			//reader = new SAXReader();
			stub   = new MyServiceSaveStub();
			method = new MyServiceSaveStub.Dosave();
			resp   = new MyServiceSaveStub.DosaveResponse();
			
			useable=true;
		}catch(Exception e){
			System.out.println("������POS��̨ʧ�ܡ�");
		}
	}
	public boolean save(Document document){
		if(useable==false){
			return false;
		}
		try{
			String xmlstr=document.asXML();
			//System.out.println("�����ͱ��ġ�" + xmlstr);
			method.setInput(xmlstr);
			resp=stub.dosave(method);
			//System.out.println("�����ر��ġ�" + resp.get_return());
			document=DocumentHelper.parseText(resp.get_return());
			List list = document.selectNodes("//responsecode");
			Iterator iter = list.iterator();
			if(iter.hasNext()) {
				Element element = (Element) iter.next();
				String values=element.getTextTrim();
				if(values.equals("0")){
					System.out.println("�������ϴ��ɹ���");
					return true;
				}else{
					System.out.println("�������ϴ�ʧ�ܡ�");
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception e){
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
//			Document  document = saxReader.read("xml/mysoap/save.xml");
//			System.out.println( ob.save(document) );
//			
//		}catch(Exception ex){
//			
//		}
//	}

}
