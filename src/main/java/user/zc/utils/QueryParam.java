package user.zc.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础参数
 * @author:Administrator
 * @create 2018-01-21 12:41
 */
@Data
public class QueryParam {
    //条件
    private Map<String, Object> condition;
//    private Map<String,Object>  in;

    //排序字段
    protected String orderByClause;
    //分组字段
    protected String groupByClause;
    @JsonIgnore
    public String getGroupByClause(){
        return this.groupByClause;
    }

    @JsonIgnore
    public String getOrderByClause(){
        return this.orderByClause;
    }

    public QueryParam(String key,Object value){
        if(value instanceof List){
            this.in(key,(List)value);
        }else {
            this.put(key,value);
        }

    }

    public void put(String key,Object value){
        if(condition==null){
            condition = new HashMap<String, Object>();
        }
        condition.put(key,value);
    }
    protected String column ;

    @JsonIgnore
    public String getColumn(){
        return this.column;
    }
    protected List<String> ids;
    protected String idsColumn ;


    public QueryParam(){
        this.put("isDeleted",false);
    }

    public void in(String key ,List<String> list){
        if(condition==null){
            condition = new HashMap<String, Object>();
        }
        column = key;
        ids = list;
    }




    public void clear(){
        this.setCondition(null);
        this.setIdsColumn(null);
        this.setColumn(null);
        this.setIds(null);
        this.orderByClause = null;
        this.groupByClause = null;
    }

}
