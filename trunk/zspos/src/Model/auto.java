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
	private ResultSet  rs   =null;   //用于检查单证号是否已出单
	
	public  boolean    useable =false;  //对象可用标志
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
			//1.检查客户信息的完整性
			if( Custom.birthday.trim().equals("") || Custom.idno.trim().equals("") || Custom.name.trim().equals("") || Custom.sex.trim().equals("")){
				JOptionPane.showMessageDialog( null, "客户信息不完整！");
				return "0";
			}
			
			//2.检查对象可用性:数据库对象，打印对象，xml文档对象
			if( !(jdbcob.useable && printob.useable && domob.useable) ){
				if(jdbcob.useable==false){
					JOptionPane.showMessageDialog( null, "本地数据库连接出错！");
				}
				if(printob.useable==false){
					JOptionPane.showMessageDialog( null, "打印机连接出错，请检查线路或端口，并重启！");
				}
				if(domob.useable==false){
					JOptionPane.showMessageDialog( null, "系统配置文档不可用！");
				}
				//useable=false;
				return "0";
			}
			
			//3.检查单证号是否在本批次的范围内
			Sysvar.sellformnum=domob.readFirst("/sysvar/sellformnum");
			Sysvar.policynum=domob.readFirst("/sysvar/policynum");
			
			BigInteger sellformnum      = new BigInteger(Sysvar.sellformnum);      //单证号维护
			BigInteger startsellformnum = new BigInteger(Sysvar.startsellformnum); //开始单证号
			BigInteger endsellformnum   = new BigInteger(Sysvar.endsellformnum);   //结束单证号
			if ( sellformnum.compareTo(startsellformnum)==-1  || sellformnum.compareTo(endsellformnum)==1 ){
				JOptionPane.showMessageDialog( null,Sysvar.pad2sellformnum(sellformnum).trim()+":该单证号超过本批次单证范围！");
				return "0";
			}
			
			//4.检查单证号是否已出过单
			String query="select * from insured where policynum='"+Sysvar.pad2policynum(sellformnum).trim()+"' or sellformno='"+Sysvar.pad2sellformnum(sellformnum).trim()+"'";
			rs = jdbcob.query(query);
			if(rs.next()){
				JOptionPane.showMessageDialog( null, Sysvar.pad2sellformnum(sellformnum).trim()+":该单证号或保单号已使用过！");
				return "0";
			}
			
			//5.检查70天内客户不能重复出单
			if(Sysvar.idnorule.equals("yes")){
				String query1="select * from insured where customid='"+Custom.idno.trim()+"' and cease='0' and datediff(sysdate(),printdate)<="+Sysvar.interval.trim();
				rs = jdbcob.query(query1);
				if(rs.next()){
					JOptionPane.showMessageDialog( null,"该客户在70天内已投保过，不可再投保！");
					return "0";
				}
			}
			
//			//6.检查打印机是否有效
//			if(!comob.checkcom()){
//				JOptionPane.showMessageDialog( null,"打印机不可用,请检查线路！");
//				return "0";
//			}
			
			//6.业务操作:将客户承保数据写入数据库
			String sql=" insert into insured (sellformno,policynum,customid,name,birthday,sex,posid,riskcode,userid,plancode,outletid,printdate,printtime,flag)";
            sql=sql+"values('"+Sysvar.pad2sellformnum(sellformnum).trim()+"','"+Sysvar.pad2policynum(sellformnum).trim()+"','"+Custom.idno.trim()+"','"+Custom.name.trim()+"','"+Custom.birthday.trim()+"','"+Custom.sex.trim()+"','"+Sysvar.poscode.trim()+"','"+Sysvar.riskcode.trim()+"','"+User.userid.trim()+"','"+Sysvar.plancode.trim()+"','"+User.outlet.trim()+"',sysdate(),sysdate(),'1'); ";
            int succ=jdbcob.exec(sql);
            if (succ==1){
                BigInteger temp =sellformnum;
                sellformnum=sellformnum.add(new BigInteger("1"));
            	//8.打印单证
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
            		//打印失败删除记录
            		String sql_delete="delete from insured where policynum='"+Sysvar.pad2policynum(temp).trim()+"' and sellformno='"+Sysvar.pad2sellformnum(temp).trim()+"' and customid='"+Custom.idno.trim()+"' and datediff(sysdate(),printdate)=0";
            		jdbcob.exec(sql_delete);
            		JOptionPane.showMessageDialog( null,"打印失败,请检查线路！");
        			return "0";
            	}
            }
        	return "0";
		}catch(Exception ex){
			JOptionPane.showMessageDialog( null, "【承保异常,请重启程序】");
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
////---------------------------------业务操作------------------------------------
//			//1.检查客户信息的完整性
//			if( !Custom.birthday.trim().equals("") && !Custom.idno.trim().equals("") && !Custom.name.trim().equals("") &&!Custom.sex.trim().equals("")){
//				//2.检查对象可用性:数据库对象，打印对象，xml文档对象
//				if(jdbcob.useable && comob.useable && domob.useable){
//
//					Sysvar.policynum=domob.readFirst("/sysvar/sellformnum");
////					Sysvar.plancode=domob.readFirst("/sysvar/plancode");
//					
//					
//					BigInteger sellformnum      = new BigInteger(Sysvar.policynum);      //保单号维护
//					BigInteger startsellformnum = new BigInteger(Sysvar.startsellformnum); //开始单证号
//					BigInteger endsellformnum   = new BigInteger(Sysvar.endsellformnum);   //结束单证号
//					
//					
//					//3.检查单证号是否在本批次的范围内
//					if ( sellformnum.compareTo(startsellformnum)==0 || sellformnum.compareTo(endsellformnum)==0 || (sellformnum.compareTo(startsellformnum)==1 && sellformnum.compareTo(endsellformnum)==-1) ){
//						String query="select * from insured where policynum='"+Sysvar.pad2policynum(sellformnum).trim()+"' or sellformnum='"+Sysvar.pad2sellformnum(sellformnum).trim()+"'";
//						rs = jdbcob.query(query);
//						//4.检查单证号是否已出过单
//						if(!rs.next()){
//							//5.检查打印机是否有效
//							if(comob.checkcom()){
//								String query1="select * from insured where idno='"+Custom.idno.trim()+"' and datediff(sysdate(),printdate)<=70";
//								rs = jdbcob.query(query1);
//								//6.检查70天内客户不能重复出单
//								if(!rs.next()){
//									
//									//7.业务操作:将客户承保数据写入数据库
//									String sql=" insert into insured (sellformnum,policynum,idno,name,birthday,sex,posid,riskcode,userid,plancode,outletid,printdate,printtime)";
//				                    sql=sql+"values('"+Sysvar.pad2sellformnum(sellformnum).trim()+"','"+Sysvar.pad2policynum(sellformnum).trim()+"','"+Custom.idno.trim()+"','"+Custom.name.trim()+"','"+Custom.birthday.trim()+"','"+Custom.sex.trim()+"','"+Sysvar.poscode.trim()+"','"+Sysvar.riskcode.trim()+"','"+User.userid.trim()+"','"+Sysvar.plancode.trim()+"','"+User.outlet.trim()+"',sysdate(),sysdate()); ";
//				                    
//				                    int succ=jdbcob.exec(sql);
//				                    if (succ==1){
//				                    	
//					                    BigInteger temp =sellformnum;
//					                    sellformnum=sellformnum.add(new BigInteger("1"));
//				                    	//8.打印单证
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
//				                    		//打印失败删除记录
//				                    		String sql_delete="delete from insured where policynum='"+Sysvar.pad2policynum(temp).trim()+"' and sellformnum='"+Sysvar.pad2sellformnum(temp).trim()+"' and idno='"+Custom.idno.trim()+"' and datediff(sysdate(),printdate)=0";
//				                    		jdbcob.exec(sql_delete);
//				                    		JOptionPane.showMessageDialog( null,"打印失败！");
//				                    	}
//				                    	
//				                    }else{
//				                    	JOptionPane.showMessageDialog( null,"数据保存失败！");
//				                    }	
//								}else{
//									JOptionPane.showMessageDialog( null,"该客户在70天内已投保过，不可再投保！");
//								}
//							}else{
//								JOptionPane.showMessageDialog( null,"打印机不可用！");
//							}
//						}else{
//							JOptionPane.showMessageDialog( null, Sysvar.pad2sellformnum(sellformnum).trim()+":该单证号或保单保已使用过！");
//						}
//					}else{
//						JOptionPane.showMessageDialog( null, Sysvar.pad2sellformnum(sellformnum).trim()+":该单证号超过本批次单证范围！");
//					}
//				}else{
//					useable=false;
//					JOptionPane.showMessageDialog( null, "系统出错，请重新启动程序！");
//				}
//			}else{
//				JOptionPane.showMessageDialog( null, "客户信息校验不通过！");
//			}
//			
////---------------------------------业务操作------------------------------------
//			//Lock.Syslock.writeLock().unlock();
//			return "0";
//		}catch(Exception ex){
//			System.out.println("承保异常！");
//			//Lock.Syslock.writeLock().unlock();
//			useable=false;
//			return "0";
//		}
//
//	}

//	//单证号码格式填充
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
