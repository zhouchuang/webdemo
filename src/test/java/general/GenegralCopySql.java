package general;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;


public class GenegralCopySql {
	/*public static final String driver = "com.mysql.jdbc.Driver";
	public static final String url = "jdbc:mysql://192.168.1.115:3306/beoro01?useUnicode=true&amp;characterEncoding=utf-8&amp;autoReconnect=true&amp;failOverReadOnly=false";
	public static final String username = "mhome";
	public static final String password = "zzzzzz";*/
	public static final String driver = "oracle.jdbc.driver.OracleDriver";
	public static final String url = "jdbc:oracle:thin:@10.100.6.108:1522/owd01";
	public static final String username = "ECP_CGN_DEV";
	public static final String password = "ECP_CGN_DEV";
	public static String COPY_SQL = "insert into ${tableName} (${columns})  select ${columns} from ${tableName}";
	
	
	public static final String tableName = "ECP_ASSIGN_RECORD";
	
	public static void main(String[] args) {
		Connection conn = null;
		try{
			Class.forName(driver);// 动态加载mysql驱动
			 
		    System.out.println("成功加载MySQL驱动程序");
		    // 一个Connection代表一个数据库连接
		    conn = DriverManager.getConnection(url,username,password);
			DatabaseMetaData metaData = conn.getMetaData();
		    ResultSet rs = metaData.getColumns(conn.getCatalog(), username.toUpperCase(), tableName.toUpperCase(), null);
		    String columns = "";
		    while(rs.next()) {   
		    	columns+=rs.getString("COLUMN_NAME")+",";
		    }
		    if(columns.length()>0){
		    	columns = columns.substring(0,columns.length()-1);
		    }
		    String sql  = COPY_SQL.replace("${tableName}", tableName);
		    sql= sql.replace("${columns}", columns);
		    System.out.println(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
    
}

