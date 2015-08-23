package pos.gui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JLabel;

import pos.gui.lib.DateChooser;
import pos.services.jdbc;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

public class statistic extends JPanel {
	private DefaultTableModel jTable1Model ;  //查询统计私有变量
	private JTable table;
	
	private DateChooser panel;
	private DateChooser panel_1;
	
	private jdbc        jdbcob;    //数据库查询对象

	/**
	 * Create the panel.
	 */
	public statistic(jdbc jdbcob) {
		this.jdbcob=jdbcob;
		setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 592, 330);
		add(scrollPane);
		
		jTable1Model = new DefaultTableModel(new String[][] { { "", "", "", "", "" } },new String[] { "日期", "当日出单", "当日承保","当日退保","实际承保" });
		table = new JTable();
		table.setModel(jTable1Model);
		scrollPane.setViewportView(table);
		
		panel = new DateChooser();
		panel.setBounds(100, 340, 85, 25);
		add(panel);
		
		panel_1 = new DateChooser();
		panel_1.setBounds(287, 340, 85, 25);
		add(panel_1);
		
		JButton button = new JButton("查  询");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				query();
			}
		});
		button.setBounds(444, 340, 93, 23);
		add(button);
		
		JLabel label = new JLabel("开始日期");
		label.setBounds(41, 340, 54, 25);
		add(label);
		
		JLabel label_1 = new JLabel("结束日期");
		label_1.setBounds(230, 340, 54, 25);
		add(label_1);

	}
	private void query(){
		
		String begin = panel.getDate1();
		String end   = panel_1.getDate1();
		String con1=" and datediff(sysdate(),printdate)<=60 ";
		if(!begin.isEmpty()){
			con1=" and printdate>='"+begin+"' ";
		}
		if(!end.isEmpty()){
			con1=con1+" and printdate<='"+end+"' ";
		}
			
		String sql=" select t1.*, coalesce(t2.succ, 0),coalesce(t3.unsucc, 0),coalesce(t2.succ, 0)-coalesce(t3.unsucc, 0) from ";
		sql=sql+   " ( select printdate,count(sellformno) as ptr    from insured where printflag='1'   "+con1+"   group by printdate )t1  left join ";
		sql=sql+   " ( select printdate,count(sellformno) as succ   from insured where insureflag='1'  "+con1+"   group by printdate )t2  on t1.printdate=t2.printdate left join ";
		sql=sql+   " ( select printdate,count(sellformno) as unsucc from insured where cease='1'       "+con1+"   group by printdate )t3  on t1.printdate=t3.printdate; ";
		//System.out.println(sql);
		//查询query
		jTable1Model.getDataVector().removeAllElements();//清空原来TableModel数据
		try{
			boolean b=false;
			ResultSet rs = jdbcob.query(sql);
			while (rs.next()) {
				jTable1Model.addRow( new String [] { rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)} );
				b=true;
			}
			if(!b){
				JOptionPane.showMessageDialog( null, "查不到数据！");
			}
		}
		catch(Exception e){
	        JOptionPane.showMessageDialog( null, "数据库查询出错，请关闭程序再重启！"); 
		}			
		table.repaint();  //刷新表格数据		
		
		
	}
}
