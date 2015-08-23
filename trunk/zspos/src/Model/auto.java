package Model;

import java.math.BigInteger;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import Beans.Custom;
import Beans.Sysvar;
import Beans.User;
import pos.services.Dom4j;
import pos.services.jdbc;
import pos.services.printer;

public class auto {
	private jdbc       jdbcob=null;
	private Dom4j      domob=null;
	private printer    printob=null;
	private ResultSet  rs   =null;   //���ڼ�鵥֤���Ƿ��ѳ���
	
	public  boolean    useable =false;  //������ñ�־
	/**
	 * @param args
	 */
	public auto(Dom4j domob){
		try{
			this.domob=domob;
			jdbcob =new jdbc("local");
			printob=new printer();
			
	        useable=true;
		}catch(Exception ex){
	        useable=false;
		}
	}
	public String go1(){
		try{
			if(useable==false){
				return "0";
			}
			//1.���ͻ���Ϣ��������
			if( Custom.birthday.trim().equals("") || Custom.idno.trim().equals("") || Custom.name.trim().equals("") || Custom.sex.trim().equals("")){
				JOptionPane.showMessageDialog( null, "�ͻ���Ϣ��������");
				return "0";
			}
			
			//2.�����������:���ݿ���󣬴�ӡ����xml�ĵ�����
			if( !(jdbcob.useable && printob.useable && domob.useable) ){
				if(jdbcob.useable==false){
					JOptionPane.showMessageDialog( null, "�������ݿ����ӳ���");
				}
				if(printob.useable==false){
					JOptionPane.showMessageDialog( null, "��ӡ�����ӳ���������·��˿ڣ���������");
				}
				if(domob.useable==false){
					JOptionPane.showMessageDialog( null, "ϵͳ�����ĵ������ã�");
				}
				//useable=false;
				return "0";
			}
			
			//3.��鵥֤���Ƿ��ڱ����εķ�Χ��
			Sysvar.sellformnum=domob.readFirst("/sysvar/sellformnum");
			Sysvar.policynum=domob.readFirst("/sysvar/policynum");
			
			BigInteger sellformnum      = new BigInteger(Sysvar.sellformnum);      //��֤��ά��
			BigInteger startsellformnum = new BigInteger(Sysvar.startsellformnum); //��ʼ��֤��
			BigInteger endsellformnum   = new BigInteger(Sysvar.endsellformnum);   //������֤��
			if ( sellformnum.compareTo(startsellformnum)==-1  || sellformnum.compareTo(endsellformnum)==1 ){
				JOptionPane.showMessageDialog( null,Sysvar.pad2sellformnum(sellformnum).trim()+":�õ�֤�ų��������ε�֤��Χ��");
				return "0";
			}
			
			//4.��鵥֤���Ƿ��ѳ�����
			String query="select * from insured where policynum='"+Sysvar.pad2policynum(sellformnum).trim()+"' or sellformno='"+Sysvar.pad2sellformnum(sellformnum).trim()+"'";
			rs = jdbcob.query(query);
			if(rs.next()){
				JOptionPane.showMessageDialog( null, Sysvar.pad2sellformnum(sellformnum).trim()+":�õ�֤�Ż򱣵�����ʹ�ù���");
				return "0";
			}
			
			//5.���70���ڿͻ������ظ�����
			if(Sysvar.idnorule.equals("yes")){
				String query1="select * from insured where customid='"+Custom.idno.trim()+"' and cease='0' and datediff(sysdate(),printdate)<="+Sysvar.interval.trim();
				rs = jdbcob.query(query1);
				if(rs.next()){
					JOptionPane.showMessageDialog( null,"�ÿͻ���70������Ͷ������������Ͷ����");
					return "0";
				}
			}
			
//			//6.����ӡ���Ƿ���Ч
//			if(!comob.checkcom()){
//				JOptionPane.showMessageDialog( null,"��ӡ��������,������·��");
//				return "0";
//			}
			
			//6.ҵ�����:���ͻ��б�����д�����ݿ�
			String sql=" insert into insured (sellformno,policynum,customid,name,birthday,sex,posid,riskcode,userid,plancode,outletid,printdate,printtime,flag)";
            sql=sql+"values('"+Sysvar.pad2sellformnum(sellformnum).trim()+"','"+Sysvar.pad2policynum(sellformnum).trim()+"','"+Custom.idno.trim()+"','"+Custom.name.trim()+"','"+Custom.birthday.trim()+"','"+Custom.sex.trim()+"','"+Sysvar.poscode.trim()+"','"+Sysvar.riskcode.trim()+"','"+User.userid.trim()+"','"+Sysvar.plancode.trim()+"','"+User.outlet.trim()+"',sysdate(),sysdate(),'1'); ";
            int succ=jdbcob.exec(sql);
            if (succ==1){
                BigInteger temp =sellformnum;
                sellformnum=sellformnum.add(new BigInteger("1"));
            	//8.��ӡ��֤
            	if(printob.write()){
					domob.write("/sysvar/sellformnum", Sysvar.pad2sellformnum(sellformnum).trim() );
					domob.write("/sysvar/policynum", Sysvar.pad2policynum(sellformnum).trim());
					
					domob.write("/sysvar/lastpolicy/policynum",Sysvar.pad2policynum(temp).trim() );
					domob.write("/sysvar/lastpolicy/sellformnum", Sysvar.pad2sellformnum(temp).trim());
					domob.write("/sysvar/lastpolicy/applicantname", Custom.name.trim() );
					domob.write("/sysvar/lastpolicy/applicantzjnum", Custom.idno.trim() );
					domob.write("/sysvar/lastpolicy/applicantsex", Custom.sex.trim() );
					domob.write("/sysvar/lastpolicy/applicantbirthday", Custom.birthday.trim() );
					
					domob.write("/sysvar/lastpolicy/riskcode", Sysvar.riskcode.trim() );
					domob.write("/sysvar/lastpolicy/plancode", Sysvar.plancode.trim() );
					domob.write("/sysvar/lastpolicy/outlets", User.outlet.trim() );
					
					domob.save();
					
					return Sysvar.pad2policynum(temp).trim();
					
            	}else{
            		//��ӡʧ��ɾ����¼
            		String sql_delete="delete from insured where policynum='"+Sysvar.pad2policynum(temp).trim()+"' and sellformno='"+Sysvar.pad2sellformnum(temp).trim()+"' and customid='"+Custom.idno.trim()+"' and datediff(sysdate(),printdate)=0";
            		jdbcob.exec(sql_delete);
            		JOptionPane.showMessageDialog( null,"��ӡʧ��,������·��");
        			return "0";
            	}
            }
        	return "0";
		}catch(Exception ex){
			JOptionPane.showMessageDialog( null, "���б��쳣,����������");
			useable=false;
			return "0";
		}
	}
	
	
//	public String go(){
//		if(useable==false){
//			return "0";
//		}
//		
//		try{
//			//Lock.Syslock.writeLock().lock();
////---------------------------------ҵ�����------------------------------------
//			//1.���ͻ���Ϣ��������
//			if( !Custom.birthday.trim().equals("") && !Custom.idno.trim().equals("") && !Custom.name.trim().equals("") &&!Custom.sex.trim().equals("")){
//				//2.�����������:���ݿ���󣬴�ӡ����xml�ĵ�����
//				if(jdbcob.useable && comob.useable && domob.useable){
//
//					Sysvar.policynum=domob.readFirst("/sysvar/sellformnum");
////					Sysvar.plancode=domob.readFirst("/sysvar/plancode");
//					
//					
//					BigInteger sellformnum      = new BigInteger(Sysvar.policynum);      //������ά��
//					BigInteger startsellformnum = new BigInteger(Sysvar.startsellformnum); //��ʼ��֤��
//					BigInteger endsellformnum   = new BigInteger(Sysvar.endsellformnum);   //������֤��
//					
//					
//					//3.��鵥֤���Ƿ��ڱ����εķ�Χ��
//					if ( sellformnum.compareTo(startsellformnum)==0 || sellformnum.compareTo(endsellformnum)==0 || (sellformnum.compareTo(startsellformnum)==1 && sellformnum.compareTo(endsellformnum)==-1) ){
//						String query="select * from insured where policynum='"+Sysvar.pad2policynum(sellformnum).trim()+"' or sellformnum='"+Sysvar.pad2sellformnum(sellformnum).trim()+"'";
//						rs = jdbcob.query(query);
//						//4.��鵥֤���Ƿ��ѳ�����
//						if(!rs.next()){
//							//5.����ӡ���Ƿ���Ч
//							if(comob.checkcom()){
//								String query1="select * from insured where idno='"+Custom.idno.trim()+"' and datediff(sysdate(),printdate)<=70";
//								rs = jdbcob.query(query1);
//								//6.���70���ڿͻ������ظ�����
//								if(!rs.next()){
//									
//									//7.ҵ�����:���ͻ��б�����д�����ݿ�
//									String sql=" insert into insured (sellformnum,policynum,idno,name,birthday,sex,posid,riskcode,userid,plancode,outletid,printdate,printtime)";
//				                    sql=sql+"values('"+Sysvar.pad2sellformnum(sellformnum).trim()+"','"+Sysvar.pad2policynum(sellformnum).trim()+"','"+Custom.idno.trim()+"','"+Custom.name.trim()+"','"+Custom.birthday.trim()+"','"+Custom.sex.trim()+"','"+Sysvar.poscode.trim()+"','"+Sysvar.riskcode.trim()+"','"+User.userid.trim()+"','"+Sysvar.plancode.trim()+"','"+User.outlet.trim()+"',sysdate(),sysdate()); ";
//				                    
//				                    int succ=jdbcob.exec(sql);
//				                    if (succ==1){
//				                    	
//					                    BigInteger temp =sellformnum;
//					                    sellformnum=sellformnum.add(new BigInteger("1"));
//				                    	//8.��ӡ��֤
//				                    	if(comob.write()){
//					    					domob.write("/sysvar/sellformnum", Sysvar.pad2sellformnum(sellformnum).trim() );
//					    					domob.write("/sysvar/policynum", Sysvar.pad2policynum(sellformnum).trim() );
//					    					
//					    					domob.write("/sysvar/lastpolicy/policynum", Sysvar.pad2policynum(temp).trim() );
//					    					domob.write("/sysvar/lastpolicy/sellformnum", Sysvar.pad2sellformnum(temp).trim() );
//					    					domob.write("/sysvar/lastpolicy/applicantname", Custom.name.trim() );
//					    					domob.write("/sysvar/lastpolicy/applicantzjnum", Custom.idno.trim() );
//					    					domob.write("/sysvar/lastpolicy/applicantsex", Custom.sex.trim() );
//					    					domob.write("/sysvar/lastpolicy/applicantbirthday", Custom.birthday.trim() );
//					    					
//					    					domob.write("/sysvar/lastpolicy/riskcode", Sysvar.riskcode.trim() );
//					    					domob.write("/sysvar/lastpolicy/plancode", Sysvar.plancode.trim() );
//					    					domob.write("/sysvar/lastpolicy/outlets", User.outlet.trim() );
//					    					
//					    					domob.save();
//					    					
//					    					return Sysvar.pad2policynum(temp).trim();
//					    					
//				                    	}else{
//				                    		//��ӡʧ��ɾ����¼
//				                    		String sql_delete="delete from insured where policynum='"+Sysvar.pad2policynum(temp).trim()+"' and sellformnum='"+Sysvar.pad2sellformnum(temp).trim()+"' and idno='"+Custom.idno.trim()+"' and datediff(sysdate(),printdate)=0";
//				                    		jdbcob.exec(sql_delete);
//				                    		JOptionPane.showMessageDialog( null,"��ӡʧ�ܣ�");
//				                    	}
//				                    	
//				                    }else{
//				                    	JOptionPane.showMessageDialog( null,"���ݱ���ʧ�ܣ�");
//				                    }	
//								}else{
//									JOptionPane.showMessageDialog( null,"�ÿͻ���70������Ͷ������������Ͷ����");
//								}
//							}else{
//								JOptionPane.showMessageDialog( null,"��ӡ�������ã�");
//							}
//						}else{
//							JOptionPane.showMessageDialog( null, Sysvar.pad2sellformnum(sellformnum).trim()+":�õ�֤�Ż򱣵�����ʹ�ù���");
//						}
//					}else{
//						JOptionPane.showMessageDialog( null, Sysvar.pad2sellformnum(sellformnum).trim()+":�õ�֤�ų��������ε�֤��Χ��");
//					}
//				}else{
//					useable=false;
//					JOptionPane.showMessageDialog( null, "ϵͳ������������������");
//				}
//			}else{
//				JOptionPane.showMessageDialog( null, "�ͻ���ϢУ�鲻ͨ����");
//			}
//			
////---------------------------------ҵ�����------------------------------------
//			//Lock.Syslock.writeLock().unlock();
//			return "0";
//		}catch(Exception ex){
//			System.out.println("�б��쳣��");
//			//Lock.Syslock.writeLock().unlock();
//			useable=false;
//			return "0";
//		}
//
//	}

//	//��֤�����ʽ���
//	private String pad1(String args,char ch) {
//		int i=14-args.length();
//		if(i>0){
//			for(int j=1;j<=i;j++){
//				args=ch+args;
//			}
//		}
//		return args ;
//	}
}
