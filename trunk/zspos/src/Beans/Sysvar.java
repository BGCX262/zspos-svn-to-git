package Beans;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sysvar {
	//��̬��ϵͳ���й���һֱ�ڱ仯
	public  static String sellformnum="";
	public  static String startsellformnum="";
	public  static String endsellformnum="";
	public  static String policynum="";
	public  static String plancode="";        //����
	public  static String online="";          //����
	//public  static String outlets="";         //����
	//��̬��ϵͳ���й�����Բ���
	public  static String riskcode="";        //����
	public  static String agentcode="";       //�����������
	public  static String agentname="";       //�����������
	public  static String printertype="";     //��ӡ���ͺ�
	public  static String sellformtype="";    //��֤����
	public  static String poscode="";         //POS������
	//���̿��Ʊ���
	public  static String idnorule="";        //�Ƿ���ͬһ�ͻ�70�첻�ܳ�����У��
	public  static String sellformrule="";    //��֤���ǰ�Ƿ���У��
	public  static String headnum="";         //��֤ͷ�����
	public  static String interval="";        //�������У��

	
	private static DateFormat format1= new SimpleDateFormat("yyyy-MM-dd");   
	private static DateFormat format2= new SimpleDateFormat("HH:mm");	//"HH:mm:ss"
	public static String getDate(){
		return format1.format( new Date() );
	}
	public static String getTime(){
		return format2.format( new Date() );
	}
	//���������ʽ���
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
	//��֤�����ʽ���
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
