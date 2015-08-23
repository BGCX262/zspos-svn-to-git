package pos.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.math.BigInteger;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import Beans.Sysvar;
import pos.mysoap.services.mysoapCheckup;
import pos.services.Dom4j;
import pos.util.net;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class store extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	private Dom4j      domob;	
	/**
	 * Create the panel.
	 */
	public store(Dom4j dom) {
		this.domob=dom;
		setLayout(null);
		
		JLabel label = new JLabel("本批次单证起始号");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(116, 40, 122, 15);
		add(label);
		
		textField = new JTextField();
		textField.setBounds(262, 37, 157, 21);
		add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("本批次单证终止号");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(116, 81, 122, 15);
		add(label_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(262, 78, 157, 21);
		add(textField_1);
		
		JLabel label_2 = new JLabel("本批次单证数量");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(116, 122, 122, 15);
		add(label_2);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(262, 119, 157, 21);
		add(textField_2);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(30, 177, 516, 2);
		add(separator);
		
		JButton button = new JButton("提   交");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				check();
			}
		});
		button.setBounds(262, 200, 157, 23);
		add(button);

	}
	
	
	//新版本
	private void save(String beginnum,String endnum){
		domob.write("/sysvar/sellformnum",Sysvar.pad2sellformnum(new BigInteger(beginnum)).trim());
		domob.write("/sysvar/policynum",Sysvar.pad2policynum(new BigInteger(beginnum)).trim());
		domob.write("/sysvar/startsellformnum", beginnum);
		domob.write("/sysvar/endsellformnum", endnum);
		if(domob.save()){
			Sysvar.sellformnum      =domob.readFirst("/sysvar/sellformnum");
			Sysvar.policynum        =domob.readFirst("/sysvar/policynum");
			Sysvar.startsellformnum =domob.readFirst("/sysvar/startsellformnum").trim();
			Sysvar.endsellformnum   =domob.readFirst("/sysvar/endsellformnum").trim();
			JOptionPane.showMessageDialog( null, "【单证入库成功】");
		}else{
			JOptionPane.showMessageDialog( null, "【单证入库失败】");
		}
	}
	private void check(){
		String begin =textField.getText().trim();
		String end   =textField_1.getText().trim();
		String num   =textField_2.getText().trim();
		try{
			if(begin.length()==14 && end.length()==14  ){//单证号码长度检查
				
				BigInteger big1= new BigInteger(begin);
				BigInteger big2= new BigInteger(end);
				BigInteger big3= big2.subtract(big1).add(new BigInteger("1"));
				if( big3.compareTo(new BigInteger(num))==0){//单证号码数量检查
					if( net.isonline() ){//判断网络连接{
						if(Sysvar.sellformrule.equals("yes")){
							
							mysoapCheckup ob=new mysoapCheckup();
							SAXReader saxReader = new SAXReader();
							Document  document = saxReader.read("xml/mysoap/save.xml");
							
							document.selectSingleNode("/transdata/baseinfo/beginnum").setText(begin);
							document.selectSingleNode("/transdata/baseinfo/endnum").setText(end);
							document.selectSingleNode("/transdata/baseinfo/machineid").setText(Sysvar.poscode);
							if( ob.checkup(document) ){
								save(begin,end);//保存入库参数
							}else{
								JOptionPane.showMessageDialog( null, "【单证入库失败，请确认该单证号码段已发放并且能上网】");
							}
						}else{
							save(begin,end);//保存入库参数
						}
					}else{
						JOptionPane.showMessageDialog( null, "【无法连接到服务器，请先连接互联网】");
					}
				}else{
					JOptionPane.showMessageDialog( null, "【单证数量不对】");
				}
			}else{
				JOptionPane.showMessageDialog( null, "【单证号码位数不对】");
			}
		}catch(Exception ex){
			JOptionPane.showMessageDialog( null, "【单证号码入库异常】");
		}
	}
}
