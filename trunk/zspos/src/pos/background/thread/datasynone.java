package pos.background.thread;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import Beans.Lock;
import pos.mysoap.services.mysoapSearch;
import pos.mysoap.services.mysoapSyn;
import pos.mysoap.services.mysoapUpdate;
import pos.services.jdbc;

public class datasynone implements Runnable {
	private Document  document=null;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			jdbc         jdbcob    = new jdbc("local");         //���ݿ����
			mysoapSyn    synob     = new mysoapSyn();           //webservice��������ͬ���ӿ�
			mysoapSearch searchob  = new mysoapSearch();        //webservice������ѯ�ӿ�
			mysoapUpdate updateob  = new mysoapUpdate();        //webservice����״̬���½ӿ�
			
			SAXReader    saxReader = new SAXReader();
			document     = saxReader.read("xml/mysoap/save.xml");  //�ӿڵĲ�����ʽ
			
			if(jdbcob.useable && synob.useable && searchob.useable && updateob.useable){
				
				try{
					Lock.Synlock.writeLock().lock();
					//��ѭ��������ͬ��������
					String sql_query =" select * from insured where datediff(sysdate(),printdate)<=90 and backup!='1' ";  //backup ='0'��ʾ���ݻ�δ��ͬ��,--->'1'��ʾ������ͬ��<----,'2'��ʾ״̬��δ��ͬ��
					ResultSet rs=jdbcob.query(sql_query);   //��ѯ�Ƿ��гб�����Ҫ����
					while(rs.next()){
						Document  doc=format(rs);
						String sellformnum = doc.selectSingleNode("transdata/inputdata/sellformnum").getText().trim();
						String policynum   = doc.selectSingleNode("transdata/inputdata/policynum").getText().trim();
						//SQL:���ݳɹ������±������ݿ�
						String sql_update=" update insured set backupdate=sysdate() ,backuptime=sysdate() , backup='1' ";
						sql_update=sql_update+" where sellformno='"+sellformnum+"' and policynum='"+policynum+"' and datediff(sysdate(),printdate)<=90 and backup!='1' ";
						
						//��ͬ��������
						if( synob.syn(doc)==true ){//�������ɹ����汣��
							jdbcob.exec(sql_update);
						}
					}
					Lock.Synlock.writeLock().unlock();
				}catch(Exception ex){
					Lock.Synlock.writeLock().unlock();
				}
				JOptionPane.showMessageDialog( null, "������ͬ��������");
			}else{
				JOptionPane.showMessageDialog( null, "������ͬ��ʧ�ܡ�");
			}
		}catch(Exception ex){
			System.out.println("������ͬ�����������쳣���÷����ѹرա�"+ex.getMessage());
			JOptionPane.showMessageDialog( null, "������ͬ�����������쳣���÷����ѹرա�");
		}
	}
	private Document format(ResultSet rs) throws SQLException{
		//��ȡ���ݿ�ļ�¼
		String  sellformno  =rs.getString("sellformno");
		String  policynum   =rs.getString("policynum");
		String  customid    =rs.getString("customid");
		String  name        =rs.getString("name");
		String  birthday    =rs.getString("birthday");
		String  sex         =rs.getString("sex");
		String  posid       =rs.getString("posid");
		String  riskcode    =rs.getString("riskcode");
		String  userid      =rs.getString("userid");
		String  plancode    =rs.getString("plancode");
		String  outletid    =rs.getString("outletid");
		String  printdate   =rs.getString("printdate");
		String  printtime   =rs.getString("printtime");
		String  printflag   =rs.getString("printflag");
		String  insuredate  =rs.getString("insuredate");
		String  insuretime  =rs.getString("insuretime");
		String  insureflag  =rs.getString("insureflag");
		String  ceasedate   =rs.getString("ceasedate");
		String  ceasetime   =rs.getString("ceasetime");
		String  cease       =rs.getString("cease");
		String  backupdate  =rs.getString("backupdate");
		String  backuptime  =rs.getString("backuptime");
		String  backup      =rs.getString("backup");
		String  flag        =rs.getString("flag");
		
		Element root = document.getRootElement();
		Element inputdata = root.element("inputdata");
		//���document�ĵ�
		inputdata.element("sellformnum").clearContent();
		inputdata.element("policynum").clearContent();
		inputdata.element("customid").clearContent();
		inputdata.element("name").clearContent();
		inputdata.element("birthday").clearContent();
		inputdata.element("sex").clearContent();
		inputdata.element("outletid").clearContent();
		inputdata.element("userid").clearContent();
		inputdata.element("posid").clearContent();
		inputdata.element("riskcode").clearContent();
		inputdata.element("plancode").clearContent();
		
		inputdata.element("printdate").clearContent();
		inputdata.element("printtime").clearContent();
		inputdata.element("printflag").clearContent();
		
		inputdata.element("insuredate").clearContent();
		inputdata.element("insuretime").clearContent();
		inputdata.element("insureflag").clearContent();
		
		inputdata.element("ceasedate").clearContent();
		inputdata.element("ceasetime").clearContent();
		inputdata.element("ceaseflag").clearContent();
		
		inputdata.element("backupdate").clearContent();
		inputdata.element("backuptime").clearContent();
		inputdata.element("backupflag").clearContent();
		
		inputdata.element("finalflag").clearContent();
		
		//��ֵ��document�ĵ�
		if( sellformno!=null){
			inputdata.element("sellformnum").setText( sellformno );
		}
		if( policynum!=null){
			inputdata.element("policynum").setText( policynum );
		}
		if( customid!=null){
			inputdata.element("customid").setText( customid );
		}
		if( name!=null){
			inputdata.element("name").setText( name );
		}
		if( birthday!=null){
			inputdata.element("birthday").setText( birthday );
		}
		if( sex!=null){
			inputdata.element("sex").setText( sex );
		}
		if( outletid!=null){
			inputdata.element("outletid").setText( outletid );
		}
		if( userid!=null){
			inputdata.element("userid").setText( userid );
		}
		if( posid!=null){
			inputdata.element("posid").setText( posid );
		}
		if( riskcode!=null){
			inputdata.element("riskcode").setText( riskcode );
		}
		if( plancode!=null){
			inputdata.element("plancode").setText( plancode );
		}
		if( printdate!=null){
			inputdata.element("printdate").setText( printdate );
		}
		if( printtime!=null){
			inputdata.element("printtime").setText( printtime );
		}
		if( printflag!=null){
			inputdata.element("printflag").setText( printflag );
		}
		if(insuredate!=null){
			inputdata.element("insuredate").setText( insuredate );
		}
		if( insuretime!=null){
			inputdata.element("insuretime").setText( insuretime );
		}
		if( insureflag!=null){
			inputdata.element("insureflag").setText( insureflag );
		}
		if( ceasedate!=null){
			inputdata.element("ceasedate").setText( ceasedate );
		}
		if( ceasetime!=null){
			inputdata.element("ceasetime").setText( ceasetime );
		}
		if( cease!=null){
			inputdata.element("ceaseflag").setText( cease );
		}
		if( backupdate!=null){
			inputdata.element("backupdate").setText( backupdate );
		}
		if( backuptime!=null){
			inputdata.element("backuptime").setText( backuptime );
		}
		if( backup!=null){
			inputdata.element("backupflag").setText( backup );
		}
		if( flag!=null){
			inputdata.element("finalflag").setText( flag );
		}
		//����document�ĵ�
		return document;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		datasynone  t =new datasynone ();
		new Thread(t).start();
	}

}
