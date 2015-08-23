package pos.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class jdbc {

	/**
	 * @param args
	 */
	private Connection conn = null;
	private Statement stmtQuery = null;  //����Query ��  Execute ����һ��Statement ���໥Ӱ�죬���Դ�������Statement
	private Statement stmtExec = null;   //��ִ��Query����ִ��Execute,��ǰ��Query�õ���ResultSet�ͻ�ر� ��

	public boolean useable = false;
	public jdbc(String str){
		// װ��������,����JDBC����
		try { 
			Class.forName("com.mysql.jdbc.Driver");
			
			String url="jdbc:mysql://localhost:3306/posv1";
			String user="root";
			String password="mysql";
			//�����������ݿ�����
			if(str.equals("local")){
				url="jdbc:mysql://localhost:3306/posv1";
				user="root";
				password="mysql";
			}
			//����Զ�����ݿ�����
			if(str.equals("remote")){
				url="jdbc:mysql://121.8.107.14:3306/posv1";
				user="linrun";
				password="linrun";
			}
			conn      = DriverManager.getConnection(url, user, password);
			stmtExec  = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
			stmtQuery = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
			
			useable = true;

		} catch (Exception e) {
			useable=false;
			System.out.println("�������ݿ��쳣��");
			e.printStackTrace();
			close();
		}
	}
	public ResultSet query(String sql) {
		
		if(useable==false){
			return null;
		}
		try {
			// ִ�в�ѯ
			ResultSet  rs = stmtQuery.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			close();

			useable=false;
			return null;
		} 
	}
	
	public int exec(String sql)
	{
		if(useable==false){
			return -1;
		}
		
		try {
			int i=0;
			i=stmtExec.executeUpdate(sql);
			return i;
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			close();
			
			useable=false;
			return -1;
		} 
	}
	public void close( )
	{
		try {
			
			if (stmtExec != null) {
				stmtExec.close();
				stmtExec = null;
			}
			if (stmtQuery != null) {
				stmtQuery.close();
				stmtQuery = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}finally{
			useable=false;
		}
	}	
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		jdbc ob=new jdbc("local");
//		//��ѯquery
//		ResultSet rs = ob.query("select * from insured where datediff(sysdate(),printdate)<=3" );
//		try{
//			System.out.println("��ʼ����....");
//			while (rs.next()) {
//				System.out.println(rs.getString("policynum"));
//			}
//		}
//		catch(Exception e){
//			System.out.println("Sorry");
//		}
//	}

}
