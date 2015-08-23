package cdsdk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * <p>
 * Title: 身份证基本信息属性
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author 山东神思
 * @version 1.0
 */

public class CardAttribModel {
	private DateFormat format1 ;
	private DateFormat format2 ;

	private String name = "";
	private String sex = "";
	private String nation = "";
	private String birthday = "";
	private String address = "";
	private String idcard = "";
	private String department = "";
	private String enddate = "";

	public CardAttribModel() {
		format1 = new SimpleDateFormat("yyyyMMdd");  
		format2 = new SimpleDateFormat("yyyy-MM-dd");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		
		try{
			this.birthday=format2.format(format1.parse(birthday));
		}catch(Exception e){
			this.birthday = birthday;
		}
		
		
	}
	
	public void setBirthday2(String birthday) {
		
		try{
			this.birthday=format2.format(format2.parse(birthday));
		}catch(Exception e){
			this.birthday = birthday;
		}
		
		
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

}
