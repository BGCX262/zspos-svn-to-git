package pos.mysoap.services;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import pos.mysoap.stub.MyServiceSearchStub;

public class mysoapSearch {

	private MyServiceSearchStub stub=null;
	private MyServiceSearchStub.Dosearch method=null;
	private MyServiceSearchStub.DosearchResponse resp=null;
	
	public boolean useable = false;
	
	public mysoapSearch(){
		try{
			//reader = new SAXReader();
			stub   = new MyServiceSearchStub();
			method = new MyServiceSearchStub.Dosearch();
			resp   = new MyServiceSearchStub.DosearchResponse();
			
			useable=true;
		}catch(Exception e){
			System.out.println("������POS��̨ʧ�ܡ�");
		}
	}
	public boolean search(Document document){
		if(useable==false){
			return false;
		}
		try{
			String xmlstr=document.asXML();
			//System.out.println("�����ͱ��ġ�" + xmlstr);
			method.setInput(xmlstr);
			resp=stub.dosearch(method);
			//System.out.println("�����ر��ġ�" + resp.get_return());
			document=DocumentHelper.parseText(resp.get_return());
			List list = document.selectNodes("//responsecode");
			Iterator iter = list.iterator();
			if(iter.hasNext()) {
				Element element = (Element) iter.next();
				String values=element.getTextTrim();
				if(values.equals("0")){
					System.out.println("���ñ����ѱ����ڷ�������");
					return true;
				}else{
					System.out.println("���ñ����ѻ�δ�ϴ���");
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
//			soapSearch ob=new soapSearch();
//			SAXReader saxReader = new SAXReader();
//			Document  document = saxReader.read("xml/mysoap/save.xml");
//			System.out.println( ob.search(document) );
//			
//		}catch(Exception ex){
//			
//		}
//	}

}
