package pos.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class dateformat {
	public static String getBeforeDate(String dates,int days)  
	{  
		try{
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
		    Date date=df.parse(dates);
		    Calendar calendar = Calendar.getInstance();     
		    calendar.setTime(date);  
		    calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) - days);  
		    return df.format(calendar.getTime());  
		}catch(Exception ex){
			System.out.println("【日期计算出错】");
			return dates;
		}

	}  
	  
	public static String getAfterDate(String dates,int days)  
	{  
		try{
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
		    Date date=df.parse(dates);
		    Calendar calendar = Calendar.getInstance();     
		    calendar.setTime(date);  
		    calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR) + days);  
		    return df.format(calendar.getTime());  
		}catch(Exception ex){
			System.out.println("【日期计算出错】");
			return dates;
		}

	}  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(dateformat.getAfterDate("2011-09-27", 91));
	}
}
