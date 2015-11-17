package org.railway.com.trainplan.repository.mybatis;

import org.railway.com.trainplan.entity.Permission;
import org.railway.com.trainplan.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * Created by star on 5/15/14.
 */
 
public interface RoleDao {

    List<Role> getRoleByAccId(int accId);
    
    List<Permission> getPermissionByRoleIds(Map<String, String> queryMap);
}
