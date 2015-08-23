package pos.mysoap.services;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pos.mysoap.stub.MyServiceCheckupStub;

public class mysoapCheckup {
	private MyServiceCheckupStub stub=null;
	private MyServiceCheckupStub.Docheck method=null;
	private MyServiceCheckupStub.DocheckResponse resp=null;
	
	public boolean useable = false;
	
	public mysoapCheckup(){
		try{
			//reader = new SAXReader();
			stub   = new MyServiceCheckupStub();
			method = new MyServiceCheckupStub.Docheck();
			resp   = new MyServiceCheckupStub.DocheckResponse();
			
			useable=true;
		}catch(Exception e){
			System.out.println("连接POS后台失败！");
		}
	}
	
	public boolean checkup(Document document){
		if(useable==false){
			return false;
		}
		try{
			String xmlstr=document.asXML();
			//System.out.println("【发送报文】" + xmlstr);
			method.setInput(xmlstr);
			resp=stub.docheck(method);
			//System.out.println("【返回报文】" + resp.get_return());
			document=DocumentHelper.parseText(resp.get_return());
			List list = document.selectNodes("//responsecode");
			Iterator iter = list.iterator();
			if(iter.hasNext()) {
				Element element = (Element) iter.next();
				String values=element.getTextTrim();
				if(values.equals("0")){
					System.out.println("【该单证号码段已发放】");
					return true;
				}else{
					System.out.println("【该单证号码段未发放】");
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
		try{

			mysoapCheckup ob=new mysoapCheckup();
			SAXReader saxReader = new SAXReader();
			Document  document = saxReader.read("xml/mysoap/save.xml");
			System.out.println( ob.checkup(document) );
			
		}catch(Exception ex){
			
		}
	}

}
