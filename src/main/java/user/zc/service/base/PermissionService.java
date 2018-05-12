package user.zc.service.base;


import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.zc.dao.base.PermissionDao;
import user.zc.models.base.Permission;
import user.zc.utils.QueryParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限服务层
 *
 * @author:Administrator
 * @create 2018-01-19 18:15
 */
@Service("permissionService")
public class PermissionService extends AbstractService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    public PermissionDao getDao() {
        return this.permissionDao;
    }

    public List<Permission> findUserPermissionList(List<String> roles)throws Exception{
        if(CollectionUtils.isEmpty(roles)){
            return new ArrayList<Permission>();
        }else{
            List<Permission> permissionList = this.findPermissions(roles);
            Permission userPermission = new Permission();
            for(Permission permission : permissionList){
                userPermission.getLastMenuByDepth(permission.getDepth()).addMenu(permission);
            }
            return userPermission.getMenuList();
        }

    }

    public List<Permission> findPermissions(List<String> roles)throws Exception{
        return getDao().findPermissions(roles);
    }

    public Set<Permission> findPermissionsToSet(List<String> roels)throws Exception{
        List<Permission> permissions = this.findPermissions(roels);
        Set<Permission> permissionsSet = new HashSet<Permission>();
        permissionsSet.addAll(permissions);
        return  permissionsSet;
    }

    public List<Permission> findPermissionByDepth(int depth)throws Exception{
        return getDao().findPermissionByDepth(depth);
    }

    public Permission getMaxSortChild(QueryParam queryParam)throws Exception{
        return getDao().getMaxSortChild(queryParam);
    }
}
