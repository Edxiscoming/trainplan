package org.railway.com.trainplan.web.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.common.constants.StaticCodeType;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.entity.BusinessInfo;
import org.railway.com.trainplan.entity.User;
import org.railway.com.trainplan.repository.mybatis.RoleDao;
import org.railway.com.trainplan.repository.mybatis.UserDao;
import org.railway.com.trainplan.service.LoginService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.railway.com.trainplan.web.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by star on 5/15/14.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private UserDao userDao;

    private RoleDao roleDao;

//    public UserDao getUserDao() {
//        return userDao;
//    }
//
//    public void setUserDao(UserDao userDao) {
//        this.userDao = userDao;
//    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }
    @RequestMapping(value = "{username}/account", method = RequestMethod.GET)
    public List<Map<String, Object>> getAccounts(@PathVariable String username) {
        logger.debug("-X GET /user/" + username + "/account");
        return loginService.getAccountByLoginName(username);
    }
    
    
    
    @ResponseBody
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public Result getFullStationTrains(HttpServletRequest request, HttpServletResponse response){
		Result result = new Result();
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
		
		result.setData(user);
		return result;
	}
    
    
/*    @ResponseBody
   	@RequestMapping(value = "/passwordfix", method = RequestMethod.POST)
   	public Result passwordFix(HttpServletRequest request, HttpServletResponse response){
   		Result result = new Result();
   		
   				var username1= $("#username1").val()  
   				var oldpass =$("#oldpass").val()    
   				var newpass = $("#newpass").val()      
   				var newpassagain =$("#newpassagain").val()
   		String username1 = StringUtil.objToStr(request.getParameter("username1"));
		String oldpass =  StringUtil.objToStr(request.getParameter("oldpass"));
		String newpass =  StringUtil.objToStr(request.getParameter("newpass"));
		//String crossName =  StringUtil.objToStr(request.getParameter("crossName"));
   		System.out.println(username1+oldpass+newpass);
   	
   		return result;
   	}*/
    
    
    
	@ResponseBody
	@RequestMapping(value = "/passwordfix", method = RequestMethod.POST)
	public Result queryBusinesses(@RequestBody Map<String,Object> reqMap){
		Result result = new Result();
		try{
			logger.info("queryBusinesses~~reqMap="+reqMap);
			//调用后台接口
	    System.out.println();
			//{username1=bjkd, oldpass=qq, newpass=qq}
			List<Map<String, Object>> list =loginService.getAccountByLoginName(reqMap.get("username1").toString());
			
			  Map<String, Object> params = new HashMap<String, Object>();
		        params.put("username", reqMap.get("username1"));
		        params.put("accId", Integer.parseInt(list.get(0).get("ACC_ID").toString()));
		        
		        
		        Map<String, Object> params1 = new HashMap<String, Object>();
		        params1.put("password", reqMap.get("newpass"));
		        params1.put("accId", Integer.parseInt(list.get(0).get("ACC_ID").toString()));
		      
			 User user = userDao.getUserByUsernameAndAccId(params);
			 System.out.println(user.getPassword());
			 if(!reqMap.get("oldpass").equals(user.getPassword())){
				 result.setCode("1");//表示原密码不同
				 return result;
			 }else{
				 
			int i =	 userDao.uptdateUser(params1);
				 if(i!=0){
					 result.setCode("2");//成功
					 return result;
				 }
				 
			 }
			 
			 
			 
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			result.setCode(StaticCodeType.SYSTEM_ERROR.getCode());
			result.setMessage(StaticCodeType.SYSTEM_ERROR.getDescription());		
		}
		return result;
	} 
	
    @ResponseBody
	@RequestMapping(value = "/loadUserMsg", method = RequestMethod.GET)
	public Result loadUserMsg(HttpServletRequest request, HttpServletResponse response){
		Result result = new Result();
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
		String b = "";
		if(user!=null){
			b = user.getUsername()+"("+user.getPostName()+ ") | " + user.getBureauShortName()+"局"; 
		}
		result.setData(b);
		return result;
	}
}
