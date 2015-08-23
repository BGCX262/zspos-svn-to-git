package pos.services;

import javax.swing.JOptionPane;

import cdsdk.CardAttribModel;
import cdsdk.TermB;

public class ReadIDCard {

	/**
	 * @param args
	 */
	private TermB   reader;
	public  boolean useable =false;  //������ñ�־
	
	public ReadIDCard() {
		try {
			reader = TermB.getInstance();
			if ( reader.InitComm2(1001)== 1) {
				useable=true;
			} else {
				System.out.println("����������ʼ��ʧ�ܡ�");
				JOptionPane.showMessageDialog( null, "����������ʼ��ʧ�ܣ�������·��");
				useable=false;
			}

		} catch (Exception e) {
			System.out.println("����������ʼ���쳣��");
			JOptionPane.showMessageDialog( null, "����������ʼ���쳣��������·��");
			useable=false;
		}

	}

	public boolean close() {
		if (reader != null) {
			if (reader.CloseComm() == 1) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public CardAttribModel read() {
		CardAttribModel model = null;
		try {
			Thread.sleep(150);
			if (reader.authenticate() == 1) {
				if (reader.Read_Content(1) == 1) {
					model = reader.getCardBaseData();
				}
			}
			return model;
		} catch (Exception e) {
			System.out.println("�����������������쳣��");
			return null;
		}
	}

//	public static void main(String[] args) {
//		
//		// TODO Auto-generated method stub
//		CardAttribModel model;
//		ReadIDCard ob = new ReadIDCard();
//		while ( ob.useable ) {
//			model = ob.read();
//			if (model != null) {
//				System.out.println(model.getName());
//			}
//		}
//		System.out.println(ob.close());
//	}

}
