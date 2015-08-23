package pos.background.thread;

import pos.gui.mainframe;
import pos.services.ReadIDCard;
import cdsdk.CardAttribModel;
import Beans.Custom;
import Beans.Lock;
import Model.auto;

public class insjob implements Runnable {
	
	private auto ins=null;
	private mainframe gui=null;
	
	public insjob( auto ins , mainframe gui ){
		this.ins=ins;
		this.gui=gui;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		CardAttribModel model=null;
		ReadIDCard idcard =new ReadIDCard();
		while(idcard.useable && ins.useable){
			model = idcard.read();
			if ( model!= null ) {	
				Lock.Syslock.writeLock().lock();
				
				gui.getPlancode();   //����û�ѡ��ķ�����������Sysvar.plancode
				//��ÿͻ��Ļ�����Ϣ1.���� 2.֤���� 3.���� 4.�Ա�
				Custom.birthday=model.getBirthday().trim(); 
				Custom.idno=model.getIdcard().trim();
				Custom.name=model.getName().trim();
				Custom.sex=model.getSex().trim();
				ins.go1();  	    //���е���Ϣ�ռ������󣬳����б���
				gui.refresh();      //�б�������û�����GUI����
				
				Lock.Syslock.writeLock().unlock();
			}
		}
	}

}
