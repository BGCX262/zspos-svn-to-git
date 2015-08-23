package Beans;

public class Ptr {
	public static Sysvar sysvar;
	public static Custom custom;
	public static User   user;
	
	//打印备用字段
	public static String  remark1="";
	public static String  remark2="";
	public static String  remark3="";
	public static String  remark4="";         
	public static String  remark5="";
	public static String  remark6="";
	public static String  remark7="";       
	public static String  remark8="";
	public static String  remark9="";
	public static String  remark10="";      
	public static String  remark11="";
	public static String  remark12="";
	public static String  remark13="";       
	public static String  remark14="";
	public static String  remark15="";
	public static String  remark16="";
	public static String  remark17="";
	public static String  remark18="";       
	public static String  remark19="";
	public static String  remark20="";
	
	public static String read(String item){
		item=item.toLowerCase().trim();
		//投保人
		if(item.equals("applicantname")){
			return custom.name.trim();
		}
		if(item.equals("applicantsex")){
			String str=custom.sex.trim();
			if(str.equals("0")){
				return "男";
			}
			if(str.equals("1")){
				return "女";
			}
			return str;
		}
		if(item.equals("applicantbirthday")){
			return custom.birthday.trim();
		}
		if(item.equals("applicantidno")){
			return custom.idno.trim();
		}
		if(item.equals("applicantidtype")){
			return custom.idtype.trim();
		}
		//被保人
		if(item.equals("insuredname")){
			return custom.name.trim();
		}
		if(item.equals("insuredsex")){
			String str=custom.sex.trim();
			if(str.equals("0")){
				return "男";
			}
			if(str.equals("1")){
				return "女";
			}
			return str;
		}
		if(item.equals("insuredbirthday")){
			return custom.birthday.trim();
		}
		if(item.equals("insuredidno")){
			return custom.idno.trim();
		}
		if(item.equals("insuredidtype")){
			return custom.idtype.trim();
		}
		//单证号与保单号
		if(item.equals("sellformnum")){
			return sysvar.sellformnum.trim();
		}
		if(item.equals("policynum")){
			return sysvar.policynum.trim();
		}
		//打印日期与打印时间
		if(item.equals("printday")){
			return sysvar.getDate().trim();
		}
		if(item.equals("printtime")){
			return sysvar.getTime().trim();
		}
		//代理机构
		if(item.equals("agentcode")){
			return sysvar.agentcode.trim();
		}
		if(item.equals("agentname")){
			return sysvar.agentname.trim();
		}
		//险种，方案，网点
		if(item.equals("riskcode")){
			return sysvar.riskcode.trim();
		}
		if(item.equals("plancode")){
			return sysvar.plancode.trim();
		}
		if(item.equals("outlets")){
			return User.outlet.trim();
		}
		//出单人
		if(item.equals("operatorcode")){
			return user.userid.trim();
		}
		if(item.equals("operatorname")){
			return user.name.trim();
		}
		//备用字段
		if(item.equals("remark1")){
			return remark1;
		}
		if(item.equals("remark2")){
			return remark2;
		}
		if(item.equals("remark3")){
			return remark3;
		}
		if(item.equals("remark4")){
			return remark4;
		}
		if(item.equals("remark5")){
			return remark5;
		}
		if(item.equals("remark6")){
			return remark6;
		}
		if(item.equals("remark7")){
			return remark7;
		}
		if(item.equals("remark8")){
			return remark8;
		}
		if(item.equals("remark9")){
			return remark9;
		}
		if(item.equals("remark10")){
			return remark10;
		}
		if(item.equals("remark11")){
			return remark11;
		}
		if(item.equals("remark12")){
			return remark12;
		}
		if(item.equals("remark13")){
			return remark13;
		}
		if(item.equals("remark14")){
			return remark14;
		}
		if(item.equals("remark15")){
			return remark15;
		}
		if(item.equals("remark16")){
			return remark16;
		}
		if(item.equals("remark17")){
			return remark17;
		}
		if(item.equals("remark18")){
			return remark18;
		}
		if(item.equals("remark19")){
			return remark19;
		}
		if(item.equals("remark20")){
			return remark20;
		}
		//如果传入的是一个数字N，返回N个空格的串
		try{
			String str="";
			int i = Integer.parseInt(item);
			for(int j=1;j<=i;j++){
				str=str+" ";
			}
			return str;
		}catch(Exception ex){
			
		}
		return "";
	}
	
	
	
	
}
