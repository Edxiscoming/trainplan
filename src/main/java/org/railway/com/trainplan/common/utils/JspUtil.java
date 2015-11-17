package org.railway.com.trainplan.common.utils;

import org.apache.shiro.SecurityUtils;
import org.railway.com.trainplan.service.ShiroRealm;
import org.springframework.web.servlet.ModelAndView;

/**
 * 返回页面所需要的user信息以及路径.
 * 
 * @author zhangPengDong
 *
 *         2015年3月31日 下午3:11:57
 */
public class JspUtil {

	public static ModelAndView returnJspUserAndBasePath(String basePath) {
		ModelAndView model = new ModelAndView();
		ShiroRealm.ShiroUser user = (ShiroRealm.ShiroUser) SecurityUtils
				.getSubject().getPrincipal();
		model.addObject("user", user);
		model.addObject("basePath", basePath);
		return model;
	}

}
