package pos.gui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

import javax.swing.JButton;

import Beans.Sysvar;
import pos.gui.lib.DateChooser;
import pos.services.jdbc;

import javax.swing.JLabel;

public class query extends JPanel {
	
	
	private DefaultTableModel jTable1Model ;  //��ѯͳ��˽�б���
	private JTable table;
	private DateChooser panel;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	
	private jdbc       jdbcob;    //���ݿ��ѯ����
	/**
	 * Create the panel.
	 */
	public query(jdbc jdbcob) {
		this.jdbcob=jdbcob;
		
		setLayout(null);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 589, 300);
		add(scrollPane);
		
		
		jTable1Model = new DefaultTableModel(new String[][] { {"", "", "" ,"","","","",""} },new String[] { "���","����", "����","���֤","��֤��","״̬","ͬ��","����" });
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow(); //���ѡ��������
                
                Object name = jTable1Model.getValueAt(selectedRow, 2);
                Object idno = jTable1Model.getValueAt(selectedRow, 1);
                Object sellformnum = jTable1Model.getValueAt(selectedRow, 4);
                
                textField_2.setText(sellformnum.toString().trim());
                textField_3.setText(name.toString().trim());
                textField_4.setText(idno.toString().trim());
			}
		});
		table.setModel(jTable1Model);
		table.getColumnModel().getColumn(0).setPreferredWidth(30); 
		table.getColumnModel().getColumn(1).setPreferredWidth(75); 
		table.getColumnModel().getColumn(2).setPreferredWidth(40); 
		table.getColumnModel().getColumn(3).setPreferredWidth(142); 
		table.getColumnModel().getColumn(4).setPreferredWidth(120); 
		table.getColumnModel().getColumn(5).setPreferredWidth(40);
		table.getColumnModel().getColumn(6).setPreferredWidth(40);
		table.getColumnModel().getColumn(7).setPreferredWidth(55);
		scrollPane.setViewportView(table);
		
		panel = new DateChooser();
		panel.setBounds(69, 310, 78, 25);
		add(panel);
		
		JLabel label = new JLabel("ѡ������");
		label.setBounds(10, 310, 54, 25);
		add(label);
		
		JButton button = new JButton("��  ѯ");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("��ѡ��ʱ�䡿"+panel.getDate1());
				query();

			}
		});
		button.setBounds(490, 310, 78, 23);
		add(button);
		
		JLabel label_1 = new JLabel("�ͻ�����");
		label_1.setBounds(157, 310, 54, 25);
		add(label_1);
		
		textField = new JTextField();
		textField.setBounds(212, 310, 71, 25);
		add(textField);
		textField.setColumns(10);
		
		JLabel label_2 = new JLabel("��֤��");
		label_2.setBounds(293, 310, 54, 25);
		add(label_2);
		
		textField_1 = new JTextField();
		textField_1.setBounds(344, 310, 125, 25);
		add(textField_1);
		textField_1.setColumns(10);
		
		JButton button_1 = new JButton("��  ��");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (JOptionPane.showConfirmDialog( null, "ȷ���˱�?", "�˱�",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
					refund();
				}
			}
		});
		button_1.setBounds(490, 345, 78, 23);
		add(button_1);
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setColumns(10);
		textField_2.setBounds(344, 345, 125, 25);
		add(textField_2);
		
		JLabel label_3 = new JLabel("\u5355\u8BC1\u53F7");
		label_3.setBounds(293, 345, 54, 25);
		add(label_3);
		
		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		textField_3.setBounds(212, 345, 71, 25);
		add(textField_3);
		
		JLabel label_4 = new JLabel("\u5BA2\u6237\u59D3\u540D");
		label_4.setBounds(157, 345, 54, 25);
		add(label_4);
		
		JLabel label_5 = new JLabel("��������");
		label_5.setBounds(10, 345, 54, 25);
		add(label_5);
		
		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setColumns(10);
		textField_4.setBounds(69, 346, 78, 25);
		add(textField_4);
		
		
	}
	//�˱�
	private void refund(){
        String sellformnum=textField_2.getText().trim();
        String name=textField_3.getText().trim();
        String ptrdate=textField_4.getText().trim();
		if(!sellformnum.equals("") && !name.equals("") && !ptrdate.equals("") ){
			String sql="update insured set cease='2' ,flag='3',ceasedate=sysdate(),ceasetime=sysdate() , backup='0' where cease='0' ";
			sql=sql+" and name='"+name+"' and  printdate='"+Sysvar.getDate().trim()+"' and sellformno='"+sellformnum+"' ";
			
			int flag=jdbcob.exec(sql);
			if(flag==1){
				JOptionPane.showMessageDialog( null, "�˱��ɹ���"); 
				//�˱�����ս���

			}else{
				JOptionPane.showMessageDialog( null, "�˱�ʧ�ܣ�"); 
			}
		}else{
			JOptionPane.showMessageDialog( null, "�������㣬�޷��˱���"); 
		}
		
	}
	//��ѯ
	private void query(){
		//��ȡ�û��������,����SQL
		String name=textField.getText().trim();
		String sellformnum=textField_1.getText().trim();
		
		String sql="select * from insured where printdate='"+ panel.getDate1().trim()+"'";
		if(!name.isEmpty()){
			sql=sql+" and name='"+name+"'";
		}
		if(!sellformnum.isEmpty()){
			sql=sql+" and sellformno like '%"+sellformnum+"%'";
		}
		sql=sql+" order by printtime desc";
		//��ѯquery
		try{
			int i=1;
			boolean b=false;
			jTable1Model.getDataVector().removeAllElements();//���ԭ��TableModel����
			ResultSet rs = jdbcob.query(sql);
			while (rs.next()) {
				//System.out.println(rs.getString("policynum"));
				jTable1Model.addRow( new String [] { String.valueOf(i++),rs.getString("printdate"),rs.getString("name"),rs.getString("customid"),rs.getString("sellformno"),format2flag(rs.getString("flag")),format2backup(rs.getString("backup")),rs.getString("outletid") } );
				b=true;
			}
			if(!b){
				JOptionPane.showMessageDialog( null, "�鲻�����ݣ�");
				clean();
				jTable1Model.getDataVector().removeAllElements();//���ԭ��TableModel����
			}
			table.repaint();
		}
		catch(Exception e){
			clean();
	        JOptionPane.showMessageDialog( null, "���ݿ��ѯ������رճ�����������"); 
		}
	}
	private String format2flag(String str){
		if(str.equals("0")){
			return "δ����";
		}
		if(str.equals("1")){
			return "�ѳ���";
		}
		if(str.equals("2")){
			return "�ѳб�";
		}
		if(str.equals("3")){
			return "���˱�";
		}
		if(str.equals("4")){
			return "���˱�";
		}
		return str;
	}
	private String format2backup(String str){
		str=str.trim();
		if(str.equals("0")){
			return "δͬ��";
		}
		if(str.equals("1")){
			return "��ͬ��";
		}
		if(str.equals("2")){
			return "δͬ��";
		}
		return str;
	}
	
	private void clean(){
        textField_2.setText("");
        textField_3.setText("");
        textField_4.setText("");
	}
}
