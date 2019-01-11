package ca.gatin.api.controller.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import ca.gatin.util.LoggerUtils;

/**
 * 
 * Intercepter for all requests
 * 
 * @author RGatin
 * @since 10-Jan-2019
 *
 */
public class AnyReourceIntercepter implements HandlerInterceptor {

	private static Logger logger = LoggerFactory.getLogger(AnyReourceIntercepter.class);
	private static boolean isDebug = logger.isDebugEnabled();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.info("RequestURI: " + request.getRequestURI());
		
		/*
		 * Don't cache any resources 
		 */
		/*
		response.addDateHeader("Expires", 1);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control","no-cache,post-check=0,pre-check=0,must-revalidate,no-store");
		*/
		if (isDebug) {
			LoggerUtils.logHttpRequestDetails(request, logger);
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}
	
}
