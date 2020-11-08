package ca.gatin.util;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * Common Logger methods
 * 
 * @author RGatin
 * @since 12-Dec-2018
 *
 */
public class LoggerUtils {
	
	private static Logger logger = LoggerFactory.getLogger(LoggerUtils.class);
	
	public static void logAfterRequest(ResponseEntity<?> postResponse) {
		doLogAfterRequest(postResponse, null);
	}
	
	public static void logAfterRequest(ResponseEntity<?> postResponse, Logger loggerClass) {
		doLogAfterRequest(postResponse, loggerClass);
	}
	
	public static void logBeforeRequest(String url, HttpMethod httpMethod, HttpHeaders headers, HttpEntity<Object> entity, Class<?> responseType, Object bean) {
		doLogBeforeRequest(url, httpMethod, headers, entity, responseType, bean, null);
	}
	
	public static void logBeforeRequest(String url, HttpMethod httpMethod, HttpHeaders headers, HttpEntity<Object> entity, Class<?> responseType, Object bean, Logger loggerClass) {
		doLogBeforeRequest(url, httpMethod, headers, entity, responseType, bean, loggerClass);
	}
	
	private static void doLogAfterRequest(ResponseEntity<?> postResponse, Logger loggerClass) {
		Logger log = (loggerClass == null) ? logger : loggerClass;
		
		log.debug("After request");
		log.debug("HTTP Status code: ", postResponse.getStatusCode());
		logDebugHeaders(postResponse.getHeaders(), log);
		log.debug("body: " + postResponse.getBody() + "\n");
	}

	private static void doLogBeforeRequest(String url, HttpMethod httpMethod, HttpHeaders headers, HttpEntity<Object> entity, Class<?> responseType, Object bean, Logger loggerClass) {
		Logger log = (loggerClass == null) ? logger : loggerClass;
		
		log.debug("");
		log.debug("Before request");
		log.debug("url: " + url);
		log.debug("httpMethod: " + httpMethod);
		logDebugHeaders(headers, log);
		log.debug("responseType: " + responseType);
		log.debug("bean: " + bean + "\n");
	}
	
	/**
	 * Logs on warning level the details of secured resource request
	 * 
	 * @param request
	 * @param loggerClass
	 */
	public static void logSecuredResourceWarning(HttpServletRequest request, Logger loggerClass) {
		Logger log = (loggerClass == null) ? logger : loggerClass;
		log.warn(">>>>>>>>>>>>>> Blocked secured resource! JSESSIONID Not found in a registry map!");
		log.warn("domain: " + request.getServerName());
		log.warn("protocol: " + request.getProtocol());
		log.warn("JSESSIONID: " + request.getRequestedSessionId());
		log.warn("uri: " + request.getRequestURI());
		log.warn("pathParams: " + request.getQueryString());
		log.warn("address: " + request.getRemoteAddr());
		log.warn("host: " + request.getRemoteHost());
		log.warn("port: " + request.getRemotePort());
		logWarnHeaders(request, log);
	}
	
	/**
	 * Logs Http Request Details
	 * 
	 * @param request
	 * @param loggerClass
	 */
	public static void logHttpRequestDetails(HttpServletRequest request, Logger loggerClass) {
		Logger log = (loggerClass == null) ? logger : loggerClass;
		log.debug("Request details:");
		log.debug("domain: " + request.getServerName());
		log.debug("protocol: " + request.getProtocol());
		log.debug("JSESSIONID: " + request.getRequestedSessionId());
		log.debug("uri: " + request.getRequestURI());
		log.debug("pathParams: " + request.getQueryString());
		log.debug("address: " + request.getRemoteAddr());
		log.debug("host: " + request.getRemoteHost());
		log.debug("port: " + request.getRemotePort());
		logDebugHeaders(request, log);
	}
	
	private static void logDebugHeaders(HttpHeaders httpHeaders, Logger log) {
		log.debug("headers start ---");
		if (httpHeaders != null) {
			for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
				String headerName = entry.getKey();
				List<String> headerValues = entry.getValue();
				log.debug(headerName + ": " + headerValues);
			}
		}
		log.debug("headers end ---\n");
	}
	
	private static void logWarnHeaders(HttpServletRequest request, Logger log) {
		log.warn("headers start ---");
		
		@SuppressWarnings("rawtypes")
		Enumeration headerNames = request.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
	            String key = (String) headerNames.nextElement();
	            String value = request.getHeader(key);
	            log.warn(key + ": " + value);
	        }
		} else {
			log.warn("empty");
		}
		log.warn("headers end ---\n");
    }
	
	private static void logDebugHeaders(HttpServletRequest request, Logger log) {
		log.debug("headers start ---");
		
		@SuppressWarnings("rawtypes")
		Enumeration headerNames = request.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
	            String key = (String) headerNames.nextElement();
	            String value = request.getHeader(key);
	            log.debug(key + ": " + value);
	        }
		} else {
			log.debug("empty");
		}
		log.debug("headers end ---\n");
    }
}
