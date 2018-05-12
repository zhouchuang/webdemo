package user.zc.dao.base;


import user.zc.utils.BaseEntity;
import user.zc.utils.PageParam;
import user.zc.utils.QueryParam;

import java.util.List;


/**
 * 通过接口编程
 *
 */
public interface Dao {
    /**
     * 添加某个对象
     * @param t 待添加的对象
     * @return  返回受影响的行数
     */
    <ENTITY extends BaseEntity> Long  add(ENTITY t);

    /**
     * 删除某个对象，在企业开发中，我们一般不做物理删除，只是添加某个字段对其数据进行可用控制
     * @param t 待删除对象
     * @return 返回受影响的条数
     */
    <ENTITY extends BaseEntity> Long  del(ENTITY t);
    /**
     * 更新某个对象
     * @param t 待更新对象
     * @return 返回受影响的条数
     */
    <ENTITY extends BaseEntity> Long update(ENTITY t);
    /**
     * 通过ID查找一个对象
     * @param id    待查询的对象ID
     * @return  返回该ID对应的对象
     */
    <ENTITY extends BaseEntity> ENTITY get(String id);
    /**
     * 查找对象集合
     * @return  返回对象集合
     */
    <ENTITY extends BaseEntity> List<ENTITY> findPage(PageParam pageParam);
    /**
     * 查询符合条件集合
     * @return  集合
     */
    <ENTITY extends BaseEntity> List<ENTITY> findList(QueryParam queryParam);
    /**
     * 查询符合条件的数量
     * @return  数量
     */
    Long count(QueryParam queryParam);
    /**
     * 查询符合条件的一个页面
     * @return  对象页面
     */
    <ENTITY extends BaseEntity> List<ENTITY> findLikePage(PageParam pageParam);
    /**
     * 查询符合条件的一个集合
     * @return  对象集合
     */
    <ENTITY extends BaseEntity> List<ENTITY> findLikeList(QueryParam queryParam);
    /**
     * 查询符合条件的一个对象
     * @return  返回id集合
     */
    <ENTITY extends BaseEntity> ENTITY one(QueryParam queryParam);
    /**
     * 查找符合条件的id集合
     * @return  返回id集合
     */
    List<String> findIds(QueryParam queryParam);
    /**
     * 查找符合条件的id集合，模糊查询
     * @return  返回id集合
     */
    List<String> findLikeIds(QueryParam queryParam);
}
