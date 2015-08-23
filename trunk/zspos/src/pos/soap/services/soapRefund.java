package pos.soap.services;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import pos.soap.stub.RefundUpdateStub;

public class soapRefund {
	private RefundUpdateStub stub=null;
	private RefundUpdateStub.RefundUpdate method=null;
	private RefundUpdateStub.RefundUpdateResponse resp=null;
	
	public boolean useable = false;
	
	public soapRefund(){
		try{
			//reader = new SAXReader();
			stub   = new RefundUpdateStub();
			method = new RefundUpdateStub.RefundUpdate();
			resp   = new RefundUpdateStub.RefundUpdateResponse();
			
			useable=true;
		}catch(Exception e){
			System.out.println("【连接实达webservices失败】");
		}

	}
	
	public boolean refund(Document document){
		if(useable==false){
			return false;
		}
		
		try{
			String xmlstr=document.asXML();
			//System.out.println("【发送报文】" + xmlstr);
			method.setIn0( xmlstr );
			resp=stub.refundUpdate(method);
			//System.out.println("【返回报文】" + resp.getOut());
			document=DocumentHelper.parseText(resp.getOut());
			List list = document.selectNodes("//mResultCode");
			Iterator iter = list.iterator();
			if(iter.hasNext()) {
				Element element = (Element) iter.next();
				String values=element.getTextTrim();
				if(values.equals("0")){
					System.out.println("【送往实达后台：退保成功】");
					return true;
				}else{
					System.out.println("【送往实达后台：退保失败】");
					return false;
				}
			}else{
				return false;
			}
		}catch(Exception e){
			//useable = false;   //承保失败后可能是数据格式问题，save方法依然能用！
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
//			soapRefund ob=new soapRefund();
//			SAXReader saxReader = new SAXReader();
//			Document  document = saxReader.read("D:/save.xml");
//			System.out.println( ob.refund(document) );
//			
//		}catch(Exception ex){
//			
//		}
//	}
	
}
