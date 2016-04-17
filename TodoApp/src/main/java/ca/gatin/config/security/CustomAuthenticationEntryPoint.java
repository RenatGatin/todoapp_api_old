package ca.gatin.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Unauthorized response configuration
 *
 * @author RGatin
 * @since Apr 17, 2016
 *
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

	public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException)
			throws IOException, ServletException {

		log.info("Pre-authenticated entry point called. Rejecting access");
		httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied. Secured resource - Authorization is required!");
	}
}