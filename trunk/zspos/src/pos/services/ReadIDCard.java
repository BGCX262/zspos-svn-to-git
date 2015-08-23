package pos.services;

import javax.swing.JOptionPane;

import cdsdk.CardAttribModel;
import cdsdk.TermB;

public class ReadIDCard {

	/**
	 * @param args
	 */
	private TermB   reader;
	public  boolean useable =false;  //对象可用标志
	
	public ReadIDCard() {
		try {
			reader = TermB.getInstance();
			if ( reader.InitComm2(1001)== 1) {
				useable=true;
			} else {
				System.out.println("【读卡器初始化失败】");
				JOptionPane.showMessageDialog( null, "【读卡器初始化失败，请检查线路】");
				useable=false;
			}

		} catch (Exception e) {
			System.out.println("【读卡器初始化异常】");
			JOptionPane.showMessageDialog( null, "【读卡器初始化异常，请检查线路】");
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
			System.out.println("【读卡器读数操作异常】");
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
