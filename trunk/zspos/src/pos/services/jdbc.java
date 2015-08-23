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
	private Statement stmtQuery = null;  //由于Query 与  Execute 共用一个Statement 会相互影响，所以创建连个Statement
	private Statement stmtExec = null;   //（执行Query后再执行Execute,先前用Query得到的ResultSet就会关闭 ）

	public boolean useable = false;
	public jdbc(String str){
		// 装载驱动类,建立JDBC连接
		try { 
			Class.forName("com.mysql.jdbc.Driver");
			
			String url="jdbc:mysql://localhost:3306/posv1";
			String user="root";
			String password="mysql";
			//创建本地数据库连接
			if(str.equals("local")){
				url="jdbc:mysql://localhost:3306/posv1";
				user="root";
				password="mysql";
			}
			//创建远程数据库连接
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
			System.out.println("链接数据库异常！");
			e.printStackTrace();
			close();
		}
	}
	public ResultSet query(String sql) {
		
		if(useable==false){
			return null;
		}
		try {
			// 执行查询
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
//		//查询query
//		ResultSet rs = ob.query("select * from insured where datediff(sysdate(),printdate)<=3" );
//		try{
//			System.out.println("开始读数....");
//			while (rs.next()) {
//				System.out.println(rs.getString("policynum"));
//			}
//		}
//		catch(Exception e){
//			System.out.println("Sorry");
//		}
//	}

}
