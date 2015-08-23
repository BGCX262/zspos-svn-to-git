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
	
	
	private DefaultTableModel jTable1Model ;  //查询统计私有变量
	private JTable table;
	private DateChooser panel;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	
	private jdbc       jdbcob;    //数据库查询对象
	/**
	 * Create the panel.
	 */
	public query(jdbc jdbcob) {
		this.jdbcob=jdbcob;
		
		setLayout(null);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 589, 300);
		add(scrollPane);
		
		
		jTable1Model = new DefaultTableModel(new String[][] { {"", "", "" ,"","","","",""} },new String[] { "序号","日期", "姓名","身份证","单证号","状态","同步","网点" });
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow(); //获得选中行索引
                
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
		
		JLabel label = new JLabel("选择日期");
		label.setBounds(10, 310, 54, 25);
		add(label);
		
		JButton button = new JButton("查  询");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("【选择时间】"+panel.getDate1());
				query();

			}
		});
		button.setBounds(490, 310, 78, 23);
		add(button);
		
		JLabel label_1 = new JLabel("客户姓名");
		label_1.setBounds(157, 310, 54, 25);
		add(label_1);
		
		textField = new JTextField();
		textField.setBounds(212, 310, 71, 25);
		add(textField);
		textField.setColumns(10);
		
		JLabel label_2 = new JLabel("单证号");
		label_2.setBounds(293, 310, 54, 25);
		add(label_2);
		
		textField_1 = new JTextField();
		textField_1.setBounds(344, 310, 125, 25);
		add(textField_1);
		textField_1.setColumns(10);
		
		JButton button_1 = new JButton("退  保");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (JOptionPane.showConfirmDialog( null, "确认退保?", "退保",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
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
		
		JLabel label_5 = new JLabel("出单日期");
		label_5.setBounds(10, 345, 54, 25);
		add(label_5);
		
		textField_4 = new JTextField();
		textField_4.setEditable(false);
		textField_4.setColumns(10);
		textField_4.setBounds(69, 346, 78, 25);
		add(textField_4);
		
		
	}
	//退保
	private void refund(){
        String sellformnum=textField_2.getText().trim();
        String name=textField_3.getText().trim();
        String ptrdate=textField_4.getText().trim();
		if(!sellformnum.equals("") && !name.equals("") && !ptrdate.equals("") ){
			String sql="update insured set cease='2' ,flag='3',ceasedate=sysdate(),ceasetime=sysdate() , backup='0' where cease='0' ";
			sql=sql+" and name='"+name+"' and  printdate='"+Sysvar.getDate().trim()+"' and sellformno='"+sellformnum+"' ";
			
			int flag=jdbcob.exec(sql);
			if(flag==1){
				JOptionPane.showMessageDialog( null, "退保成功！"); 
				//退保后清空界面

			}else{
				JOptionPane.showMessageDialog( null, "退保失败！"); 
			}
		}else{
			JOptionPane.showMessageDialog( null, "参数不足，无法退保！"); 
		}
		
	}
	//查询
	private void query(){
		//获取用户输入参数,构造SQL
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
		//查询query
		try{
			int i=1;
			boolean b=false;
			jTable1Model.getDataVector().removeAllElements();//清空原来TableModel数据
			ResultSet rs = jdbcob.query(sql);
			while (rs.next()) {
				//System.out.println(rs.getString("policynum"));
				jTable1Model.addRow( new String [] { String.valueOf(i++),rs.getString("printdate"),rs.getString("name"),rs.getString("customid"),rs.getString("sellformno"),format2flag(rs.getString("flag")),format2backup(rs.getString("backup")),rs.getString("outletid") } );
				b=true;
			}
			if(!b){
				JOptionPane.showMessageDialog( null, "查不到数据！");
				clean();
				jTable1Model.getDataVector().removeAllElements();//清空原来TableModel数据
			}
			table.repaint();
		}
		catch(Exception e){
			clean();
	        JOptionPane.showMessageDialog( null, "数据库查询出错，请关闭程序再重启！"); 
		}
	}
	private String format2flag(String str){
		if(str.equals("0")){
			return "未出单";
		}
		if(str.equals("1")){
			return "已出单";
		}
		if(str.equals("2")){
			return "已承保";
		}
		if(str.equals("3")){
			return "待退保";
		}
		if(str.equals("4")){
			return "已退保";
		}
		return str;
	}
	private String format2backup(String str){
		str=str.trim();
		if(str.equals("0")){
			return "未同步";
		}
		if(str.equals("1")){
			return "已同步";
		}
		if(str.equals("2")){
			return "未同步";
		}
		return str;
	}
	
	private void clean(){
        textField_2.setText("");
        textField_3.setText("");
        textField_4.setText("");
	}
}
