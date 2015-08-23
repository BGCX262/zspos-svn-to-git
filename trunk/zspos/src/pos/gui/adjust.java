package pos.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import Beans.Lock;
import Beans.Sysvar;
import pos.services.Dom4j;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigInteger;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
public class adjust extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	
	private Dom4j      domob;
	/**
	 * Create the panel.
	 */
	public adjust(Dom4j dom) {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				refresh();
			}
		});
		domob=dom;
		setLayout(null);
		
		JLabel label = new JLabel("\u672C\u6279\u6B21\u5355\u8BC1\u8303\u56F4");
		label.setBounds(83, 41, 98, 15);
		add(label);
		
		JLabel label_1 = new JLabel("\u5230");
		label_1.setBounds(332, 41, 23, 15);
		add(label_1);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBounds(191, 38, 131, 21);
		add(textField);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(355, 38, 131, 21);
		add(textField_1);
		
		JLabel label_2 = new JLabel("\u5F53\u524D\u5355\u8BC1\u53F7");
		label_2.setBounds(108, 82, 73, 15);
		add(label_2);
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setColumns(10);
		textField_2.setBounds(191, 79, 131, 21);
		add(textField_2);
		
		JLabel label_3 = new JLabel("\u6B63\u786E\u5355\u8BC1\u53F7");
		label_3.setBounds(108, 123, 73, 15);
		add(label_3);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(191, 120, 131, 21);
		add(textField_3);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(41, 177, 497, 2);
		add(separator);
		
		JButton button = new JButton("\u63D0  \u4EA4");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				doadjust();
			}
		});
		button.setBounds(191, 204, 131, 23);
		add(button);
	}

	public void refresh(){
		Lock.Syslock.readLock().lock();
		textField.setText(domob.readFirst("/sysvar/startsellformnum"));
		textField_1.setText(domob.readFirst("/sysvar/endsellformnum"));
		textField_2.setText(domob.readFirst("/sysvar/sellformnum"));
		Lock.Syslock.readLock().unlock();
	}
	
	
	//新版本
	private void doadjust(){
		String begin    = textField.getText().trim();
		String end      = textField_1.getText().trim();
		String point    = textField_2.getText().trim();
		String newpoint = textField_3.getText().trim();
		try{
			if(begin.length()==14 && end.length()==14 && newpoint.length()==14 ){
				BigInteger big1=new BigInteger(newpoint);
				BigInteger big2=new BigInteger(begin);
				BigInteger big3=new BigInteger(end);
				BigInteger big4=new BigInteger(point);
				if( big1.compareTo(big2)!=-1 && big1.compareTo(big3)!=1 && big1.compareTo(big4)==1){
					
					domob.write("/sysvar/sellformnum",Sysvar.pad2sellformnum(big1).trim());
					domob.write("/sysvar/policynum",Sysvar.pad2policynum(big1).trim());
					if(domob.save()){
						Sysvar.sellformnum =domob.readFirst("/sysvar/sellformnum");
						Sysvar.policynum   =domob.readFirst("/sysvar/policynum");
						JOptionPane.showMessageDialog( null, "【单证校正成功】");
					}else{
						JOptionPane.showMessageDialog( null, "【单证校正失败】");
					}
					refresh();
					
				}else{
					JOptionPane.showMessageDialog( null, "【单证校正失败,正确单证号不在可用范围】");
				}
			}else{
				JOptionPane.showMessageDialog( null, "【录入的数据无法通过校验】");
			}
		}catch(Exception ex){
			JOptionPane.showMessageDialog( null, "【单证校正异常】");
		}

	}
}
