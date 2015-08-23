package pos.util;

public class card {
	 public static String[] getDataByNo(String num) {
	        // �ж��Ƿ�Ϊ��
	        if (num == null || num.trim().equals("")) {
	            return null;
	        }
	        //�õ�֤�����볤��
	        int length = num.length();
	        // �ж��Ƿ�Ϊ15��18λ
	        if (length != 15 && length != 18) {
	            return null;
	        }
	        //���֤������Ϊ15λ��ת��Ϊ18λ
	        if(length == 15){
	            num=uptoeighteen(num);
	        }
	        //���
	        int count = 0;
	        // 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
	        char[] numChar = num.substring(0, 17).toCharArray();
	        int[] xishu = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
	        int[] vi = { 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 };
	        for (int i = 0; i < numChar.length; i++) {
	            count += (numChar[i] - '0') * xishu[i];
	        }
	        // ����
	        int temp = count % 11;
	        // �ж�У����λ�Ƿ���ȷ
	        String yx = (temp == 2 ? "X" : String.valueOf(vi[temp])).toString();
	        String[] data = new String[2];
	        if (!yx.equalsIgnoreCase(num.substring(17, 18))) {
	            return null;
	        } else {
	                // �õ�����
	                data[0] = num.substring(6, 14);
	                // У���Ա�
	                data[1] = num.substring(14, 17);
	            // �Ա����Ϊż����Ů������Ϊ����
	            if (Integer.parseInt(data[1]) % 2 == 0) {
	                data[1] = "Ů";
	            } else {
	                data[1] = "��";
	            }
	        }
	        return data;
	    }
	    // 15λ���֤��������Ϊ18λ
	    public static String uptoeighteen(String fifteencardid) {
	        String eightcardid = fifteencardid.substring(0, 6);
	        eightcardid = eightcardid + "19";
	        eightcardid = eightcardid + fifteencardid.substring(6, 15);
	        eightcardid = eightcardid + getVerify(eightcardid);
	        return eightcardid;
	    }
	    // �õ���18λ��У����
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
