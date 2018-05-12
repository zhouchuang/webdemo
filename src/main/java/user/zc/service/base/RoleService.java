package user.zc.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.zc.dao.base.RoleDao;
import user.zc.models.base.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 角色服务层
 *
 * @author:Administrator
 * @create 2018-01-19 18:15
 */
@Service("roleService")
public class RoleService extends AbstractService {
    @Autowired
    private RoleDao roleDao;
    private PermissionService permissionService;
    public RoleDao getDao() {
        return this.roleDao;
    }
    public List<Role> findRolesByAccountNo(String accountNo) {
        return getDao().findRolesByAccountNo(accountNo);
    }

    public Set<Role> findRoleSetsByAccountNo(String accountNo){
        List<Role> roles = this.findRolesByAccountNo(accountNo);
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.addAll(roles);
        return  roleSet;
    }

    public List<String> findRoleIdByAccountNo(String accountNo){
        List<String> roleIds = new ArrayList<String>();
        for(Role role : findRoleSetsByAccountNo(accountNo)){
            if(role!=null)roleIds.add(role.getId());
        }
        return roleIds;
    }
}
