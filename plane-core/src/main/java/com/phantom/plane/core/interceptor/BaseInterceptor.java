package com.phantom.plane.core.interceptor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.phantom.plane.core.base.BaseEntity;

/**
 * 自定义拦截器 Created by BlueT on 2017/3/9.
 */
@Component
public class BaseInterceptor extends HandlerInterceptorAdapter {
	private static Set<String> path = new HashSet<String>();
	static { // 不拦截的路径,如果放行error会出错
		path.add("/login");
		path.add("/loginCtrl/login");

		path.add("/toLogin");
		path.add("/home");
		path.add("/simi/api");
		path.add("/");
		path.add("/getcode");

	}

	/**
	 * 拦截请求 前，此处可设置session,characterEncoding
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		try {
			HttpSession session = request.getSession();
			BaseEntity user = new BaseEntity();
			session.setAttribute("loginUser", user);
			long start = System.currentTimeMillis();

			request.setAttribute("start", start);
			if (session.getAttribute("loginUser") != null) {
				return true;
			} else if (path.contains(request.getRequestURI())) {
				System.out.println("---放行：" + request.getRequestURI());
				// response.sendRedirect("/toLogin");
				return true;
			} else {
				System.out.println("---拦截：" + request.getRequestURI());

				request.setAttribute("url", request.getRequestURI());
				String url = "/toLogin";
				response.sendRedirect(url);
				System.err.println("---重定向成功:" + url);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 拦截请求完成后执行的操作
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		request.setAttribute("time", new Date().toLocaleString());
	}

}
