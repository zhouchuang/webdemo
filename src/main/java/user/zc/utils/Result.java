package user.zc.utils;


import lombok.Data;

import java.util.List;

@Data
public class Result implements java.io.Serializable {


	private static final long serialVersionUID = 1L;

	private boolean success = true;

	private String msg = "操作成功";

	private Object data = null;
	
	private String code="200";  //200成功  500失败  403没有权限 404找不到

	public static String SUCCESS = "200";
	public static String ERROR = "500";
	public static String NOAUTHC = "403";
	public static String NOFOUND = "404";
	public static String PARAMNOMATCH = "400";
	private int nextPage;
	private int totalPage;


	public Result(){

	}

	public Result(String code,String msg){
		put(code,msg);
	}

	public void put(String code, String msg){
		this.code = code;
		this.msg = msg;
	}
	public void byUpdate(long num){
		this.code = SUCCESS;
		this.msg = "修改成功，"+num+"条记录发生了改变";
	}
	public void byDelete(long num){
		this.code = SUCCESS;
		this.msg = "删除成功，"+num+"条记被删除";
	}
	public void byInsert(long num){
		this.code = SUCCESS;
		this.msg = "新增成功，"+num+"条记被插入";
	}
	public void error(String msg){
		this.code = ERROR;
		this.success = false;
		this.msg =  msg;
	}

	public void  setData(Object object){
		this.data = object;
		if(object!=null  && object instanceof PageParam){
			((PageParam)object).clear();
		}
	}


	public List getDataList(){
		if( this.data instanceof  PageParam ){
			Object object =  this.getData();
			if(object!=null){
				return ((PageParam)object).getList();
			}
		}
		return null;
	}

}
