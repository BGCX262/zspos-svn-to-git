package cdsdk;

import java.io.*;

//import java.awt.Image;
//import java.awt.image.BufferedImage;
//import javax.imageio.ImageIO;
//import java.awt.image.RenderedImage;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGCodec;

/**
 * <p>
 * Title: 山东神思628A机具读取
 * </p>
 * <p>
 * Description: 驱动山东神思628A二代证机具
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:山东神思
 * </p>
 * 
 * @author 山东神思
 * @version 1.0
 */
public class TermB {

	//Java native函数，使用Jni技术调用dll
	private native int start(int iPort);
	private native int finish();
	private native int AuthenticateCard();
	private native int read(int Act);
	private native int getTest();
	static 
	{
		System.loadLibrary("jtermb");
	}
	
	
	private static TermB termB;
	private final String FILE_ROOT_DIR = System.getProperty("user.dir");

	public static void main(String[] args) {
		TermB test = TermB.getInstance();
		int returnCode = test.start(1);
		System.out.println(" " + returnCode);
	}
	
	
	public static TermB getInstance() {
		if (termB == null)
			termB = new TermB();
		return termB;
	}

	public void dispose() {
		if (termB != null)
			termB.finish();
	}

	/**
	 * 初始化端口(串口:1~16,USB:1001~1016)
	 * 
	 * @param iPort
	 *            int
	 * @return int (1:success)
	 */
	public int InitComm2(int iPort) {
		return (int) start(iPort);
	}

	/**
	 * 关闭端口
	 * 
	 * @return int (1:success)
	 */
	public int CloseComm() {
		return (int) finish();
	}

	/**
	 * 卡认证
	 * 
	 * @return int (1:success)
	 */
	public int authenticate() {
		return (int) AuthenticateCard();
	}

	/**
	 * 读卡内信息
	 * 
	 * @param Act
	 *            int (1:读基本信息;2:只读文字信息;3:读最新住址信息)
	 * @return int (1:success)
	 */
	public int Read_Content(int Act) {
		return (int) read(Act);
	}

	public int getTestValue() {
		return (int) getTest();
	}

	/**
	 * 返回当前读取的二代证的文字信息
	 * 
	 * @return byte[]
	 */
	public byte[] read_wz() {
		String filename = FILE_ROOT_DIR + "\\wz.txt";
		byte wzData[] = (byte[]) null;
		try {
			FileInputStream hFile = new FileInputStream(filename);
			int i = hFile.available();
			wzData = new byte[i];
			hFile.read(wzData);
			hFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wzData;
	}

//以下两个函数是私人注释,需要ParseBMPToJPG.java文件 
//	/**
//	 * 取当前卡中相片的信息
//	 * 
//	 * @return BufferedImage
//	 */
//	public BufferedImage getPhotoBufferedImage() {
//		Image img = new ParseBMPToJPG().load(System.getProperty("user.dir")
//				+ "\\zp.bmp");
//
//		BufferedImage bimg = new BufferedImage(img.getWidth(null), img
//				.getHeight(null), BufferedImage.TYPE_INT_RGB);
//		bimg.createGraphics().drawImage(img, 0, 0, null);
//
//		return bimg;
//	}
//
//	/**
//	 * 取当前卡中的相片信息
//	 * 
//	 * @return Image
//	 */
//	public Image getPhotoImage() {
//		Image img = new ParseBMPToJPG().load(System.getProperty("user.dir")
//				+ "\\zp.bmp");
//
//		return img;
//	}

	
	/**
	 * 获取读取的新地址信息
	 * 
	 * @return byte[]
	 */
	public byte[] read_newAdd() {
		String filename = FILE_ROOT_DIR + "\\wz.txt";
		byte newAddData[] = (byte[]) null;
		try {
			FileInputStream hFile = new FileInputStream(filename);
			int i = hFile.available();
			newAddData = new byte[i];
			hFile.read(newAddData);
			hFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newAddData;
	}

	public static String Unicode2GBK(String dataStr) {
		/*
		 * int index = 0; StringBuffer buffer = new StringBuffer();
		 * while(index<dataStr.length()-2) {
		 * if(!"\\u".equals(dataStr.substring(index,index+2))){
		 * buffer.append(dataStr.charAt(index)); index++; continue; } String
		 * charStr = ""; charStr = dataStr.substring(index+2,index+6); char
		 * letter = (char) Integer.parseInt(charStr, 16 );
		 * buffer.append(letter); index+=6; } return buffer.toString();
		 */
		String str;
		str = null;
		try {
			str = new String(dataStr.getBytes(), "GBK");
		} catch (java.io.UnsupportedEncodingException e) {
		}
		return str;
	}

	/**
	 * 返回卡基本属性
	 * 
	 * @return CardAttribModel
	 */
	public CardAttribModel getCardBaseData() {
		CardAttribModel model = new CardAttribModel();

		byte[] source = read_wz();
		if (source.length <= 0) {
			return null;
		}
		// 取当前读取信息
		String tmpstr1 = "", tmpstr2 = "", tmpstr3 = "", tmpstr4 = "", wltfile = "", bmpfile = "";
		String name = "", sex = "", nation = "", birthdate = "", address = "", idnum = "", organ = "", startdate = "", enddate = "", newaddress = ""; // 姓名,性别,民族,出生年月,住址,身份证号,签发机关,起始日期,终止日期,新住址
		int year, month, day, hour, minute, second, tmpint1, tmpint2, tmpint3, tmpint4;
		byte[] tmpb = new byte[256];
		System.arraycopy(source, 0, tmpb, 0, 256);

		try {
			tmpstr1 = new String(tmpb, "UTF-16LE");
		} catch (java.io.UnsupportedEncodingException e) {
		}

		tmpb = new byte[4];
		System.arraycopy(source, 0, tmpb, 0, 4);
		tmpint1 = 0;
		tmpint1 |= tmpb[0];
		tmpint1 &= 0x000000FF; // 去掉负号
		tmpint2 = 0;
		tmpint2 |= tmpb[1];
		tmpint2 &= 0x000000FF;
		tmpint3 = 0;
		tmpint3 |= tmpb[2];
		tmpint3 &= 0x000000FF;
		tmpint4 = 0;
		tmpint4 |= tmpb[3];
		tmpint4 &= 0x000000FF;

		int tmpp = tmpint4 * 0x1000000 + tmpint3 * 0x10000 + tmpint2 * 0x100
				+ tmpint1;
		year = ((tmpp >> 25) & 0x7F) + 2000;
		month = (tmpp >> 21) & 0x0F;
		day = (tmpp >> 16) & 0x1F;
		hour = (tmpp >> 11) & 0x1F;
		minute = (tmpp >> 5) & 0x3F;
		second = (tmpp & 0x1F) * 2;

		tmpstr2 = "";
		// 0316 tmpb=new byte[1024]; // 38862
		// 0316 System.arraycopy(source,264,tmpb,0,1024); // 38862
		// 0316 byte[] bmpphoto=tmpb;

		tmpb = new byte[4];
		System.arraycopy(source, 4, tmpb, 0, 4);
		try {
			tmpstr4 = new String(tmpb, "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
		}
		// 从unicode文字信息(256)里提取 姓名,性别,民族,出生年月,住址,身份证号,签发机关,起始日期,终止日期
		// 这里舍弃tmpstr1 另外有可能从unicode转string
//		int i, j = 0;
//		String charstr1 = "", charstr2 = "";
//		char letter;

		tmpb = new byte[30];
		System.arraycopy(source, 0, tmpb, 0, 30);
		try {
			name = new String(tmpb, "UTF-16LE"); // UTF-16BE
			name = new String(name.getBytes("UTF-8"), "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
		}
		model.setName(name);

		tmpb = new byte[2];
		System.arraycopy(source, 30, tmpb, 0, 2);
		if (tmpb[0] == 50) // 0x32
		{
			model.setSex("1");
		} else if (tmpb[0] == 49) // 0x32
		{
			model.setSex("0");
		}
		/*
		 * System.out.println("sex value is :"+tmpb[0]); try{ sex=new
		 * String(tmpb,"UTF-16LE"); sex=new
		 * String(nation.getBytes("UTF-8"),"UTF-8");
		 * }catch(java.io.UnsupportedEncodingException e){ } model.setSex(sex);
		 */

		tmpb = new byte[4];
		System.arraycopy(source, 32, tmpb, 0, 4);
		System.out.println("nation value is :" + tmpb[0] + tmpb[1] + tmpb[2]
				+ tmpb[3]);
		try {
			nation = new String(tmpb, "UTF-16LE");
			nation = new String(nation.getBytes("UTF-8"), "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
		}
		model.setNation(nation);

		tmpb = new byte[16];
		System.arraycopy(source, 36, tmpb, 0, 16);
		try {
			birthdate = new String(tmpb, "UTF-16LE");
			birthdate = new String(birthdate.getBytes("UTF-8"), "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
		}
		model.setBirthday(birthdate);

		tmpb = new byte[70];
		System.arraycopy(source, 52, tmpb, 0, 70);
		try {
			address = new String(tmpb, "UTF-16LE");
			address = new String(address.getBytes("UTF-8"), "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
		}
		model.setAddress(address);

		tmpb = new byte[36];
		System.arraycopy(source, 122, tmpb, 0, 36);
		try {
			idnum = new String(tmpb, "UTF-16LE");
			idnum = new String(idnum.getBytes("UTF-8"), "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
		}
		model.setIdcard(idnum);

		tmpb = new byte[30];
		System.arraycopy(source, 158, tmpb, 0, 30);
		try {
			organ = new String(tmpb, "UTF-16LE");
			organ = new String(organ.getBytes("UTF-8"), "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
		}
		model.setDepartment(organ);

		tmpb = new byte[16];
		System.arraycopy(source, 188, tmpb, 0, 16);
		try {
			startdate = new String(tmpb, "UTF-16LE");
			startdate = new String(startdate.getBytes("UTF-8"), "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
		}

		tmpb = new byte[16];
		System.arraycopy(source, 204, tmpb, 0, 16);
		try {
			enddate = new String(tmpb, "UTF-16LE");
			enddate = new String(enddate.getBytes("UTF-8"), "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
		}
		model.setEnddate(enddate);

		return model;
	}


}
