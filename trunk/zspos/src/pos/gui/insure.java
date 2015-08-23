package pos.gui;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;

import Beans.Gui;
import Beans.Lock;
import Beans.Sysvar;
import pos.services.Dom4j;
import pos.services.jdbc;
import Model.auto;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;

import org.dom4j.Element;
import org.dom4j.Node;

import java.awt.Color;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class insure extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private JCheckBox  checkBox;
	private JComboBox  comboBox_1;
	
	private Dom4j      domob;
	private auto       ins;
	private jdbc       jdbcob;    //数据库查询对象
	/**
	 * Create the panel.
	 */
	public insure(Dom4j dom,auto ins,jdbc jdbcob) {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				refresh();
			}
		});
		this.domob=dom;
		this.ins=ins;
		this.jdbcob=jdbcob;
		
		
		setFont(new Font("宋体", Font.PLAIN, 12));
		setLayout(null);
		
		JLabel label = new JLabel("\u3010\u6700\u8FD1\u51FA\u5355\u3011");
		label.setBounds(0, 10, 104, 15);
		add(label);
		
		JLabel label_2 = new JLabel("\u5355\u8BC1\u53F7\uFF1A");
		label_2.setBounds(43, 44, 58, 15);
		add(label_2);
		
		textField = new JTextField();
		textField.setBounds(111, 41, 156, 21);
		textField.setEditable(false);
		add(textField);
		textField.setColumns(10);
		
		JLabel label_3 = new JLabel("\u4FDD\u5355\u53F7\uFF1A");
		label_3.setBounds(286, 44, 66, 15);
		add(label_3);
		
		textField_3 = new JTextField();
		textField_3.setBounds(357, 41, 152, 21);
		textField_3.setEditable(false);
		add(textField_3);
		textField_3.setColumns(10);
		
		JLabel label_5 = new JLabel("\u59D3  \u540D\uFF1A");
		label_5.setBounds(43, 70, 58, 15);
		add(label_5);
		
		textField_1 = new JTextField();
		textField_1.setBounds(111, 67, 156, 21);
		textField_1.setEditable(false);
		add(textField_1);
		textField_1.setColumns(10);
		
		JLabel label_4 = new JLabel("\u8EAB\u4EFD\u8BC1\uFF1A");
		label_4.setBounds(286, 70, 66, 15);
		add(label_4);
		
		textField_4 = new JTextField();
		textField_4.setBounds(357, 67, 152, 21);
		textField_4.setEditable(false);
		add(textField_4);
		textField_4.setColumns(10);
		
		JLabel label_6 = new JLabel("\u51FA\u751F\u65E5\u671F\uFF1A");
		label_6.setBounds(31, 96, 70, 15);
		add(label_6);
		
		textField_2 = new JTextField();
		textField_2.setBounds(111, 93, 156, 21);
		textField_2.setEditable(false);
		add(textField_2);
		textField_2.setColumns(10);
		
		JLabel label_7 = new JLabel("\u6027  \u522B\uFF1A");
		label_7.setBounds(286, 96, 66, 15);
		add(label_7);
		
		
		textField_5 = new JTextField();
		textField_5.setBounds(357, 93, 152, 21);
		textField_5.setEditable(false);
		add(textField_5);
		textField_5.setColumns(10);
		
		JLabel label_8 = new JLabel("\u65B9  \u6848\uFF1A");
		label_8.setBounds(43, 122, 58, 15);
		add(label_8);
		
		JLabel label_1 = new JLabel("\u3010\u4E0B\u4E00\u5F20\u5355\u3011");
		label_1.setBounds(0, 181, 104, 15);
		add(label_1);
		
		JButton button = new JButton("\u9000  \u4FDD");
		button.addMouseListener(new MouseAdapter() {
			//退保
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if ( JOptionPane.showConfirmDialog( null, "确认退保吗?", "退保",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION ){
					refund();
				}
			}
		});
		button.setBounds(357, 118, 152, 23);
		add(button);
		
		JLabel label_9 = new JLabel("\u5355\u8BC1\u53F7\uFF1A");
		label_9.setBounds(43, 225, 58, 15);
		add(label_9);
		
		JLabel label_10 = new JLabel("\u59D3  \u540D\uFF1A");
		label_10.setBounds(43, 251, 58, 15);
		add(label_10);
		
		JLabel label_11 = new JLabel("\u51FA\u751F\u65E5\u671F\uFF1A");
		label_11.setBounds(31, 277, 70, 15);
		add(label_11);
		
		JLabel label_12 = new JLabel("\u65B9\u6848\u9009\u62E9\uFF1A");
		label_12.setBounds(31, 303, 70, 15);
		add(label_12);
		
		textField_6 = new JTextField();
		textField_6.setEditable(false);
		textField_6.setColumns(10);
		textField_6.setBounds(111, 222, 156, 21);
		add(textField_6);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		textField_7.setBounds(111, 248, 156, 21);
		add(textField_7);
		
		textField_8 = new JTextField();
		textField_8.setColumns(10);
		textField_8.setBounds(111, 274, 156, 21);
		add(textField_8);
		
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		comboBox_1 = new JComboBox(model);
		
		Iterator iter=domob.readAll("/sysvar/plans/item");
		while(iter.hasNext()){
			Element element = (Element) iter.next();
			
			Node key=element.selectSingleNode("key");
			Node value =element.selectSingleNode("value");
			
			String strkey=key.getText().trim();
			String strvalue=value.getText().trim();
			
			item it=new item();
			it.setKey(strkey);
			it.setValue(strvalue);
			model.addElement(it);
		}
		

		comboBox_1.setForeground(Color.BLACK);
		comboBox_1.setPreferredSize(new Dimension(74, 22));
		comboBox_1.setMinimumSize(new Dimension(69, 8));
		comboBox_1.setFont(new Font("宋体", Font.BOLD, 12));
		comboBox_1.setBounds(111, 300, 156, 21);
		add(comboBox_1);
		
		JLabel label_13 = new JLabel("\u4FDD\u5355\u53F7\uFF1A");
		label_13.setBounds(286, 225, 66, 15);
		add(label_13);
		
		JLabel label_14 = new JLabel("\u8EAB\u4EFD\u8BC1\uFF1A");
		label_14.setBounds(286, 251, 66, 15);
		add(label_14);
		
		JLabel label_15 = new JLabel("\u6027  \u522B\uFF1A");
		label_15.setBounds(286, 277, 66, 15);
		add(label_15);
		
		textField_9 = new JTextField();
		textField_9.setEditable(false);
		textField_9.setColumns(10);
		textField_9.setBounds(357, 222, 152, 21);
		add(textField_9);
		
		textField_10 = new JTextField();
		textField_10.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				autocomplete();
			}
		});
		textField_10.setColumns(10);
		textField_10.setBounds(357, 248, 152, 21);
		add(textField_10);
		
		textField_11 = new JTextField();
		textField_11.setColumns(10);
		textField_11.setBounds(357, 274, 152, 21);
		add(textField_11);
		
		JButton button_1 = new JButton("\u627F  \u4FDD");
		button_1.addMouseListener(new MouseAdapter() {
			//承保
			@Override
			public void mouseClicked(MouseEvent arg0) {
				save();
			}
		});
		button_1.setBounds(357, 299, 152, 23);
		add(button_1);
		
		textField_12 = new JTextField();
		textField_12.setEditable(false);
		textField_12.setColumns(10);
		textField_12.setBounds(111, 119, 156, 21);
		add(textField_12);
		
		checkBox = new JCheckBox("编辑");
		checkBox.setSelected(true);
		checkBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//可编辑键状态变化
				if( checkBox.isSelected() ){
					comboBox_1.setEnabled(true);
				}else{
					comboBox_1.setEnabled(false);
				}
			}
		});

		checkBox.setBounds(282, 299, 58, 23);
		add(checkBox);
		
		ini();
	}
	//界面初始化
	private void ini(){
		item it=(item)comboBox_1.getSelectedItem();
		Sysvar.plancode=it.getValue().trim();
	}
	//获得方案
	public void getPlancode(){
		item it=(item)comboBox_1.getSelectedItem();
		Sysvar.plancode=it.getValue().trim();
	}
	
	private void autocomplete(){
		String idno=textField_10.getText().trim();
		if(idno.length()==18 || idno.length()==15){
			if( idno.length()==18 ){
				String birthday=idno.substring(6, 14);
				String sex=idno.substring(16, 17);
				
				System.out.println(sex);
				String year=birthday.substring(0, 4);
				String month=birthday.substring(4, 6);
				String day=birthday.substring(6, 8);
				textField_8.setText(year+"-"+month+"-"+day);
				
				int i=Integer.parseInt(sex);
				if(i%2==0){
					textField_11.setText("女");
				}else{
					textField_11.setText("男");
				}
				
			}
			if( idno.length()==15 ){
				String birthday=idno.substring(6, 12);
				String sex=idno.substring(14, 15);
				
				System.out.println(sex);
				String year="19"+birthday.substring(0, 2);
				String month=birthday.substring(2, 4);
				String day=birthday.substring(4, 6);
				textField_8.setText(year+"-"+month+"-"+day);
				
				int i=Integer.parseInt(sex);
				if(i%2==0){
					textField_11.setText("女");
				}else{
					textField_11.setText("男");
				}
			}
		}else{
			textField_8.setText("");
			textField_11.setText("");
		}
	}
	
	//取输入控件值时先运行过滤器，去掉不合规的数据
	private boolean filter(){
		String name     =textField_7.getText().trim();
		String idno     =textField_10.getText().trim();
		String sex      =textField_11.getText().trim();
		String birthday =textField_8.getText().trim();
		
		item it=(item)comboBox_1.getSelectedItem();
		String plancode=it.getValue().trim();
		
		if(idno.length()!=18){
			return false;
		}
		if( !sex.equals("男" ) && !sex.equals("女" ) ){
			return false;
		}
		if( !birthday.matches("([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))")){
			System.out.println("日期格式有误");
			return false;
		}
		String regEx=birthday.replaceAll("-", "");
		if( !Pattern.compile(regEx).matcher( idno ).find() ){
			return false;
		}
		
		Gui.custom.idno=idno;
		Gui.custom.name=name;
		Gui.custom.birthday=birthday;
		Gui.custom.sex=formatsex1(sex);
		Gui.sysvar.plancode=plancode;
		
		return true;
		
	}
	//更新界面
	public void refresh(){
		Lock.Syslock.readLock().lock();
		
		textField.setText(domob.readFirst("/sysvar/lastpolicy/sellformnum"));
		textField_1.setText(domob.readFirst("/sysvar/lastpolicy/applicantname"));
		textField_2.setText(domob.readFirst("/sysvar/lastpolicy/applicantbirthday"));
		textField_12.setText(formatplan( domob.readFirst("/sysvar/lastpolicy/plancode") ));
		textField_3.setText(domob.readFirst("/sysvar/lastpolicy/policynum"));
		textField_4.setText(domob.readFirst("/sysvar/lastpolicy/applicantzjnum"));
		textField_5.setText( formatsex(domob.readFirst("/sysvar/lastpolicy/applicantsex")) );
		
		textField_6.setText(domob.readFirst("/sysvar/sellformnum"));
		textField_9.setText(domob.readFirst("/sysvar/policynum"));
		
		Lock.Syslock.readLock().unlock();
	}
	
	private String formatsex(String str){
		str=str.trim();
		if(str.equals("0")){
			return "男";
		}
		if(str.equals("1")){
			return "女";
		}
		return str;
	}
	private String formatplan(String plan){
		plan=plan.trim();
		//处理中山计划生育险
		if(plan.equals("Foreigner")){
			return "流动人口";
		}
		if(plan.equals("Native")){
			return "本地人口";
		}
		//处理佛山乘意险
		if(plan.equals("three")){
			return "3块钱方案";
		}
		if(plan.equals("four")){
			return "4块钱方案";
		}
		if(plan.equals("five")){
			return "5块钱方案";
		}
		return plan;
	}
	
	private String formatsex1(String str){
		if(str.equals("男")){
			return "0";
		}
		if(str.equals("女")){
			return "1";
		}
		return str;
	}
	//界面事件触发的动作
	private void save(){
		Lock.Syslock.writeLock().lock();
		
		if(filter()){
			if(!ins.go1().equals("0")){
				//手工承保成功，清空界面录入的信息
				textField_7.setText("");
				textField_10.setText("");
				textField_11.setText("");
				textField_8.setText("");
			}
			refresh();
		}else{
			JOptionPane.showMessageDialog( null, "录入的数据无法通过校验！");
		}
		
		Lock.Syslock.writeLock().unlock();
	}
	
	//退保
	private void refund(){
        String sellformnum = textField.getText().trim();
        String name        = textField_1.getText().trim();
        String idno        = textField_4.getText().trim();
		if(!sellformnum.equals("") && !name.equals("") && !idno.equals("") ){
			String sql="update insured set cease='2' ,flag='3',ceasedate=sysdate(),ceasetime=sysdate() ,backup='0' where  cease='0' ";
			sql=sql+" and name='"+name+"' and  customid='"+idno+"' and sellformno='"+sellformnum+"' ";
			
			int flag=jdbcob.exec(sql);
			if(flag==1){
				JOptionPane.showMessageDialog( null, "退保成功！"); 
				//退保后清空界面,中山计生险的要求
				textField_1.setText("");
				textField_2.setText("");
				textField_12.setText("");
				textField_4.setText("");
				textField_5.setText("");

			}else{
				JOptionPane.showMessageDialog( null, "退保失败！"); 
			}
		}else{
			JOptionPane.showMessageDialog( null, "参数不足，无法退保！"); 
		}
	}
}

class item {
	private String key="";
	private String value="";
	
    public String toString() {
        return key;
    }
    
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
