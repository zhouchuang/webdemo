package user.zc.dao.base;

import user.zc.models.base.Permission;
import user.zc.utils.PageParam;
import user.zc.utils.QueryParam;

import java.util.HashMap;
import java.util.List;

/**
 * 权限dao层
 *
 * @author:Administrator
 * @create 2018-01-19 18:12
 */
public interface PermissionDao extends Dao{
    List<Permission> findMenuList();
    List<Permission> findPermissions(List<String> roles);
    List<Permission> findPermissionByDepth(int depth);
    Permission getMaxSortChild(QueryParam queryParam);
    List<HashMap> queryHashMap(PageParam pageParam);
}
