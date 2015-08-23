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
				
				gui.getPlancode();   //获得用户选择的方案，保存在Sysvar.plancode
				//获得客户的基本信息1.生日 2.证件号 3.名字 4.性别
				Custom.birthday=model.getBirthday().trim(); 
				Custom.idno=model.getIdcard().trim();
				Custom.name=model.getName().trim();
				Custom.sex=model.getSex().trim();
				ins.go1();  	    //所有的信息收集完整后，出单承保。
				gui.refresh();      //承保后更新用户界面GUI参数
				
				Lock.Syslock.writeLock().unlock();
			}
		}
	}

}
