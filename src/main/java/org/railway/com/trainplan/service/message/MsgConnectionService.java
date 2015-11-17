package org.railway.com.trainplan.service.message;


import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.directwebremoting.ScriptSession;
import org.railway.com.trainplan.common.utils.DwrScriptSessionManagerUtil;
import org.railway.com.trainplan.service.ShiroRealm;
import org.springframework.stereotype.Service;

import uk.ltd.getahead.dwr.WebContextFactory;


@Service
public class MsgConnectionService {
	private static final Logger logger = Logger.getLogger(MsgConnectionService.class);
	
	
	/**
	 * 注册消息连接通道
	 * @param username
	 */
    public void register() {
//    	ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	
//        ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
//   
//        scriptSession.setAttribute("userInfo", user);
        DwrScriptSessionManagerUtil dwrScriptSessionManagerUtil = new DwrScriptSessionManagerUtil();
   
        try {  
        	dwrScriptSessionManagerUtil.init();  
        } catch (ServletException e) {
        	logger.error("消息连接发生异常", e);
        }
    }
}
