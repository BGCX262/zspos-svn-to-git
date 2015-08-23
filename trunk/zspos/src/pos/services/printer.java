package pos.services;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.swing.JOptionPane;

import org.dom4j.Element;

import Beans.*;
import pos.services.Dom4j;
public class printer {
	
	private CommPortIdentifier portId =null;
	private Dom4j              domob  =null;
	public  boolean            useable=false;
	
	public printer(){
		domob=new Dom4j("xml/printer/prtstyle.xml");
		if(domob.useable){
			String comname=domob.readFirst("/pagestyle/destinationport").trim();
			Enumeration        portList = CommPortIdentifier.getPortIdentifiers();
			CommPortIdentifier portId1   = null;
			while (portList.hasMoreElements()) {
				portId1 = (CommPortIdentifier) portList.nextElement();
				//System.out.println("探测到端口:"+portId1.getName());
				if( portId1.getPortType() == CommPortIdentifier.PORT_SERIAL && portId1.getName().equals(comname.toUpperCase().trim())) {
					portId=portId1;
					useable=true;
					System.out.println("【添加打印端口】" + portId1.getName());
				}
			}
			if(useable==false){
				JOptionPane.showMessageDialog( null, "打印机初始化失败，请检查线路或端口！");
			}
		}
	}
	
	public boolean checkcom(){
		try{
			SerialPort   serialPort   = (SerialPort) portId.open("PosApp",2000);
			OutputStream outputStream = serialPort.getOutputStream();
			InputStream  inputStream  = serialPort.getInputStream();
			serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8, SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
			
			//检查打印机状态
			String probecode="\u0010"+"\u0004"+"\u0001";
			outputStream.write( probecode.getBytes() );
			//System.out.println("发送探测报文:"+probecode);
			Thread.sleep(20);
			
			byte[] readBuffer = new byte[1];
			if(inputStream.available()>0) {
				inputStream.read(readBuffer);
				String flag= new String(readBuffer);
				//System.out.println("返回探测代码:"+flag);
				if(flag.equals("")){
					serialPort.close();
					useable=true;
					System.out.println("打印机正常！");
					return true;
				}else{
					serialPort.close();
					//useable=false;
					System.out.println("打印机不可打印！");
					return false;
				}
			}else{
				//System.out.println("发送探测报文没返回！");
				return false;
			}

		}catch(Exception ex){
			useable=false;
			//System.out.println("打开打印机异常！");
			return false;
		}
	}
	
	
	private String getStyleID(){
		if(useable){
		 	String printertype=Ptr.sysvar.printertype.trim();
			String riskcode=Ptr.sysvar.riskcode.trim();
			String sellformtype=Ptr.sysvar.sellformtype.trim();
			String plans=Ptr.sysvar.plancode.trim();
			
			String xpath="/pagestyle/printer[@type='"+printertype+"']/riskcode[@id='"+riskcode+"']/sellformtype[@type='"+sellformtype+"']/plans[@code='"+plans+"']";
			String id= domob.readFirst(xpath);
			
			//System.out.println(xpath);
			System.out.println("读到打印格式代码:"+id);
			return id;
		}else{
			return "";
		}
	}
	public  boolean  write() {
	
		String PrtString="";
		try{
			//Lock.Syslock.readLock().lock();
			//检查打印机是否可打印，是否取得打印格式
			boolean  chk=checkcom();
			String   styleid=getStyleID();
			if(styleid.equals("") || useable==false || chk==false){
				return false;
			}
			//初始化remark数据
			Ptr.remark1=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark1");
			Ptr.remark2=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark2");
			Ptr.remark3=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark3");
			Ptr.remark4=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark4");
			Ptr.remark5=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark5");
			Ptr.remark6=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark6");
			Ptr.remark7=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark7");
			Ptr.remark8=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark8");
			Ptr.remark9=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark9");
			Ptr.remark10=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark10");
			Ptr.remark11=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark11");
			Ptr.remark12=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark12");
			Ptr.remark13=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark13");
			Ptr.remark14=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark14");
			Ptr.remark15=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark15");
			Ptr.remark16=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark16");
			Ptr.remark17=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark17");
			Ptr.remark18=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark18");
			Ptr.remark19=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark19");
			Ptr.remark20=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/defaultitems/remark20");
			//获取数据，构造打印格式
			int      row=0;
			String   xpath="/pagestyle/styles[@id='"+styleid+"']/lines/line";
			Iterator iter=domob.readAll(xpath);
			while(iter.hasNext()) {
				row++;
				//构造行格式
				Element element = (Element) iter.next();
				String  text    = element.getTextTrim().trim();
				if(text.equals("")){
					List     list2 = element.selectNodes("item");
					Iterator iter2 = list2.iterator();
					while (iter2.hasNext()) {
						Element element2 = (Element) iter2.next();
						String text2 = element2.getTextTrim();
						
//						System.out.println(text2); //构造打印格式
						PrtString=PrtString+Ptr.read(text2);
						
					}
					PrtString=PrtString+"\n";
				}else{
					 int i = Integer.parseInt(text);
					 for(int j=1 ;j<=i && i>0 ;j++){
//						 System.out.println("空行"); //构造打印格式
						 PrtString=PrtString+"\n";
					 }
				}
				
				//行正微调
				String adjust=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/linesadjustplus/adjustwidth[@line='"+row+"']").trim();
				String hexstr=toStringHex(adjust.trim());
				if( hexstr!=null ){
					PrtString=PrtString+"\u001B"+"J"+hexstr;
				}
				//行负微调
				adjust=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/linesadjustminus/adjustwidth[@line='"+row+"']").trim();
				hexstr=toStringHex(adjust.trim());
				if( hexstr!=null ){
					PrtString=PrtString+"\u001B"+"K"+hexstr;
				}

			}

			//页尾微调
			String adjust1=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/pageadjust").trim();
			String hexstr=toStringHex(adjust1.trim());
			if( hexstr!=null ){
				PrtString=PrtString+"\u001B"+"J"+hexstr;
			}
			//页尾走行
			adjust1=domob.readFirst("/pagestyle/styles[@id='"+styleid+"']/skiplines");
			hexstr=toStringHex(adjust1.trim());
			if( hexstr!=null ){
				PrtString=PrtString+"\u001B"+"d"+hexstr;
			}
			
			//Lock.Syslock.readLock().unlock();
			
			
			System.out.println(PrtString);
			//最后打印
			SerialPort   serialPort   = (SerialPort) portId.open("PosApp",2000);
			OutputStream outputStream = serialPort.getOutputStream();
			InputStream  inputStream  = serialPort.getInputStream();
			serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8, SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
			outputStream.write( PrtString.getBytes() );
			serialPort.close();
			return true;
		}catch(Exception ex){
			//Lock.Syslock.readLock().unlock();
			return false;
		}
	}
	private String toStringHex(String s) {

		if(s.trim().equals("")){
			return null;
		}
		
		if ("0x".equals(s.substring(0, 2))) {
			s = s.substring(2);
		}
		byte[] baKeyword = new byte[s.length() / 2]; // 取字符串长度的一半
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
						i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
		return s;
	}
}
