package pos.util;

public class card {
	 public static String[] getDataByNo(String num) {
	        // 判断是否为空
	        if (num == null || num.trim().equals("")) {
	            return null;
	        }
	        //得到证件号码长度
	        int length = num.length();
	        // 判断是否为15或18位
	        if (length != 15 && length != 18) {
	            return null;
	        }
	        //如果证件号码为15位，转换为18位
	        if(length == 15){
	            num=uptoeighteen(num);
	        }
	        //求和
	        int count = 0;
	        // 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
	        char[] numChar = num.substring(0, 17).toCharArray();
	        int[] xishu = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
	        int[] vi = { 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 };
	        for (int i = 0; i < numChar.length; i++) {
	            count += (numChar[i] - '0') * xishu[i];
	        }
	        // 余数
	        int temp = count % 11;
	        // 判断校验码位是否正确
	        String yx = (temp == 2 ? "X" : String.valueOf(vi[temp])).toString();
	        String[] data = new String[2];
	        if (!yx.equalsIgnoreCase(num.substring(17, 18))) {
	            return null;
	        } else {
	                // 得到生日
	                data[0] = num.substring(6, 14);
	                // 校验性别
	                data[1] = num.substring(14, 17);
	            // 性别代码为偶数是女性奇数为男性
	            if (Integer.parseInt(data[1]) % 2 == 0) {
	                data[1] = "女";
	            } else {
	                data[1] = "男";
	            }
	        }
	        return data;
	    }
	    // 15位身份证号码提升为18位
	    public static String uptoeighteen(String fifteencardid) {
	        String eightcardid = fifteencardid.substring(0, 6);
	        eightcardid = eightcardid + "19";
	        eightcardid = eightcardid + fifteencardid.substring(6, 15);
	        eightcardid = eightcardid + getVerify(eightcardid);
	        return eightcardid;
	    }
	    // 得到第18位的校验码
	    public static String getVerify(String eightcardid) {
	        int remaining = 0;
	        int[] wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
	        int[] vi = { 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 };
	        int[] ai = new int[18];
	        String returnStr = null;
	        try {
	            if (eightcardid.length() == 18) {
	                eightcardid = eightcardid.substring(0, 17);
	            }
	            if (eightcardid.length() == 17) {
	                int sum = 0;
	                String k = null;
	                for (int i = 0; i < 17; i++) {
	                    k = eightcardid.substring(i, i + 1);
	                    ai[i] = Integer.parseInt(k);
	                    k = null;
	                }
	                for (int i = 0; i < 17; i++) {
	                    sum = sum + wi[i] * ai[i];
	                }
	                remaining = sum % 11;
	            }
	            returnStr = remaining == 2 ? "X" : String.valueOf(vi[remaining]);
	        } catch (Exception ex) {
	            return null;
	        } finally {
	            wi = null;
	            vi = null;
	            ai = null;
	        }
	        return returnStr;
	    }
}
