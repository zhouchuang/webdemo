package user.zc.models.base;import lombok.Data;import user.zc.utils.BaseEntity;//表名称 userrole@Datapublic class Userrole extends BaseEntity {	private static final long serialVersionUID = 1L;	public Userrole() {		super();	}	    private String userId;    private String roleId;}