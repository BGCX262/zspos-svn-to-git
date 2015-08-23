package pos.gui;

import javax.swing.JPanel;
import pos.background.thread.datasynone;
import pos.services.jdbc;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class backup extends JPanel {

	/**
	 * Create the panel.
	 */
	private jdbc       jdbcob;    //数据库查询对象
	private JTextField textField;
	private JTextField textField_1;
	public backup(jdbc jdbcob) {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				query();
				query_ins();
			}
		});
		
		this.jdbcob=jdbcob;
		setLayout(null);
		
		JLabel label = new JLabel("90\u5929\u5185\u672A\u540C\u6B65\u7684\u6570\u636E");
		label.setBounds(63, 114, 129, 15);
		add(label);
		
		textField = new JTextField();
		textField.setForeground(Color.RED);
		textField.setEditable(false);
		textField.setBounds(198, 111, 121, 21);
		add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("\u540C\u6B65\u6570\u636E");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {			
				funtion();
			}
		});
		button.setBounds(415, 110, 93, 23);
		add(button);
		
		JButton button_1 = new JButton("\u5237\u65B0");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				query();
			}
		});
		button_1.setBounds(329, 110, 64, 23);
		add(button_1);
		
		JLabel label_1 = new JLabel("90\u5929\u5185\u672A\u627F\u4FDD\u7684\u6570\u636E");
		label_1.setBounds(63, 74, 129, 15);
		add(label_1);
		
		textField_1 = new JTextField();
		textField_1.setForeground(Color.RED);
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(198, 71, 121, 21);
		add(textField_1);
		
		JButton button_2 = new JButton("\u5237\u65B0");
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				query_ins();
			}
		});
		button_2.setBounds(329, 70, 64, 23);
		add(button_2);
	}
	private void query_ins(){
		try{
			String sql=" select count(policynum) num from insured where datediff(sysdate(),printdate)<=90 and ( insureflag='0' or cease='2' ) ";
			ResultSet rs = this.jdbcob.query(sql);
			if(rs.next()) {
				textField_1.setText(rs.getString("num"));
			}else{
				textField_1.setText("无");
			}
		}catch(Exception ex){
			JOptionPane.showMessageDialog( null, "【数据库查询出错】");
		}
	}
	private void query(){
		try{
			String sql=" select count(policynum) num from insured where datediff(sysdate(),printdate)<=90 and  backup!='1' ";
			ResultSet rs = this.jdbcob.query(sql);
			if(rs.next()) {
				textField.setText(rs.getString("num"));
			}else{
				textField.setText("无");
			}
		}catch(Exception ex){
			JOptionPane.showMessageDialog( null, "【数据库查询出错】");
		}
	}
	private void funtion(){
		try{
			datasynone  T     = new datasynone();
			new Thread(T).start();
		}catch(Exception ex){
			JOptionPane.showMessageDialog( null, "【创建备份线程出错】");
		}
	}
}
