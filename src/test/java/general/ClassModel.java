package general;

import java.util.ArrayList;
import java.util.List;

public class ClassModel {
	private String className;
	private String name;
	private String packageName;
	private String tableName;
	private String abbrTableName;
	private String pkId;
	private String createBy;
	private String createTime;
	private String updateBy;
	private String updateTime;
	private List<Column> list = new ArrayList<Column>();


	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getPkId() {
		return pkId;
	}
	public void setPkId(String pkId) {
		this.pkId = pkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.className = toUpperFirstLetterCase(name);
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public List<Column> getList() {
		return list;
	}
	public void setList(List<Column> list) {
		this.list = list;
	}
	public void addColumn(Column column){
		this.list.add(column);
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.abbrTableName = toAbbrTableName(tableName);
		this.tableName = tableName;
	}
	private String toUpperFirstLetterCase(String str){
		return  str.substring(0,1).toUpperCase()+str.substring(1);
	}
	public String getAbbrTableName() {
		return abbrTableName;
	}
	public void setAbbrTableName(String abbrTableName) {
		this.abbrTableName = abbrTableName;
	}
	private String toAbbrTableName(String tableName){
		String abbr = "";
		for(String s  : tableName.split("_")){
			abbr += String.valueOf((s.charAt(0)));
		}
		if(abbr.length()==0)abbr = "t";
		return abbr;
	}
	
}
