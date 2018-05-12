package general;


import user.zc.service.base.DictCacheService;
import user.zc.utils.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GenegralTool {

	/*public static String tableName = "shiro_permission";
	public static String name = "Permission";*/
	public static final String packageName  = "user.zc";
	public static final String databaseName = "tool";

	
	public static final String pkId = "id";
	//以下4个列必须字段 ，一般以前的表里面都会有，或者换一个名字CREATE_DATE 之类  请一一对应好，如果没有此些字段  生成代码的时候会自动往表里面添加这些字段
	public static final String createdBy = "createBy";//CREATOR     创建人
	public static final String createdDate = "createTime";//CREATE_TIME   创建时间
	public static final String modifiedBy = "updateBy";//MODIFIER      修改人
	public static final String modifiedDate = "updateTime";//MODIFY_TIME   修改时间
	public static final boolean addColumnAble = false;
	public static String rootPath=GenegralTool.class.getResource("/").getFile().toString()+ "/general/";

	public static final String space = "    ";
	public static final String filterProperty = ";id;createBy;createTime;updateBy;updateTime;isDeleted;version;";
	//public static final String filterMapper = ";ID;CREATED_BY;CREATED_DATE;DELETED_BY;DELETED_DATE;IS_DELETED;IS_MODIFIED;IS_SELECTED;LAST_MODIFIED_BY;LAST_MODIFIED_DATE;VERSION;";
	public static final String filterMapper = ";id;createBy;createTime;updateBy;updateTime;isDeleted;version;";
	//public static final String alterColumn = " VERSION  number default 1 ,CREATED_BY  VARCHAR2(50) , CREATED_DATE  TIMESTAMP(6) ,DELETED_BY  VARCHAR2(50),DELETED_DATE  TIMESTAMP(6) ,IS_DELETED  VARCHAR2(1) default '0' ,LAST_MODIFIED_DATE  TIMESTAMP(6),LAST_MODIFIED_BY  VARCHAR2(50) ";
	public static final String alterColumn = " VERSION  number default 1 ,DELETED_BY  VARCHAR2(50),DELETED_DATE  TIMESTAMP(6) ,IS_DELETED  VARCHAR2(1) default '0' ";
	//public static final String alterColumnNeedID = " ID VARCHAR2(50) ,VERSION  number default 1 ,CREATED_BY  VARCHAR2(50) , CREATED_DATE  TIMESTAMP(6) ,DELETED_BY  VARCHAR2(50),DELETED_DATE  TIMESTAMP(6) ,IS_DELETED  VARCHAR2(1) default '0' ,LAST_MODIFIED_DATE  TIMESTAMP(6),LAST_MODIFIED_BY  VARCHAR2(50) ";
	/**
	 * @param args
	 */

	public static String driver ;
	public static String url ;
	public static String username ;
	public static String password ;
	public static void main(String[] args)throws Exception{
		Properties properties=new Properties();
		//获得输入流
		InputStream is=GenegralTool.class.getClassLoader().getResourceAsStream("jdbc.properties");
		System.out.println("我正在读取文件");
		properties.load(is);
		System.out.println("我成功读取");
		is.close();
		 driver = (String)properties.get("jdbc.driver");
		 url = (String)properties.get("jdbc.url");
		 username = (String)properties.get("jdbc.username");
		 password = (String)properties.get("jdbc.password");

		batchGeneral();
//		general("news","News");
	}


	public static void batchGeneral()throws Exception{
		Connection conn = null;
		Map<String,String> tableMap = new HashMap<String,String>();
		try {
			// 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，
			// 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以
			Class.forName(driver);// 动态加载mysql驱动

			System.out.println("成功加载MySQL驱动程序");
			// 一个Connection代表一个数据库连接
			conn = DriverManager.getConnection(url, username, password);
			// Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等
			Statement colmunment = conn.createStatement();
			ResultSet result = colmunment.executeQuery("select table_name from information_schema.tables where table_schema='"+databaseName+"' and table_type='base table'");
			while(result.next()){
				String tableName = result.getString("table_name");
				String name = StringUtils.getFirstCharToUpper(tableName.replace("_",""));
				tableMap.put(tableName,name);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			conn.close();
		}


		for(String key : tableMap.keySet()){
			try {
				general(key,tableMap.get(key));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void general(String tableName,String name)throws Exception {
        String sql;
        // MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值
        // 避免中文乱码要指定useUnicode和characterEncoding
        // 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，
        // 下面语句之前就要先创建javademo数据库

		Connection conn = null;
		try {
            // 之所以要使用下面这条语句，是因为要使用MySQL的驱动，所以我们要把它驱动起来，
            // 可以通过Class.forName把它加载进去，也可以通过初始化来驱动起来，下面三种形式都可以
            Class.forName(driver);// 动态加载mysql驱动

            System.out.println("成功加载MySQL驱动程序");
            // 一个Connection代表一个数据库连接
            conn = DriverManager.getConnection(url,username,password);
            // Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等



            Map<String,String>  comments = new HashMap<String,String>();
            //获取列注释
            Statement colmunment = conn.createStatement();
//            ResultSet result = colmunment.executeQuery("select * from user_col_comments where table_name = '"+tableName.toUpperCase()+"'");
			ResultSet result = colmunment.executeQuery("select COLUMN_NAME,column_comment from INFORMATION_SCHEMA.Columns where table_name='"+tableName+"' and table_schema='ijob'");
            while(result.next()){
            	comments.put(result.getString("COLUMN_NAME"), result.getString("column_comment"));
            }
            //获取列属性
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet rs = metaData.getColumns(conn.getCatalog(), username.toUpperCase(), tableName.toUpperCase(), null);
            ClassModel cm  = new ClassModel();
            cm.setName(StringUtils.getFirstCharToLower(name));
            cm.setPackageName(packageName);
            cm.setTableName(tableName);
            cm.setPkId(pkId);
            cm.setCreateBy(createdBy);
            cm.setCreateTime(createdDate);
            cm.setUpdateBy(modifiedBy);
            cm.setUpdateTime(modifiedDate);
            while(rs.next()) {
              if(!pkId.toUpperCase().equals(rs.getString("COLUMN_NAME").toUpperCase())){
            	  Column column = new Column();
            	  column.setColumnSize(Integer.parseInt(rs.getString("DECIMAL_DIGITS")!=null?rs.getString("DECIMAL_DIGITS"):"0"));
				  String str = rs.getString("COLUMN_SIZE");
				  if(StringUtils.isEmpty(str)){
				  	str = "0";
				  }
            	  column.setDecimalNum(Integer.parseInt(str));
                  column.setColumnName(rs.getString("COLUMN_NAME"));
                  column.setColumnType(rs.getString("TYPE_NAME"));
                  column.setRemarks(comments.get(rs.getString("COLUMN_NAME")));
                  column.setDefaultValue(rs.getString("COLUMN_DEF"));
                  cm.addColumn(column);
              }
            }
            //新增基本列
            Statement statement = conn.createStatement();
            if(pkId.toUpperCase().equals("ID")){
            	try{
            		statement.executeUpdate("alter table "+cm.getTableName()+" add (id VARCHAR(36)) ");
            	}catch(SQLException e){
            		System.out.println(cm.getTableName()+"已经存在'ID'列\n"+e.getMessage());
            	}
    		}
			 //CREATOR
			try{
				statement.executeUpdate("alter table "+cm.getTableName()+" add ("+createdBy+"  VARCHAR(36)) ");
			}catch(SQLException e){
				System.out.println(cm.getTableName()+"已经存在'"+createdBy+"'列\n"+e.getMessage());
			}
			//CREATE_TIME
			try{
				statement.executeUpdate("alter table "+cm.getTableName()+" add ( "+createdDate+"  datetime ) ");
			}catch(SQLException e){
				System.out.println(cm.getTableName()+"已经存在'"+createdDate+"'列\n"+e.getMessage());
			}
			//MODIFIER
			try{
				statement.executeUpdate("alter table "+cm.getTableName()+" add ("+modifiedBy+"  VARCHAR(36)) ");
			}catch(SQLException e){
				System.out.println(cm.getTableName()+"已经存在'"+modifiedBy+"'列\n"+e.getMessage());
			}
			//MODIFY_TIME
			try{
				statement.executeUpdate("alter table "+cm.getTableName()+" add ( "+modifiedDate+"  datetime ) ");
			}catch(SQLException e){
				System.out.println(cm.getTableName()+"已经存在'"+modifiedDate+"'列\n"+e.getMessage());
			}

			//version
			try{
				statement.executeUpdate("alter table "+cm.getTableName()+" add ( version  int ) ");
			}catch(SQLException e){
				System.out.println(cm.getTableName()+"已经存在'version '列\n"+e.getMessage());
			}

			//isDeleted
			try{
				statement.executeUpdate("alter table "+cm.getTableName()+" add ( isDeleted  tinyint(1) ) ");
			}catch(SQLException e){
				System.out.println(cm.getTableName()+"已经存在'isDeleted '列\n"+e.getMessage());
			}
			getClassModel(cm);
            getMapperXML(cm);
            getMapperModel(cm);
            getServiceModel(cm);
            getControllerModel(cm);
			getApiModel(cm);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            	System.out.println("代码生成完成");
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

    }

	private static  String toUpperFirstLetterCase(String str){
		return  str.substring(0,1).toUpperCase()+str.substring(1);
	}

	private static void getMapperXML(ClassModel cm){
		StringBuffer sb = new StringBuffer();
		File f = new File(rootPath+"MapperXmlTemplate");
		try {
			FileReader fr  = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while ((s = br.readLine()) != null) {
				 sb.append(s+"\r");
			}
			br.close();
			fr.close();



			String xmlString = sb.toString();
			//替换掉类属性
			Field[] fields = cm.getClass().getDeclaredFields();
			for(Field field : fields ){
				if(field.getType().getName().equals("java.lang.String"))
					xmlString = xmlString.replace("${"+field.getName()+"}",(String)cm.getClass().getMethod("get"+toUpperFirstLetterCase(field.getName()),null).invoke(cm,null));
			}
			
			Pattern pattern = Pattern.compile("@([a-zA-Z]+)\\{(.+)\\}@",Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(xmlString);
			while(matcher.find()){
//			    System.out.println(matcher.group(0));
//			    System.out.println(matcher.group(1));
			    //System.out.println(matcher.group(2));
			    StringBuffer params  = new StringBuffer();
			    List<String> matList = new ArrayList<String>();
				Pattern paramreg = Pattern.compile("@\\{([^\\}]+)\\}");
				Matcher parammat = paramreg.matcher(matcher.group(2));
				while(parammat.find()){
					/*System.out.println(parammat.group(0));
					System.out.println(parammat.group(1));*/
					for(int i=0;i<cm.getList().size();i++){
						Column cl  = cm.getList().get(i);
						if(matList.size()<cm.getList().size()){
							matList.add(matcher.group(2).replace(parammat.group(0), (String)cl.getClass().getMethod("get"+toUpperFirstLetterCase(parammat.group(1)),null).invoke(cl,null)));
						}else{
							matList.set(i, matList.get(i).replace(parammat.group(0), (String)cl.getClass().getMethod("get"+toUpperFirstLetterCase(parammat.group(1)),null).invoke(cl,null)));
						}
					}
				}
				boolean flag = matcher.group(2).contains("<");
				for(String str : matList){
					params.append(str+(flag?"\r":""));
				}
				String paramsresult = params.toString();
				if(!flag&&!"".equals(paramsresult))
					paramsresult = paramsresult.substring(0, paramsresult.length()-1);
			    xmlString   =  xmlString.replace(matcher.group(0),paramsresult);
			    matcher = pattern.matcher(xmlString);
			}
			//System.out.println(xmlString);
			File fgroup  = new File(DictCacheService.OUT_JAVA_PATH+File.separator+packageName.replace(".",File.separator)+File.separator+ "mapper");
			if(!fgroup.exists()){
				fgroup.mkdirs();
			}
			File fout = new File(DictCacheService.OUT_JAVA_PATH+File.separator+packageName.replace(".",File.separator)+File.separator+ "mapper" +File.separator+cm.getClassName()+"Dao.xml");
			if(fout.exists()){
				fout.delete(); 
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(fout, true));
		 	writer.write(xmlString);
		 	writer.close();
		 	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void getMapperModel(ClassModel cm){
		StringBuffer sb = new StringBuffer();
		File f = new File(rootPath+"MapperClassTemplate");
		try {
			FileReader fr  = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while ((s = br.readLine()) != null) {
				 sb.append(s+"\r");
			}
			br.close();
			fr.close();
			String classString = sb.toString();
			Field[] fields = cm.getClass().getDeclaredFields();
			for(Field field : fields ){
				if(field.getType().getName().equals("java.lang.String"))
					classString = classString.replace("${"+field.getName()+"}",(String)cm.getClass().getMethod("get"+toUpperFirstLetterCase(field.getName()),null).invoke(cm,null));
			}
			
			File fgroup  = new File(DictCacheService.OUT_JAVA_PATH+File.separator+packageName.replace(".",File.separator)+File.separator+"dao");
			if(!fgroup.exists()){
				fgroup.mkdirs();
			}
			File fout = new File(DictCacheService.OUT_JAVA_PATH+File.separator+packageName.replace(".",File.separator)+File.separator+"dao"+File.separator+cm.getClassName()+"Dao.java");
			if(fout.exists()){
				fout.delete(); 
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(fout, true));
		 	writer.write(classString);
		 	writer.close();
		 	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void getServiceModel(ClassModel cm){
		StringBuffer sb = new StringBuffer();

		File f = new File(rootPath+"ServiceTemplate");
		try {
			FileReader fr  = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while ((s = br.readLine()) != null) {
				 sb.append(s+"\r");
			}
			br.close();
			fr.close();
			String classString = sb.toString();
			Field[] fields = cm.getClass().getDeclaredFields();
			for(Field field : fields ){
				if(field.getType().getName().equals("java.lang.String"))
					classString = classString.replace("${"+field.getName()+"}",(String)cm.getClass().getMethod("get"+toUpperFirstLetterCase(field.getName()),null).invoke(cm,null));
			}
			
			File fgroup  = new File(DictCacheService.OUT_JAVA_PATH+File.separator+packageName.replace(".",File.separator)+File.separator+"service");
			if(!fgroup.exists()){
				fgroup.mkdirs();
			}
			File fout = new File(DictCacheService.OUT_JAVA_PATH+File.separator+packageName.replace(".",File.separator)+File.separator+"service"+File.separator+cm.getClassName()+"Service.java");
			if(fout.exists()){
				fout.delete(); 
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(fout, true));
		 	writer.write(classString);
		 	writer.close();
		 	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void getControllerModel(ClassModel cm){
		StringBuffer sb = new StringBuffer();
		File f = new File(rootPath+"ControllerTemplate");
		try {
			FileReader fr  = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while ((s = br.readLine()) != null) {
				 sb.append(s+"\r");
			}
			br.close();
			fr.close();
			String classString = sb.toString();
			Field[] fields = cm.getClass().getDeclaredFields();
			for(Field field : fields ){
				if(field.getType().getName().equals("java.lang.String"))
					classString = classString.replace("${"+field.getName()+"}",(String)cm.getClass().getMethod("get"+toUpperFirstLetterCase(field.getName()),null).invoke(cm,null));
			}
			
			File fgroup  = new File(DictCacheService.OUT_JAVA_PATH+File.separator+packageName.replace(".",File.separator)+File.separator+"controller");
			if(!fgroup.exists()){
				fgroup.mkdirs();
			}
			File fout = new File(DictCacheService.OUT_JAVA_PATH+File.separator+packageName.replace(".",File.separator)+File.separator+"controller"+File.separator+cm.getClassName()+"Controller.java");
			if(fout.exists()){
				fout.delete(); 
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(fout, true));
		 	writer.write(classString);
		 	writer.close();
		 	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void getApiModel(ClassModel cm){
		StringBuffer sb = new StringBuffer();
		File f = new File(rootPath+"ApiTemplate");
		try {
			FileReader fr  = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while ((s = br.readLine()) != null) {
				sb.append(s+"\r");
			}
			br.close();
			fr.close();
			String classString = sb.toString();
			Field[] fields = cm.getClass().getDeclaredFields();
			for(Field field : fields ){
				if(field.getType().getName().equals("java.lang.String"))
					classString = classString.replace("${"+field.getName()+"}",(String)cm.getClass().getMethod("get"+toUpperFirstLetterCase(field.getName()),null).invoke(cm,null));
			}

			File fgroup  = new File(DictCacheService.OUT_JAVA_PATH+File.separator+packageName.replace(".",File.separator)+File.separator+"api");
			if(!fgroup.exists()){
				fgroup.mkdirs();
			}
			File fout = new File(DictCacheService.OUT_JAVA_PATH+File.separator+packageName.replace(".",File.separator)+File.separator+"api"+File.separator+"Api"+cm.getClassName()+"Controller.java");
			if(fout.exists()){
				fout.delete();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(fout, true));
			writer.write(classString);
			writer.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void getClassModel(ClassModel cm){
		StringBuffer sb = new StringBuffer();
		File f = new File(rootPath+"ClassTemplate");
		try {
			FileReader fr  = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while ((s = br.readLine()) != null) {
				 sb.append(s+"\r");
			}
			br.close();
			fr.close();
			StringBuffer function= new StringBuffer();
			for(Column cl : cm.getList()){
				if(!filterProperty.contains(";"+cl.getName()+";")&&!pkId.toUpperCase().equals(cl.getColumnName().toUpperCase())){
					function.append( (cl.getRemarks()!=null?("    //"+cl.getRemarks()+"\r"):"") +space+"private "+ cl.getType() +" "+cl.getName()+(cl.getDefaultValue()!=null?(" = "+cl.getDefaultValueToString()):"")+";\r");//+(cl.getRemarks()!=null?("  //"+cl.getRemarks()):"")+"\r"
					/*+ space+"public void set"+cl.getFunName()+"("+cl.getType()+" "+cl.getName()+"){\r"
					+ space+space+"this."+cl.getName()+" = "+cl.getName()+";\r"
					+ space+"}\r"
					+ space+"public "+cl.getType()+" get"+cl.getFunName()+"(){\r"
					+ space+space+"return this."+cl.getName()+";\r"
					+ space+"}\n\r");*/
				}
				
			}
			String classString = sb.toString();
			classString  =  classString.replace("${packageName}", cm.getPackageName());
			classString  =  classString.replace("${tableName}", cm.getTableName());
			classString  =  classString.replace("${className}", cm.getClassName());
			classString  =  classString.replace("${function}", function.toString());
			//System.out.println(classString);
			
			File fgroup  = new File(DictCacheService.OUT_JAVA_PATH+File.separator+packageName.replace(".",File.separator)+File.separator+"models");
			if(!fgroup.exists()){
				fgroup.mkdirs();
			}
			File fout = new File(DictCacheService.OUT_JAVA_PATH+File.separator+packageName.replace(".",File.separator)+File.separator+"models"+File.separator+cm.getClassName()+".java");
			if(fout.exists()){
				fout.delete(); 
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(fout, true));
		 	writer.write(classString);
		 	writer.close();
		 	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

