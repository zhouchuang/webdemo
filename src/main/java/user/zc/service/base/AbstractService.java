package user.zc.service.base;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import user.zc.aop.StorageChild;
import user.zc.dao.base.Dao;
import user.zc.utils.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象service
 *
 * @author:Administrator
 * @create 2018-01-18 14:04
 */
public abstract class AbstractService implements BaseService{


    /*@Transactional( value = "transactionManager_1", readOnly = false, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)*/


    public abstract Dao getDao();
    /**
     * 物理删除
     * @param entity
     * @return List<ENTITY>
     * @exception Exception
     */
    public <ENTITY extends BaseEntity> Long physicalDelete(ENTITY entity) throws Exception {
        if(entity!=null){
            this.getDao().del(entity);
        }
        return  -1L;
    }
    /**
     * 逻辑删除
     * @param entity
     * @return List<ENTITY>
     * @exception Exception
     */
    public <ENTITY extends BaseEntity> Long delete(ENTITY entity) throws Exception {
        if(entity!=null){
            entity.setDeleted(true);
            return update(entity);
        }
        return  -1L;
    }
    /**
     * 逻辑删除list
     * @param list
     * @return List<ENTITY>
     * @exception Exception
     */
    public <ENTITY extends BaseEntity> Long deleteList(List<ENTITY> list) throws Exception {
        Long num = 0L;
        for(ENTITY entity : list){
            num += delete(entity);
        }
        return num;
    }
    /**
     * 新增操作
     * @param entity
     * @return List<ENTITY>
     * @exception Exception
     */
    public <ENTITY extends BaseEntity> Long add(ENTITY entity) throws Exception {
        if(entity!=null){
            if(StringUtils.isEmpty(entity.getId())){
                entity.setId(UUIDGenerator.randomUUID());
            }
            entity.addPre();
            return this.getDao().add(entity);
        }
        return  -1L;
    }
    /**
     * 更新操作
     * @param entity
     * @return List<ENTITY>
     * @exception Exception
     */
    public <ENTITY extends BaseEntity> Long update(ENTITY entity) throws Exception{
        if(entity!=null){
            entity.setVersion(entity.getVersion()+1);
            entity.updatePre();
            return this.getDao().update(entity);
        }
        return -1L;
    }
    public <ENTITY extends BaseEntity> Long persistence(ENTITY entity)throws Exception{
        if(entity!=null){
            if(StringUtils.isNotEmpty(entity.getId())){
                return update(entity);
            }else{
                return add(entity);
            }
        }
        return -1L;
    }
    /**
     * 更新操作 此方法可以级联保存对象里面的子对象
     * @param entity
     * @return List<ENTITY>
     * @exception Exception
     */
    @Transactional
    public <ENTITY extends BaseEntity> Long persistenceAndChild(ENTITY entity)throws Exception{
        if(entity!=null){
            //有的需要先初始化子对象然后拿到子对象的id赋值给主对象
            Boolean needAgain = false;
            for(Field field : entity.getClass().getDeclaredFields()){
                if(field.getAnnotation(StorageChild.class)!=null){
                    field.setAccessible(true);
                    StorageChild storageChild = field.getAnnotation(StorageChild.class);
                    String service = storageChild.service();
                    Object ent = field.get(entity);
                    if(StringUtils.isNotEmpty(service)&&ent!=null ){
                        ApplicationContext beanFactory= SpringContextUtil.getApplicationContext();
                        Object serviceimpl = beanFactory.getBean(service);
                        String refColumn = storageChild.refColumn();
                        //如果没有传入refColumn值 ，则判断是不是反向关联列revColumn
                        if(StringUtils.isNotEmpty(refColumn)){
                            ((AbstractService)serviceimpl).persistence((ENTITY)ent);
                            Field refField = entity.getClass().getDeclaredField(refColumn);
                            refField.setAccessible(true);
                            refField.set(entity,((ENTITY) ent).getId());
                        }else{
                            String revColumn = storageChild.revColumn();
                            if(StringUtils.isNotEmpty(revColumn))
                                needAgain = true;
                        }
                    }
                }
            }
            persistence(entity);
            //有的需要初始化主对象，然后拿到主对象的id赋值给子对象
            if(needAgain){
                for(Field field : entity.getClass().getDeclaredFields()){
                    if(field.getAnnotation(StorageChild.class)!=null){
                        field.setAccessible(true);
                        StorageChild storageChild = field.getAnnotation(StorageChild.class);
                        String service = storageChild.service();
                        Object ent = field.get(entity);
                        if(StringUtils.isNotEmpty(service)&&ent!=null ){
                            ApplicationContext beanFactory= SpringContextUtil.getApplicationContext();
                            Object serviceimpl = beanFactory.getBean(service);
                            String revColumn = storageChild.revColumn();
                            if(StringUtils.isNotEmpty(revColumn)){
                                if(ent instanceof List){
                                    //保存前先吧主表的ID设置到子表的关联列里面去
                                    for(ENTITY child : (List<ENTITY>)ent){
                                        Field revField = child.getClass().getDeclaredField(revColumn);
                                        revField.setAccessible(true);
                                        revField.set(child,entity.getId());
                                    }
                                    ((AbstractService)serviceimpl).persistenceList((List<ENTITY>)ent);
                                }else{
                                    Field revField = ent.getClass().getDeclaredField(revColumn);
                                    revField.setAccessible(true);
                                    revField.set(ent,entity.getId());
                                    ((AbstractService)serviceimpl).persistence((ENTITY)ent);
                                }
                            }
                        }
                    }
                }
            }
        }
        return -1L;
    }
    /**
     * 保存一个list
     * @param entitys
     * @return Long
     * @exception Exception
     */
    public <ENTITY extends BaseEntity> Long persistenceList(List<ENTITY> entitys)throws Exception{
        Long num = 0L;
        for(ENTITY entity : entitys){
            num += persistence(entity);
        }
        return num;
    }
    /**
     * 删除旧的list
     * @param newList,oldList
     * @return Long
     * @exception Exception
     */
    public <ENTITY extends BaseEntity> Long removeList(List<ENTITY> newList,List<ENTITY> oldList)throws Exception{
        Long num = 0L;
        for(ENTITY old : oldList){
            boolean flag = true;
            for( ENTITY news : newList){
                if(old.getId().equals(news.getId())){
                    flag = false;
                    break;
                }
            }
            if(flag){
                num += physicalDelete(old);
            }
        }
        return num;
    }

    /**
     * 获取一个通过id
     * @param id
     * @return List<ENTITY>
     * @exception Exception
     */
    public <ENTITY extends BaseEntity> ENTITY get(String id)throws Exception{
        if(StringUtils.isNotEmpty(id)){
            return this.getDao().get(id);
        }
        return null;
    }
    /**
     * 获取一个通过非id类型
     * @param queryParam
     * @return List<ENTITY>
     * @exception Exception
     */
    public <ENTITY extends BaseEntity> ENTITY one(QueryParam queryParam)throws Exception{
        if(queryParam!=null){
            return this.getDao().one(queryParam);
        }
        return null;
    }
    /**
     * 获取分页
     * @param pageParam
     * @return List<ENTITY>
     * @exception Exception
     */
    public PageParam findPage(PageParam pageParam)throws Exception{
        pageParam.setList(this.getDao().findPage(pageParam));
        return  pageParam;
    }
    /**
     * 获取数量
     * @param queryParam
     * @return List<ENTITY>
     * @exception Exception
     */
    public  Long count(QueryParam queryParam)throws Exception{
        return  this.getDao().count(queryParam);
    }
    /**
     * 获取模糊查询分页
     * @param pageParam
     * @return List<ENTITY>
     * @exception Exception
     */
    public PageParam findLikePage(PageParam pageParam)throws Exception{
        pageParam.setList(this.getDao().findLikePage(pageParam));
        return  pageParam;
    }
    /**
     * 获取list集合，不分页
     * @param queryParam
     * @return List<ENTITY>
     * @exception Exception
     */
    public <ENTITY extends BaseEntity> List<ENTITY> findList(QueryParam queryParam)throws Exception{
        return this.getDao().findList(queryParam);
    }
    /**
     * 获取list集合，模糊查询，不分页
     * @param queryParam
     * @return List<ENTITY>
     * @exception Exception
     */
    public <ENTITY extends BaseEntity> List<ENTITY> findLikeList(QueryParam queryParam)throws Exception{
        return this.getDao().findLikeList(queryParam);
    }
    /**
     * 获取id集合
     * @param queryParam
     * @return List<String>
     * @exception Exception
     */
    public  List<String> findIds(QueryParam queryParam,String idsName)throws Exception{
        queryParam.setIdsColumn(idsName);
        return  this.getDao().findIds(queryParam);
    }
    /**
     * 获取id集合,模糊搜索
     * @param queryParam
     * @return List<String>
     * @exception Exception
     */
    public  List<String> findLikeIds(QueryParam queryParam,String idsName)throws Exception{
        queryParam.setIdsColumn(idsName);
        return  this.getDao().findLikeIds(queryParam);
    }

    public <ENTITY extends BaseEntity>  List<String> findIds(List<ENTITY> entities)throws Exception{
        List<String> ids = new ArrayList<String>();
        for(ENTITY entity : entities){
            ids.add(entity.getId());
        }
        return ids;
    }

}

