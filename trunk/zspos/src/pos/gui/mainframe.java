package pos.gui;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

import Beans.User;
import Model.auto;

import pos.services.Dom4j;
import pos.services.jdbc;
import javax.swing.JLabel;

public class mainframe extends JFrame {

	private JPanel contentPane;
	private JLabel label_1=null;
	//五个功能界面
	private insure    panel1=null;
	private query     panel2=null;
	private statistic panel3=null;
	private store     panel4=null;
	private adjust    panel5=null;
	private backup    panel6=null;
	//1.创建文档对象
	private Dom4j domob;
	//2.创建自动承保对象
	private auto  ins;
	//3.数据库查询对象
	private jdbc  jdbcob;   

	/**
	 * Create the frame.
	 */
	public mainframe(Dom4j domob,auto  ins) {
		int width=600;//600
		int height=468;//468
		int x = (Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2;
		int y = (Toolkit.getDefaultToolkit().getScreenSize().height - height) / 2;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(x, y, width, height);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 592, 413);
		contentPane.add(tabbedPane);
		
		//创建对象
		this.domob  = domob;
		this.ins    = ins;
		jdbcob = new jdbc("local");
		
		//初始化界面
		panel1 = new insure(domob,ins,jdbcob);
		tabbedPane.addTab("出单界面", null, panel1, null);
		
		panel2 = new query(jdbcob);
		tabbedPane.addTab("保单查询", null, panel2, null);
		panel3 = new statistic(jdbcob);
		tabbedPane.addTab("保单统计", null, panel3, null);
		
		panel4 = new store(domob);
		tabbedPane.addTab("单证入库", null, panel4, null);
		panel5 = new adjust(domob);
		tabbedPane.addTab("单证校正", null, panel5, null);
		
		panel6 = new backup(jdbcob);
		tabbedPane.addTab("数据检查", null, panel6, null);
		
		JLabel label = new JLabel("\u7528\u6237\u540D:");
		label.setBounds(10, 412, 45, 22);
		contentPane.add(label);
		
		label_1 = new JLabel("");
		label_1.setBounds(54, 412, 54, 22);
		contentPane.add(label_1);
		
		//刷新界面
		refresh();
		
	}
	//获得方案
	public void getPlancode(){
		panel1.getPlancode();
	}
	//刷新界面
	public void refresh(){
		panel1.refresh();
		panel5.refresh();
		label_1.setText(User.name.trim());
	}
}
