package org.railway.com.trainplan.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;  
  




import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.session.HttpServletSession;
import org.directwebremoting.Browser;
import org.directwebremoting.Container;  
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContextFactory;  
import org.directwebremoting.WebContext;
import org.directwebremoting.event.ScriptSessionEvent;  
import org.directwebremoting.event.ScriptSessionListener;  
import org.directwebremoting.extend.ScriptSessionManager;  
import org.directwebremoting.servlet.DwrServlet;
import org.railway.com.trainplan.common.constants.ScriptSessionData;
import org.railway.com.trainplan.common.constants.ScriptSessionMap;
import org.railway.com.trainplan.service.ShiroRealm;

import uk.ltd.getahead.dwr.WebContextFactory;

public class DwrScriptSessionManagerUtil extends DwrServlet {
	private static final Logger logger = Logger.getLogger(DwrScriptSessionManagerUtil.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {

		try {
		Container container = ServerContextFactory.get().getContainer();
		ScriptSessionManager manager = container.getBean(ScriptSessionManager.class);
//		@SuppressWarnings("deprecation")
//		WebContext webc = WebContextFactory.get();
//		HttpServletRequest request = webc.getHttpServletRequest();
//		final HttpSession hs = request.getSession();
		ScriptSessionListener listener = new ScriptSessionListener() {
			public void sessionCreated(ScriptSessionEvent ev) {
				ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)SecurityUtils.getSubject().getPrincipal();
				//logger.debug("a ScriptSession is created! username:"+user.getUsername());
				ev.getSession().setAttribute("userInfo", user);
				
				//1.写入本地内存
				ScriptSessionMap.ScriptSessionDataMap.put(ev.getSession().getId(), ev.getSession());
				//2.调接口同步其他服务器
				
//				List<ScriptSession> scriptSessions = null;
//				if(null != hs.getAttribute("scriptSessions")) {
//					ScriptSessionData.scriptSessionList.add(ev.getSession());
//					hs.setAttribute("scriptSessions", ScriptSessionData.scriptSessionList);	
//				}
//				else {
//					scriptSessions = new ArrayList<ScriptSession>();
//					ScriptSessionData.scriptSessionList.clear();
//					ScriptSessionData.scriptSessionList.add(ev.getSession());
//					hs.setAttribute("scriptSessions", ScriptSessionData.scriptSessionList);			
//				}
			}

			public void sessionDestroyed(ScriptSessionEvent ev) {
				//logger.debug("a ScriptSession is distroyed  "+((ShiroRealm.ShiroUser)ev.getSession().getAttribute("userInfo")).getUsername());
				//1.更新本地
				ScriptSessionMap.ScriptSessionDataMap.remove(ev.getSession().getId());
				//2.调接口，同步其他服务，一次只传输一个ScriptSession
				
				//疑问？当ScriptSession数量相当大的时候，如何提升效率，保证数据正确性？可以增加分布式缓存
				//直接在分布式缓存中进行读写
				
			}
		};
		manager.addScriptSessionListener(listener);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
		
	}
}
