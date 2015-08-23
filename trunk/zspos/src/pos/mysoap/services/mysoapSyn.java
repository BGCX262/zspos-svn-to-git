package pos.mysoap.services;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import pos.mysoap.stub.MyServiceSynStub;

public class mysoapSyn {

	private MyServiceSynStub stub=null;
	private MyServiceSynStub.Dosyn  method=null;
	private MyServiceSynStub.DosynResponse resp=null;
	
	public boolean useable = false;
	
	public mysoapSyn(){
		try{
			//reader = new SAXReader();
			stub   = new MyServiceSynStub();
			method = new MyServiceSynStub.Dosyn();
			resp   = new MyServiceSynStub.DosynResponse();
			
			useable=true;
		}catch(Exception e){
			System.out.println("������POS��̨ʧ�ܡ�");
		}
	}
	
	public boolean syn(Document document){
		if(useable==false){
			return false;
		}
		try{
			String xmlstr=document.asXML();
			//System.out.println("�����ͱ��ġ�" + xmlstr);
			method.setInput(xmlstr);
			resp=stub.dosyn(method);
			//System.out.println("�����ر��ġ�" + resp.get_return());
			document=DocumentHelper.parseText(resp.get_return());
			List list = document.selectNodes("//responsecode");
			Iterator iter = list.iterator();
			if(iter.hasNext()) {
				Element element = (Element) iter.next();
				String values=element.getTextTrim();
				if(values.equals("0")){
					System.out.println("����������ͬ���ɹ���");
					return true;
				}else{
					System.out.println("����������ͬ��ʧ�ܡ�");
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
