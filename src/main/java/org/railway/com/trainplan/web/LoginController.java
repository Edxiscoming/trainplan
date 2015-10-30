/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.railway.com.trainplan.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.railway.com.trainplan.common.utils.IpUtil;
import org.railway.com.trainplan.service.MessageService;
import org.railway.com.trainplan.service.ShiroRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * 
 * 真正登录的POST请求由Filter完成,
 * 
 * @author calvin
 */
@Controller
public class LoginController {

	private final static Log logger = LogFactory.getLog(LoginController.class);

	@Autowired
	private MessageService messageService;

	@RequestMapping(value = "login1", method = RequestMethod.GET)
	public String login() {
		return "index";
	}

	/**
	 * 2015-4-16 11:11:56,zpd.
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView loginNew(HttpServletRequest req) {
		ModelAndView model = new ModelAndView();
		// System.out.println(IpUtil.getIpAddr(req));
		String ip = IpUtil.getIpAddr(req);
		model.addObject("ip", ip);
		model.setViewName("login");
		return model;
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String fail(
			@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName,
			Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,
				userName);
		logger.info("Login:" + userName + " login failed!");
		return "login";
	}

	@RequestMapping(value = "user/switch", method = RequestMethod.GET)
	public String logout() {
		Subject currentUser = SecurityUtils.getSubject();
		Object principal = currentUser.getPrincipal();
		if (principal == null) {
			return "redirect:../login";
		}
		String username = ((ShiroRealm.ShiroUser) principal).getUsername();
		currentUser.logout();
		logger.info("Logout:" + username + " logout!");
		logger.info("Logout BureauShortName:"
				+ ((ShiroRealm.ShiroUser) principal).getBureauShortName()
				+ " logout!");

		return "redirect:../login";
	}
	/**
	 * suntao ljdd 远程调用
	 * @return
	 */
	@RequestMapping(value = "remoteLogout", method = RequestMethod.GET)
	public String remoteLogout(){
		try {
			Subject currentUser = SecurityUtils.getSubject();
			Object principal = currentUser.getPrincipal();
			String username = ((ShiroRealm.ShiroUser) principal).getUsername();
			currentUser.logout();
			logger.info("Logout:" + username + " logout!");
			logger.info("Logout BureauShortName:"
					+ ((ShiroRealm.ShiroUser) principal).getBureauShortName()
					+ " logout!");
		} catch (Exception e) {
			
		}
		return "logout";
	}
	@RequestMapping(value = "index1", method = RequestMethod.GET)
	public String index1() {
		return "index";
	}

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView model = new ModelAndView();
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		if(null != user){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("receivePost", user.getPostName());
			map.put("statusCode", 0);
			model.addObject("msgCount", messageService.getReceiveMsgCount(map) == 0 ? "" : messageService.getReceiveMsgCount(map));
			model.addObject("user", user);
		}
		model.setViewName("index");
		return model;
	}
	@RequestMapping(value = "cmdLogin", method = RequestMethod.POST)
	public Object cmdLogin(@RequestParam String username, @RequestParam String password) {
		
		Subject currentUser = SecurityUtils.getSubject();
	    if (!currentUser.isAuthenticated()) {
	      UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//	      token.setRememberMe(rememberMe);
	    }
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		if(null != user){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("receivePost", user.getPostName());
			map.put("statusCode", 0);
		}
		return "index";
	}
	@RequestMapping(value = "cmdLogin2", method = RequestMethod.GET)
	public ModelAndView cmdLogin2(@RequestParam String username, @RequestParam String password) {
		ModelAndView model = new ModelAndView();
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token =null;
		if (!currentUser.isAuthenticated()) {
			token = new UsernamePasswordToken(username, password);
//	      token.setRememberMe(rememberMe);
		}
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		if(null != user){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("receivePost", user.getPostName());
			map.put("statusCode", 0);
		}
		model.setViewName("index");
		return model;
	}
}
