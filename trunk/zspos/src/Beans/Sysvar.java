package Beans;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sysvar {
	//动态：系统运行过程一直在变化
	public  static String sellformnum="";
	public  static String startsellformnum="";
	public  static String endsellformnum="";
	public  static String policynum="";
	public  static String plancode="";        //方案
	public  static String online="";          //方案
	//public  static String outlets="";         //网点
	//静态：系统运行过程相对不变
	public  static String riskcode="";        //险种
	public  static String agentcode="";       //代理机构代码
	public  static String agentname="";       //代理机构名称
	public  static String printertype="";     //打印机型号
	public  static String sellformtype="";    //单证类型
	public  static String poscode="";         //POS机代码
	//流程控制变量
	public  static String idnorule="";        //是否做同一客户70天不能出单的校验
	public  static String sellformrule="";    //单证入库前是否做校验
	public  static String headnum="";         //单证头号码段
	public  static String interval="";        //几天出单校验

	
	private static DateFormat format1= new SimpleDateFormat("yyyy-MM-dd");   
	private static DateFormat format2= new SimpleDateFormat("HH:mm");	//"HH:mm:ss"
	public static String getDate(){
		return format1.format( new Date() );
	}
	public static String getTime(){
		return format2.format( new Date() );
	}
	//保单号码格式填充
	public static String pad2sellformnum(BigInteger big) {
		String args=big.toString().trim();
		int i=14-args.length();
		if(i>0){
			for(int j=1;j<=i;j++){
				args="0"+args;
			}
		}
		return args ;
	}
	//单证号码格式填充
	public static String pad2policynum(BigInteger big) {
		String args=big.toString().trim();
		int len=args.length();
		if(len<=8){
			int i=8-len;
			for(int j=1;j<=i;j++){
				args="0"+args;
			}
		}else{
			args=args.substring(len-8);			
		}
		return Sysvar.headnum.trim()+args ;
	}
}
