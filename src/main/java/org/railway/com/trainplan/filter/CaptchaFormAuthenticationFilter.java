package org.railway.com.trainplan.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.railway.com.trainplan.service.ShiroRealm;

public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,

	Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest
				.getHeader("X-Requested-With"))) {// 不是ajax请求
			issueSuccessRedirect(request, response);
		} else {
			httpServletResponse.setCharacterEncoding("UTF-8");
			PrintWriter out = httpServletResponse.getWriter();
			out.println("{success:true,message:'登入成功'}");
			out.flush();
			out.close();
		}
		return false;
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		if (!"XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request)
				.getHeader("X-Requested-With"))) {// 不是ajax请求
			setFailureAttribute(request, e);
			return true;
		}
		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String message = e.getClass().getSimpleName();
			if ("IncorrectCredentialsException".equals(message)) {
				out.println("{success:false,message:'密码错误'}");
			} else if ("UnknownAccountException".equals(message)) {
				out.println("{success:false,message:'账号不存在'}");
			} else if ("LockedAccountException".equals(message)) {
				out.println("{success:false,message:'账号被锁定'}");
			} else {
				out.println("{success:false,message:'未知错误'}");
			}
			out.flush();
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,ServletResponse response) throws Exception {
//		if ("cmdLogin2".equals(getPathWithinApplication(request))) {
		
		/*
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		if (isLoginRequest(request, response)) {

			if (isLoginSubmission(request, response)) {
				// if (log.isTraceEnabled()) {
				// log.trace("Login submission detected.  Attempting to execute login.");
				// }
				if ("XMLHttpRequest"
						.equalsIgnoreCase(((HttpServletRequest) request)
								.getHeader("X-Requested-With"))) {// 不是ajax请求
					String vcode = request.getParameter("vcode");
					HttpServletRequest httpservletrequest = (HttpServletRequest) request;
					String vvcode = "";
					// String vvcode = (String) httpservletrequest
					// .getSession()
					// .getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
					if (vvcode == null || "".equals(vvcode)
							|| !vvcode.equals(vcode)) {
						response.setCharacterEncoding("UTF-8");
						PrintWriter out = response.getWriter();
						out.println("{success:false,message:'验证码错误'}");
						out.flush();
						out.close();
						return false;
					}
				}
				return executeLogin(request, response);
			} else {
				// if (log.isTraceEnabled()) {
				// log.trace("Login page view.");
				// }
				// allow them to see the login page ;)
				return true;
			}
		} else {

			// if (log.isTraceEnabled()) {
			// log.trace("Attempting to access a path which requires authentication.  Forwarding to the "
			// + "Authentication url [" + getLoginUrl() + "]");
			// }
			if (!"XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {// 不是ajax请求
				saveRequestAndRedirectToLogin(request, response);
			} else {
				response.setCharacterEncoding("UTF-8");
				PrintWriter out = response.getWriter();
				out.println("{message:'login'}");
				out.flush();
				out.close();
			}
			return false;
		}
		
	*/	
		saveRequestAndRedirectToLogin(request, response);
		return executeLogin(request, response);
	}

}
