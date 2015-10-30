package org.railway.com.trainplan.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.railway.com.trainplan.common.utils.LjUtil;
import org.railway.com.trainplan.entity.Permission;
import org.railway.com.trainplan.entity.Role;
import org.railway.com.trainplan.entity.User;
import org.railway.com.trainplan.repository.mybatis.RoleDao;
import org.railway.com.trainplan.repository.mybatis.UserDao;

/**
 * Created by star on 5/15/14.
 */
public class ShiroRealm extends AuthorizingRealm {

	private final static Log logger = LogFactory.getLog(ShiroRealm.class);

	private UserDao userDao;

	private RoleDao roleDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		logger.info("Authorization::::" + shiroUser.getUsername());
		try {
			if (shiroUser != null) {
				List<Role> roleList = shiroUser.getRoleList();
				List<String> permissionList = shiroUser.getPermissionList();
				if (roleList != null && !roleList.isEmpty()) {
					SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
					StringBuffer roleIdSbf = new StringBuffer(); // 保存角色Id
					for (Role role : roleList) {
						info.addRole(role.getName());
						roleIdSbf.append(role.getId()).append(",");
						logger.debug("添加角色::" + role.getName() + " TO 用户:::"
								+ shiroUser.getName());
					}

					if (permissionList != null && !permissionList.isEmpty()) {
						for (String permissionStr : permissionList) {
							info.addStringPermission(permissionStr);
							logger.debug("添加权限::" + permissionStr + " TO 用户:::"
									+ shiroUser.getName());
						}
					}

					return info;
				}
			}
		} catch (Exception e) {
			logger.error("查询角色出错::::::");
			logger.error(e);
		}
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		String username = usernamePasswordToken.getUsername();
		logger.info("Authentication:::::" + username);
		String[] logininfo = username.split("@");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", logininfo[0]);
		if(!StringUtils.equals(logininfo[0], "ktz")){
			params.put("ljjc", LjUtil.getLjByNameJ(logininfo[2], 1));
		}
		params.put("accId", Integer.parseInt(logininfo[1]));
		try {

			User user = userDao.getUserByUsernameAndAccId(params);
			if (user != null) {
				if (0 == user.getId() || "".equals(user.getUsername())
						|| "".equals(user.getName())) {
					throw new DisabledAccountException();
				}

				ShiroUser shiroUser = new ShiroUser(user.getUsername(),
						user.getName(), Integer.parseInt(logininfo[1]),
						user.getLjpym(), user.getLjjc(), user.getLjqc(),
						user.getDeptname(), user.getPostId(),
						user.getPostName());

				// 添加角色列表
				List<Role> roleList = roleDao.getRoleByAccId(shiroUser
						.getAccId());
				shiroUser.setRoleList(roleList);
				String roleIds = "";
				StringBuffer roleIdSbf = new StringBuffer(""); // 保存角色Id
				for (Role role : roleList) {
					roleIdSbf.append(role.getId()).append(",");
				}
				if (roleIdSbf.indexOf(",") > -1) {
					roleIds = roleIdSbf
							.substring(0, roleIdSbf.lastIndexOf(","));
				}

				// 添加权限列表
				List<String> permissionKeyList = new ArrayList<String>();
				Map<String, String> queryMap = new HashMap<String, String>();
				queryMap.put("roleIds", roleIds);
				List<Permission> permissionList = roleDao
						.getPermissionByRoleIds(queryMap);
				if (permissionList != null && !permissionList.isEmpty()) {
					for (Permission permission : permissionList) {
						permissionKeyList.add(permission.getKey());
					}
				}
				shiroUser.setPermissionList(permissionKeyList);
				logger.info("权限列表::" + shiroUser.getPermissionList());

				return new SimpleAuthenticationInfo(shiroUser,
						user.getPassword(), getName());
			} else {

				throw new Exception("not found");
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public static class ShiroUser implements Serializable {

		private String username;

		/**
		 * 姓名.
		 */
		private String name;

		private int accId;

		private String deptName;

		private String bureau;

		private String bureauShortName;

		private String bureauFullName;

		private List<Role> roleList;

		private List<String> permissionList;

		private String postId;// 用户登录时选择的岗位id

		/**
		 * 所属单位.
		 */
		private String postName;// 用户登录时选择的岗位名称

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public int getAccId() {
			return accId;
		}

		public void setAccId(int accId) {
			this.accId = accId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getBureau() {
			return bureau;
		}

		public void setBureau(String bureau) {
			this.bureau = bureau;
		}

		public String getBureauShortName() {
			return bureauShortName;
		}

		public void setBureauShortName(String bureauShortName) {
			this.bureauShortName = bureauShortName;
		}

		public String getBureauFullName() {
			return bureauFullName;
		}

		public void setBureauFullName(String bureauFullName) {
			this.bureauFullName = bureauFullName;
		}

		public String getDeptName() {
			return deptName;
		}

		public void setDeptName(String deptName) {
			this.deptName = deptName;
		}

		public List<Role> getRoleList() {
			return roleList;
		}

		public void setRoleList(List<Role> roleList) {
			this.roleList = roleList;
		}

		public List<String> getPermissionList() {
			return permissionList;
		}

		public void setPermissionList(List<String> permissionList) {
			this.permissionList = permissionList;
		}

		public ShiroUser(String username, String name, int accId) {
			this.username = username;
			this.name = name;
			this.accId = accId;
		}

		public String getPostId() {
			return postId;
		}

		public void setPostId(String postId) {
			this.postId = postId;
		}

		public String getPostName() {
			return postName;
		}

		public void setPostName(String postName) {
			this.postName = postName;
		}

		public ShiroUser(String username, String name, int accId,
				String bureau, String bureauShortName, String bureauFullName,
				String deptName, String postId, String postName) {
			this.username = username;
			this.name = name;
			this.accId = accId;
			this.bureau = bureau;
			this.bureauShortName = bureauShortName;
			this.bureauFullName = bureauFullName;
			this.deptName = deptName;
			this.postId = postId;
			this.postName = postName;
		}

		@Override
		public String toString() {
			return this.name;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(this.username) + accId;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ShiroUser other = (ShiroUser) obj;
			if (this.username == null) {
				if (other.getUsername() != null) {
					return false;
				}
			} else if (!this.username.equals(other.getUsername())) {
				return false;
			}
			if (this.accId == 0) {
				if (other.getUsername() != null) {
					return false;
				}
			} else if (this.accId != other.getAccId()) {
				return false;
			}
			return true;
		}
	}
}
