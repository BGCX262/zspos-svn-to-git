package pos.background.thread;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.Document;

import pos.soap.services.soapRefund;
import pos.soap.services.soapSave;
import pos.soap.services.soapSearch;
import pos.services.Dom4j;
import pos.services.jdbc;
import pos.util.dateformat;

public class datains implements Runnable {

	Dom4j        domSave   = null;
	Dom4j        domRefund = null;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			jdbc         jdbcob   = new jdbc("local");           //数据库对象
			soapSave     saveob   = new soapSave();              //承保对象
			soapRefund   refundob = new soapRefund();            //退保对象
			soapSearch   searchob = new soapSearch();            //保单查询对象
			
			domSave  = new Dom4j("xml/soap/save.xml");                    //承保读写xml对象
			domRefund= new Dom4j("xml/soap/refund.xml");                  //退保读写xml对象
			
			while( jdbcob.useable && saveob.useable && refundob.useable && searchob.useable  ){
				Thread.sleep(1*30*1000);   //休眠1分钟
				ResultSet rs=null;
				
				
				//【承保处理】
				String sql_save   ="select * from insured where datediff(sysdate(),printdate)<=90 and insureflag='0' ";
				rs = jdbcob.query(sql_save);
				while(rs.next()){	
					Document  doc = rs2savexml(rs);
					String sellformnum = doc.selectSingleNode("transdata/inputdata/sellformno").getText().trim();
					String policynum   = doc.selectSingleNode("transdata/inputdata/policynum").getText().trim();
					
					//System.out.println(doc.asXML());
					//SQL:承保成功，更新本地数据库
					String sql_update=      " update insured set insuredate=sysdate() ,insuretime=sysdate() , insureflag='1' ,flag='2' ,backup='0' ";  //flag状态只用于显示，不用于实际操作
					sql_update=sql_update + " where sellformno='"+sellformnum+"' and policynum='"+policynum+"' and datediff(sysdate(),printdate)<=90 and insureflag='0'";
					
					if(  saveob.save(doc)==true  ){//送往实达后台做承保
						jdbcob.exec(sql_update);
					}else{//承保失败处理
						if( searchob.search(policynum)==true ){//检查是否是报文丢失
							jdbcob.exec(sql_update);
						}else{//以其他证件类型再承保一次
							doc.selectSingleNode("//insuredzjtype").setText("8");
							doc.selectSingleNode("//applicantzjtype").setText("8");
							if( saveob.save(doc)==true ){
								jdbcob.exec(sql_update);
							}else{//还是失败，发回广分一条日志记录
								
							}
						}
					}
				}
				System.out.println("【本轮数据承保已完成,1分钟后将进入下一轮数据承保】");
				
				//【退保处理】
				String sql_refund="select * from insured where datediff(sysdate(),printdate)<=90 and insureflag='1' and cease='2' ";
				rs = jdbcob.query(sql_refund);
				while(rs.next()){	
					Document  doc = rs2refundxml(rs);
					String sellformnum = doc.selectSingleNode("transdata/inputdata/sellformno").getText().trim();
					String policynum   = doc.selectSingleNode("transdata/inputdata/policynum").getText().trim();
					
					String sql_upd =    "update insured set ceasedate=sysdate(),ceasetime=sysdate(),cease='1',flag='4' ,backup='0' ";
					sql_upd = sql_upd + "where sellformno="+sellformnum+" and policynum='"+policynum+"' and  datediff(sysdate(),printdate)<=90 and insureflag='1' and cease='2' ";
					if( refundob.refund(doc)==true ){
						//System.out.println(sql_upd);
						jdbcob.exec(sql_upd);
					}
				}
				System.out.println("【本轮退保已完成,1分钟后将进入下一轮退保】");
				
				
			}
			System.out.println("【数据承保服务遇到错误，该服务已关闭】");
		} catch (Exception ex) {
			System.out.println("【数据承保服务遇到异常，该服务已关闭】" + ex.getMessage());
		}

	}

	private Document rs2savexml(ResultSet rs) throws SQLException{
		
        //保存业务参数
        domSave.write("//policynum",rs.getString("policynum") );
        domSave.write("//sellformno",rs.getString("sellformno") );
        //保存客户信息
        domSave.write("//applicantname", rs.getString("name"));
        domSave.write("//applicantzjnum", rs.getString("customid"));
        domSave.write("//applicantbirthday", rs.getString("birthday"));
        domSave.write("//applicantsex", rs.getString("sex"));
        domSave.write("//insuredname", rs.getString("name"));
        domSave.write("//insuredzjnum", rs.getString("customid"));
        domSave.write("//bbrdate", rs.getString("birthday"));
        domSave.write("//insuredsex", rs.getString("sex"));
		//保存业务发生时间
        domSave.write("//issueddate", rs.getString("printdate"));//签单日期
        domSave.write("//issuedtime", rs.getString("printtime"));//签单时间
        domSave.write("//inputdate", rs.getString("printdate")+" "+rs.getString("printtime"));//录单时间
        //证件类型的控制
        String idno=rs.getString("customid");
        if(idno.trim().length()==18){
        	domSave.write("//insuredzjtype", "0");
        	domSave.write("//applicantzjtype", "0");
        }else{
        	domSave.write("//insuredzjtype", "8");
        	domSave.write("//applicantzjtype", "8");
        }
        //操作员（用户）
        domSave.write("//operatorcode", rs.getString("userid"));
        domSave.write("//opeartorname", "");
        //不同方案，不同的处理，templet默认方案使用save.xml默认值
        String plancode=rs.getString("plancode").trim();
        //佛山乘意险
        if(plancode.equals("three")){
        	domSave.write("//plancode", "66331016");
        	domSave.write("//summoney", "3");
        	domSave.write("//amount",   "30000");
            domSave.write("//startdate", rs.getString("printdate")+" "+rs.getString("printtime"));//保单起期
            domSave.write("//enddate",   rs.getString("printdate")+" "+"23:59:59");//保单止期
        }else{
        	if(plancode.equals("four")){
            	domSave.write("//plancode", "66331015");
            	domSave.write("//summoney", "4");
            	domSave.write("//amount",   "40000");
                domSave.write("//startdate", rs.getString("printdate")+" "+rs.getString("printtime"));//保单起期
                domSave.write("//enddate",   rs.getString("printdate")+" "+"23:59:59");//保单止期
        	}else{
        		if(plancode.equals("five")){
                	domSave.write("//plancode", "66331014");
                	domSave.write("//summoney", "5");
                	domSave.write("//amount",   "50000");
                    domSave.write("//startdate", rs.getString("printdate")+" "+rs.getString("printtime"));//保单起期
                    domSave.write("//enddate",   rs.getString("printdate")+" "+"23:59:59");//保单止期
        		}
        	}
        }
        //中山计生险
        if(plancode.equals("Native")){
        	domSave.write("//plancode", "66339002"); //本地人口
        	domSave.write("//summoney", "5");
        	domSave.write("//amount",   "465000");
            domSave.write("//startdate", dateformat.getAfterDate(rs.getString("printdate"),  1)+"  00:00:01");//保单起期
            domSave.write("//enddate",   dateformat.getAfterDate(rs.getString("printdate"), 91)+"  23:59:59");//保单止期
        }else{
        	if(plancode.equals("Foreigner")){
            	domSave.write("//plancode", "66339001");//流动人口
            	domSave.write("//summoney", "5");
            	domSave.write("//amount",   "462000");
                domSave.write("//startdate", dateformat.getAfterDate(rs.getString("printdate"),  1)+"  00:00:01");//保单起期
                domSave.write("//enddate",   dateformat.getAfterDate(rs.getString("printdate"), 91)+"  23:59:59");//保单止期
        	}
        }
		return domSave.getDocument();
		
	}
	
	private Document rs2refundxml(ResultSet rs) throws SQLException{
        //保存业务参数
        domRefund.write("//policynum" ,rs.getString("policynum") );
        domRefund.write("//sellformno",rs.getString("sellformno") );
        //保存客户信息
        domRefund.write("//applicantname", rs.getString("name"));
        return domRefund.getDocument();
	}
	
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		datains  t =new datains ();
//		new Thread(t).start();
//	}

}
