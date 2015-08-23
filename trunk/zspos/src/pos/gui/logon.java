package pos.gui;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import Beans.Sysvar;
import Beans.User;
import pos.background.thread.datains;
import pos.background.thread.insjob;
import pos.mysoap.services.mysoapAuth;
import pos.services.Dom4j;
import Model.auto;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import pos.util.net;
public class logon extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JLabel label_2;
	private JButton button;
	private JButton button_1;
	
	private Dom4j     domob=null;    //创建文档对象
	private auto      ins=null;      //创建自动承保对象
	private mainframe maingui=null;  //主界面

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					logon frame = new logon();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public logon() {
		int width=450;
		int height=300;
		int x = (Toolkit.getDefaultToolkit().getScreenSize().width - width) / 2;
		int y = (Toolkit.getDefaultToolkit().getScreenSize().height - height) / 2;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(x, y, width, height);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("用户名:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(99, 97, 54, 15);
		contentPane.add(label);
		
		textField = new JTextField();
		textField.setBounds(184, 97, 140, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("密  码:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(99, 133, 54, 15);
		contentPane.add(label_1);
		
		passwordField = new JPasswordField();
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					login();//登录
				}
			}
		});
		passwordField.setBounds(184, 133, 140, 21);
		contentPane.add(passwordField);
		
		label_2 = new JLabel("POS出单系统登录界面");
		label_2.setFont(new Font("宋体", Font.BOLD, 26));
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(38, 34, 358, 41);
		contentPane.add(label_2);
		
		button = new JButton("确 定");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				login();//登录
			}
		});
		button.setBounds(231, 176, 93, 23);
		contentPane.add(button);
		
		button_1 = new JButton("取 消");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				textField.setText("");
				passwordField.setText("");
				
			}
		});
		button_1.setBounds(112, 176, 93, 23);
		contentPane.add(button_1);
	}
	
	private void setHide(){
		this.setVisible(false);
	}
	//新版本
	private void login(){
		String name=textField.getText().toString().trim();
		String pwd=String.valueOf(passwordField.getPassword()).trim();	
		if (!name.equals("") && !pwd.equals("")) {
			
			if( net.isonline() ){//判断网络连接
				
				if( auth(name,pwd) ){//用户认证

					//创建对象
					domob  = new Dom4j("xml/sysinfo.xml");
					ins    = new auto (domob);
					//初始化系统参数
					Sysvar.startsellformnum =domob.readFirst("/sysvar/startsellformnum").trim();
					Sysvar.endsellformnum   =domob.readFirst("/sysvar/endsellformnum").trim();
					Sysvar.printertype      =domob.readFirst("/sysvar/printertype").trim();
					Sysvar.riskcode         =domob.readFirst("/sysvar/riskcode").trim();
					Sysvar.sellformtype     =domob.readFirst("/sysvar/sellformtype").trim();
					Sysvar.poscode          =domob.readFirst("/sysvar/poscode").trim();
//					Sysvar.agentcode        =domob.readFirst("/sysvar/agentcode").trim();
//					Sysvar.agentname        =domob.readFirst("/sysvar/agentname").trim();
					
					Sysvar.idnorule         =domob.readFirst("/sysvar/idnorule").trim();
					Sysvar.sellformrule     =domob.readFirst("/sysvar/sellformrule").trim();
					Sysvar.headnum          =domob.readFirst("/sysvar/headnum").trim();
					Sysvar.interval         =domob.readFirst("/sysvar/interval").trim();
					
					//创建主界面
					setHide();
					maingui= new mainframe(domob,ins);
					maingui.setVisible(true);
					
					//创建自动刷卡线程
					insjob  T1 = new insjob(ins,maingui);
					new Thread(T1).start();
					
					//创建实达后台承保线程
					datains T2 =new datains();
					new Thread(T2).start();
					
				}else{
					JOptionPane.showMessageDialog( null, "【用户登录失败，请分别检查用户名,密码,网络连接】");
				}
			}else{
				JOptionPane.showMessageDialog( null, "【无法连接到服务器，请先连接互联网】");
			}
		} else {
			JOptionPane.showMessageDialog( null, "【密码或用户名不能为空】");
		}
	}
	private boolean auth( String userid,String pwd ){
		mysoapAuth authob    = null;
		SAXReader  saxReader = null;
		try{
			saxReader = new SAXReader();
			Document  document = saxReader.read("xml/mysoap/save.xml");
			document.selectSingleNode("/transdata/baseinfo/userid").setText(userid);
			document.selectSingleNode("/transdata/baseinfo/userpwd").setText(pwd);
			
			authob=new mysoapAuth();
			Document doc=authob.auth(document);
			if( doc!=null ){
				
				String end=doc.selectSingleNode("/transdata/description/end").getText().trim();
				if( isDateBefore( end )){
					//初始化用户参数
					User.userid   =doc.selectSingleNode("/transdata/description/userid").getText().trim();
					User.name     =doc.selectSingleNode("/transdata/description/username").getText().trim();
					User.pwd      =doc.selectSingleNode("/transdata/description/userpwd").getText().trim();
					User.riskcode =doc.selectSingleNode("/transdata/description/riskcode").getText().trim();
					User.outlet   =doc.selectSingleNode("/transdata/description/outlet").getText().trim();
					return true;
				}else{
					JOptionPane.showMessageDialog( null, "【该用户名已过期】");
					return false;
				}
				
			}else{
				System.out.println("【登录失败】");
				return false;
			}
		}catch(Exception ex){
			System.out.println("【系统异常】");
			return false;
		}finally{
			//释放资源
			authob    = null;
			saxReader = null;
		}

	}

	// 判断当前时间是否在时间date2之前
	// 时间格式 2005-4-21
	private static boolean isDateBefore(String date2) {
		try {
			Date date1 = new Date(); 
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return date1.before(df.parse(date2));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	// 判断当前时间是否在时间date2之后或相等
	// 时间格式 2005-4-21
	private static boolean isDateAfterOrEqual(String date2) {
		boolean flag = false;
		try {
			DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = new Date();
			if (fmt.parse(fmt.format(date1)).equals(fmt.parse(date2))
					|| fmt.parse(fmt.format(date1)).after(fmt.parse(date2)))
				flag = true;
		} catch (Exception e) {
			System.out.print("[SYS] " + e.getMessage());
			flag = false;
		}
		return flag;
	}

	// 判断时间date1是否在时间date2之前
	// 时间格式 2005-4-21
	private static boolean isDateBefore(String date1, String date2) {
		try {
			DateFormat df =  new SimpleDateFormat("yyyy-MM-dd");//DateFormat.getDateTimeInstance();
			return df.parse(date1).before(df.parse(date2));
		} catch (Exception e) {
			System.out.print("[SYS]" + e.getMessage());
			return false;
		}
	}

	
	/*	//旧版本
	private boolean verify(String userid,String pwd ){
		try{ 
			jdbc oblocal = new jdbc("local");
			if (oblocal.useable) {
				return check(userid, pwd, oblocal);
			} else {
				return false;
			}
		}catch(Exception e1){ 
			JOptionPane.showMessageDialog( null, "系统异常！");
			return false;
		}
	}
	private boolean check(String userid,String pwd,jdbc ob){
		
		try{
			ResultSet rs=ob.query("select * from user where userid='"+userid+"' and userpwd='"+pwd+"' and valid='1' ");
			if(rs.next()){
				//该用户在有效期内
				if(  isDateBefore(rs.getString("end").toString().trim())  ){
					
					//验证该POS系统的配置
					Dom4j domob=new Dom4j("xml/sysinfo.xml");
					
					User.userid   =rs.getString("userid").trim();
					User.name     =rs.getString("username").trim();
					User.pwd      =rs.getString("userpwd").trim();
					User.riskcode =rs.getString("riskcode").trim();
					User.outlet   =rs.getString("outlet").trim();
					
					Sysvar.startsellformnum =domob.readFirst("/sysvar/startsellformnum").trim();
					Sysvar.endsellformnum   =domob.readFirst("/sysvar/endsellformnum").trim();
					Sysvar.printertype      =domob.readFirst("/sysvar/printertype").trim();
					Sysvar.riskcode         =domob.readFirst("/sysvar/riskcode").trim();
					Sysvar.sellformtype     =domob.readFirst("/sysvar/sellformtype").trim();
					Sysvar.poscode          =domob.readFirst("/sysvar/poscode").trim();
					Sysvar.agentcode        =domob.readFirst("/sysvar/agentcode").trim();
					Sysvar.agentname        =domob.readFirst("/sysvar/agentname").trim();
					
					Sysvar.idnorule         =domob.readFirst("/sysvar/idnorule").trim();
					Sysvar.sellformrule     =domob.readFirst("/sysvar/sellformrule").trim();
					Sysvar.headnum          =domob.readFirst("/sysvar/headnum").trim();
					Sysvar.interval         =domob.readFirst("/sysvar/interval").trim();
					//释放资源
					domob=null;
					
					if(!User.riskcode.equals(Sysvar.riskcode)){
						JOptionPane.showMessageDialog( null, "该用户名能出险种与该POS机不匹配！");
						return false;
					}
					return true;
				}else{
					JOptionPane.showMessageDialog( null, "该用户名已过期！");
					return false;
				}
			}else{
				JOptionPane.showMessageDialog( null, "密码或用户名错误！");
				return false;
			}

		}catch(Exception e1){
			System.out.println("登录异常！");
			return false;
		}
		
	}*/
	
}
