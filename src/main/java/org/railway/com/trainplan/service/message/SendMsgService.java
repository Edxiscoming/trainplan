package org.railway.com.trainplan.service.message;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.railway.com.trainplan.common.constants.ScriptSessionMap;
import org.railway.com.trainplan.entity.Role;
import org.railway.com.trainplan.service.ShiroRealm;
import org.springframework.stereotype.Service;

@Service
public class SendMsgService {
	private static final Logger logger = Logger.getLogger(SendMsgService.class);

	/**
	 * 消息推送
	 * @param message 消息内容
	 * @param page 页面url
	 * @param jsFuncName 页面js方法名称
	 */
		
	public void sendMessage(final String message, final String pageUrl, final String jsFuncName) {
		logger.debug("~~~~向页面page："+pageUrl +"  推送消息内容："+message +" jsFuncName:"+jsFuncName);
		
		
		try{
			Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
				
				public boolean match(ScriptSession session) {
					return true;
		//				if(null != session) {
		//					logger.debug("session.getPage()：" + session.getPage());
		//					if ((pageUrl).equals(session.getPage())) {					
		//						return true;
		//					} else {
		//						return false;
		//					}
		//				}
		//				else {
		//					return true;
		//				}
				}		
			}, new Runnable() {
				private ScriptBuffer script = new ScriptBuffer();
		
				public void run() {
					script.appendCall(jsFuncName, message);				
					//Collection<ScriptSession> sessions = Browser.getTargetSessions();
					Collection<ScriptSession> sessions = ScriptSessionMap.ScriptSessionDataMap.values();
					if(!sessions.isEmpty()) {
						for (ScriptSession scriptSession : sessions) {
							if((pageUrl).equals(scriptSession.getPage())) {
								scriptSession.addScript(script);
							}		
						}
					}
					
					//判断这个信息是否要分发，如果要，则查询出配置列表中的url，然后依次调用分发
//					if(isShare && configIsShare) {
//						 DwrMessageData dwrMessageData = new DwrMessageData();
//						 dwrMessageData.setJsFuncName(jsFuncName);
//						 dwrMessageData.setMessage(message);
//						 dwrMessageData.setPageUrl(pageUrl);
//						 try {
//							shareDwrMsg(dwrMessageData);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
				}
			});
		}
		catch(Exception e) {
			logger.error("Browser Exception maybe null or DwrmsgShareUrl maybe null");
//			if(isShare && configIsShare) {
//				 DwrMessageData dwrMessageData = new DwrMessageData();
//				 dwrMessageData.setJsFuncName(jsFuncName);
//				 dwrMessageData.setMessage(message);
//				 dwrMessageData.setPageUrl(pageUrl);
//				 try {
//					shareDwrMsg(dwrMessageData);
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}
		}
	}
	
	
	
	/**
	 * 消息推送给某用户
	 * @param username 登录名
	 * @param message 消息内容
	 * @param page 页面url
	 * @param jsFuncName 页面js方法名称
	 */
	public void sendMessageToUser(final String username, final String message, final String pageUrl, final String jsFuncName) {
		logger.debug("~~~~username:"+username+"  向页面page："+pageUrl +"  推送消息内容："+message +" jsFuncName:"+jsFuncName);
		
		Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
			public boolean match(ScriptSession session) {
				ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser)session.getAttribute("userInfo");
				//System.err.println("!!!!!!!! session.getPage()="+session.getPage()+" session.username ="+user.getUsername());
				if ((pageUrl).equals(session.getPage()) && user!=null && user.getUsername().equals(username)) {
					return true;
				} else {
					return false;
				}
			}
		}, new Runnable() {
			private ScriptBuffer script = new ScriptBuffer();

			public void run() {
				script.appendCall(jsFuncName, message);
				Collection<ScriptSession> sessions = Browser
						.getTargetSessions();
				for (ScriptSession scriptSession : sessions) {
					scriptSession.addScript(script);
				}
			}
		});
	}
	
	
	
	/**
	 * 消息推送给某类角色
	 * @param rolename 
	 * @param message 消息内容
	 * @param page 页面url
	 * @param jsFuncName 页面js方法名称
	 */
	public void sendMessageToRole(final String rolename, final String message, final String pageUrl, final String jsFuncName) {
		logger.info("~~~~rolename:"+rolename+"  向页面page："+pageUrl +"  推送消息内容："+message +" jsFuncName:"+jsFuncName);
		
		Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
			public boolean match(ScriptSession session) {
				if ((pageUrl).equals(session.getPage())) {
					ShiroRealm.ShiroUser shiroUser = (ShiroRealm.ShiroUser)session.getAttribute("userInfo");
	                if(shiroUser!=null && shiroUser.getRoleList() != null && !shiroUser.getRoleList().isEmpty()) {
	                	for(Role role: shiroUser.getRoleList()) {
	                		if (rolename.equals(role.getName())) {
	                			return true;
	                		}
	                    }
	                } else {
	                	return false;
	                }
				} else {
					return false;
				}
				return false;
			}
		}, new Runnable() {
			private ScriptBuffer script = new ScriptBuffer();

			public void run() {
				script.appendCall(jsFuncName, message);
				Collection<ScriptSession> sessions = Browser
						.getTargetSessions();
				for (ScriptSession scriptSession : sessions) {
					scriptSession.addScript(script);
				}
			}
		});
	}
	
//	private Collection<ScriptSession> getScriptSessions() {
////		Collection pages = ScriptSessionMap.ScriptSessionDataMap.values();
////		Util util = new Util(pages);
//		Collection<ScriptSession> sessions = new ArrayList<ScriptSession>();
//		Set<String> keySet = ScriptSessionMap.ScriptSessionDataMap.keySet();
//	    for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
//	        String key = (String) it.next();
//	        sessions.add(ScriptSessionMap.ScriptSessionDataMap.get(key));
//	    }
//	    
//	    return sessions;
//	}
	
//	private void shareDwrMsg(DwrMessageData dwrMessageData) throws Exception {
//		//调用失败暂不处理
//		String params = JSONObject.fromObject(dwrMessageData).toString();
//        List<String> urlList = StringUtil.getDwrmsgShareUrl(urls);
//        if(!urlList.isEmpty()) {
//        	for(String url : urlList) {
//        		httpClientService.sendHttpClient(url, params, OperationConstants.REQUEST_METHOD.POST);
//        	}
//        }    
//	}
}
