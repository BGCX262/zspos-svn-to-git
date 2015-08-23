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
			jdbc         jdbcob   = new jdbc("local");           //���ݿ����
			soapSave     saveob   = new soapSave();              //�б�����
			soapRefund   refundob = new soapRefund();            //�˱�����
			soapSearch   searchob = new soapSearch();            //������ѯ����
			
			domSave  = new Dom4j("xml/soap/save.xml");                    //�б���дxml����
			domRefund= new Dom4j("xml/soap/refund.xml");                  //�˱���дxml����
			
			while( jdbcob.useable && saveob.useable && refundob.useable && searchob.useable  ){
				Thread.sleep(1*30*1000);   //����1����
				ResultSet rs=null;
				
				
				//���б�����
				String sql_save   ="select * from insured where datediff(sysdate(),printdate)<=90 and insureflag='0' ";
				rs = jdbcob.query(sql_save);
				while(rs.next()){	
					Document  doc = rs2savexml(rs);
					String sellformnum = doc.selectSingleNode("transdata/inputdata/sellformno").getText().trim();
					String policynum   = doc.selectSingleNode("transdata/inputdata/policynum").getText().trim();
					
					//System.out.println(doc.asXML());
					//SQL:�б��ɹ������±������ݿ�
					String sql_update=      " update insured set insuredate=sysdate() ,insuretime=sysdate() , insureflag='1' ,flag='2' ,backup='0' ";  //flag״ֻ̬������ʾ��������ʵ�ʲ���
					sql_update=sql_update + " where sellformno='"+sellformnum+"' and policynum='"+policynum+"' and datediff(sysdate(),printdate)<=90 and insureflag='0'";
					
					if(  saveob.save(doc)==true  ){//����ʵ���̨���б�
						jdbcob.exec(sql_update);
					}else{//�б�ʧ�ܴ���
						if( searchob.search(policynum)==true ){//����Ƿ��Ǳ��Ķ�ʧ
							jdbcob.exec(sql_update);
						}else{//������֤�������ٳб�һ��
							doc.selectSingleNode("//insuredzjtype").setText("8");
							doc.selectSingleNode("//applicantzjtype").setText("8");
							if( saveob.save(doc)==true ){
								jdbcob.exec(sql_update);
							}else{//����ʧ�ܣ����ع��һ����־��¼
								
							}
						}
					}
				}
				System.out.println("���������ݳб������,1���Ӻ󽫽�����һ�����ݳб���");
				
				//���˱�����
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
				System.out.println("�������˱������,1���Ӻ󽫽�����һ���˱���");
				
				
			}
			System.out.println("�����ݳб������������󣬸÷����ѹرա�");
		} catch (Exception ex) {
			System.out.println("�����ݳб����������쳣���÷����ѹرա�" + ex.getMessage());
		}

	}

	private Document rs2savexml(ResultSet rs) throws SQLException{
		
        //����ҵ�����
        domSave.write("//policynum",rs.getString("policynum") );
        domSave.write("//sellformno",rs.getString("sellformno") );
        //����ͻ���Ϣ
        domSave.write("//applicantname", rs.getString("name"));
        domSave.write("//applicantzjnum", rs.getString("customid"));
        domSave.write("//applicantbirthday", rs.getString("birthday"));
        domSave.write("//applicantsex", rs.getString("sex"));
        domSave.write("//insuredname", rs.getString("name"));
        domSave.write("//insuredzjnum", rs.getString("customid"));
        domSave.write("//bbrdate", rs.getString("birthday"));
        domSave.write("//insuredsex", rs.getString("sex"));
		//����ҵ����ʱ��
        domSave.write("//issueddate", rs.getString("printdate"));//ǩ������
        domSave.write("//issuedtime", rs.getString("printtime"));//ǩ��ʱ��
        domSave.write("//inputdate", rs.getString("printdate")+" "+rs.getString("printtime"));//¼��ʱ��
        //֤�����͵Ŀ���
        String idno=rs.getString("customid");
        if(idno.trim().length()==18){
        	domSave.write("//insuredzjtype", "0");
        	domSave.write("//applicantzjtype", "0");
        }else{
        	domSave.write("//insuredzjtype", "8");
        	domSave.write("//applicantzjtype", "8");
        }
        //����Ա���û���
        domSave.write("//operatorcode", rs.getString("userid"));
        domSave.write("//opeartorname", "");
        //��ͬ��������ͬ�Ĵ���templetĬ�Ϸ���ʹ��save.xmlĬ��ֵ
        String plancode=rs.getString("plancode").trim();
        //��ɽ������
        if(plancode.equals("three")){
        	domSave.write("//plancode", "66331016");
        	domSave.write("//summoney", "3");
        	domSave.write("//amount",   "30000");
            domSave.write("//startdate", rs.getString("printdate")+" "+rs.getString("printtime"));//��������
            domSave.write("//enddate",   rs.getString("printdate")+" "+"23:59:59");//����ֹ��
        }else{
        	if(plancode.equals("four")){
            	domSave.write("//plancode", "66331015");
            	domSave.write("//summoney", "4");
            	domSave.write("//amount",   "40000");
                domSave.write("//startdate", rs.getString("printdate")+" "+rs.getString("printtime"));//��������
                domSave.write("//enddate",   rs.getString("printdate")+" "+"23:59:59");//����ֹ��
        	}else{
        		if(plancode.equals("five")){
                	domSave.write("//plancode", "66331014");
                	domSave.write("//summoney", "5");
                	domSave.write("//amount",   "50000");
                    domSave.write("//startdate", rs.getString("printdate")+" "+rs.getString("printtime"));//��������
                    domSave.write("//enddate",   rs.getString("printdate")+" "+"23:59:59");//����ֹ��
        		}
        	}
        }
        //��ɽ������
        if(plancode.equals("Native")){
        	domSave.write("//plancode", "66339002"); //�����˿�
        	domSave.write("//summoney", "5");
        	domSave.write("//amount",   "465000");
            domSave.write("//startdate", dateformat.getAfterDate(rs.getString("printdate"),  1)+"  00:00:01");//��������
            domSave.write("//enddate",   dateformat.getAfterDate(rs.getString("printdate"), 91)+"  23:59:59");//����ֹ��
        }else{
        	if(plancode.equals("Foreigner")){
            	domSave.write("//plancode", "66339001");//�����˿�
            	domSave.write("//summoney", "5");
            	domSave.write("//amount",   "462000");
                domSave.write("//startdate", dateformat.getAfterDate(rs.getString("printdate"),  1)+"  00:00:01");//��������
                domSave.write("//enddate",   dateformat.getAfterDate(rs.getString("printdate"), 91)+"  23:59:59");//����ֹ��
        	}
        }
		return domSave.getDocument();
		
	}
	
	private Document rs2refundxml(ResultSet rs) throws SQLException{
        //����ҵ�����
        domRefund.write("//policynum" ,rs.getString("policynum") );
        domRefund.write("//sellformno",rs.getString("sellformno") );
        //����ͻ���Ϣ
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
