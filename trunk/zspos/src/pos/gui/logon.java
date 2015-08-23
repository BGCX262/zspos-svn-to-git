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
	
	private Dom4j     domob=null;    //�����ĵ�����
	private auto      ins=null;      //�����Զ��б�����
	private mainframe maingui=null;  //������

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
		
		JLabel label = new JLabel("�û���:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(99, 97, 54, 15);
		contentPane.add(label);
		
		textField = new JTextField();
		textField.setBounds(184, 97, 140, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("��  ��:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(99, 133, 54, 15);
		contentPane.add(label_1);
		
		passwordField = new JPasswordField();
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					login();//��¼
				}
			}
		});
		passwordField.setBounds(184, 133, 140, 21);
		contentPane.add(passwordField);
		
		label_2 = new JLabel("POS����ϵͳ��¼����");
		label_2.setFont(new Font("����", Font.BOLD, 26));
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(38, 34, 358, 41);
		contentPane.add(label_2);
		
		button = new JButton("ȷ ��");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				login();//��¼
			}
		});
		button.setBounds(231, 176, 93, 23);
		contentPane.add(button);
		
		button_1 = new JButton("ȡ ��");
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
	//�°汾
	private void login(){
		String name=textField.getText().toString().trim();
		String pwd=String.valueOf(passwordField.getPassword()).trim();	
		if (!name.equals("") && !pwd.equals("")) {
			
			if( net.isonline() ){//�ж���������
				
				if( auth(name,pwd) ){//�û���֤

					//��������
					domob  = new Dom4j("xml/sysinfo.xml");
					ins    = new auto (domob);
					//��ʼ��ϵͳ����
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
					
					//����������
					setHide();
					maingui= new mainframe(domob,ins);
					maingui.setVisible(true);
					
					//�����Զ�ˢ���߳�
					insjob  T1 = new insjob(ins,maingui);
					new Thread(T1).start();
					
					//����ʵ���̨�б��߳�
					datains T2 =new datains();
					new Thread(T2).start();
					
				}else{
					JOptionPane.showMessageDialog( null, "���û���¼ʧ�ܣ���ֱ����û���,����,�������ӡ�");
				}
			}else{
				JOptionPane.showMessageDialog( null, "���޷����ӵ����������������ӻ�������");
			}
		} else {
			JOptionPane.showMessageDialog( null, "��������û�������Ϊ�ա�");
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
					//��ʼ���û�����
					User.userid   =doc.selectSingleNode("/transdata/description/userid").getText().trim();
					User.name     =doc.selectSingleNode("/transdata/description/username").getText().trim();
					User.pwd      =doc.selectSingleNode("/transdata/description/userpwd").getText().trim();
					User.riskcode =doc.selectSingleNode("/transdata/description/riskcode").getText().trim();
					User.outlet   =doc.selectSingleNode("/transdata/description/outlet").getText().trim();
					return true;
				}else{
					JOptionPane.showMessageDialog( null, "�����û����ѹ��ڡ�");
					return false;
				}
				
			}else{
				System.out.println("����¼ʧ�ܡ�");
				return false;
			}
		}catch(Exception ex){
			System.out.println("��ϵͳ�쳣��");
			return false;
		}finally{
			//�ͷ���Դ
			authob    = null;
			saxReader = null;
		}

	}

	// �жϵ�ǰʱ���Ƿ���ʱ��date2֮ǰ
	// ʱ���ʽ 2005-4-21
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

	// �жϵ�ǰʱ���Ƿ���ʱ��date2֮������
	// ʱ���ʽ 2005-4-21
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

	// �ж�ʱ��date1�Ƿ���ʱ��date2֮ǰ
	// ʱ���ʽ 2005-4-21
	private static boolean isDateBefore(String date1, String date2) {
		try {
			DateFormat df =  new SimpleDateFormat("yyyy-MM-dd");//DateFormat.getDateTimeInstance();
			return df.parse(date1).before(df.parse(date2));
		} catch (Exception e) {
			System.out.print("[SYS]" + e.getMessage());
			return false;
		}
	}

	
	/*	//�ɰ汾
	private boolean verify(String userid,String pwd ){
		try{ 
			jdbc oblocal = new jdbc("local");
			if (oblocal.useable) {
				return check(userid, pwd, oblocal);
			} else {
				return false;
			}
		}catch(Exception e1){ 
			JOptionPane.showMessageDialog( null, "ϵͳ�쳣��");
			return false;
		}
	}
	private boolean check(String userid,String pwd,jdbc ob){
		
		try{
			ResultSet rs=ob.query("select * from user where userid='"+userid+"' and userpwd='"+pwd+"' and valid='1' ");
			if(rs.next()){
				//���û�����Ч����
				if(  isDateBefore(rs.getString("end").toString().trim())  ){
					
					//��֤��POSϵͳ������
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
					//�ͷ���Դ
					domob=null;
					
					if(!User.riskcode.equals(Sysvar.riskcode)){
						JOptionPane.showMessageDialog( null, "���û����ܳ��������POS����ƥ�䣡");
						return false;
					}
					return true;
				}else{
					JOptionPane.showMessageDialog( null, "���û����ѹ��ڣ�");
					return false;
				}
			}else{
				JOptionPane.showMessageDialog( null, "������û�������");
				return false;
			}

		}catch(Exception e1){
			System.out.println("��¼�쳣��");
			return false;
		}
		
	}*/
	
}
