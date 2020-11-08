package ca.gatin.config.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.util.JsonUtil;

/**
 * Spring Security logout handler
 * 
 * @author RGatin
 * @since 03-Apr-2016
 */
@Component
public class CustomLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {
	
	private final Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);

    private static final String BEARER_AUTHENTICATION = "Bearer ";
    private static final String HEADER_AUTHORIZATION = "authorization";

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Authentication authentication) throws IOException, ServletException {

        String headerAuthorization = httpServletRequest.getHeader(HEADER_AUTHORIZATION);
        logger.info("Logging out, headerAuthorization: " + headerAuthorization);
        
        if (headerAuthorization != null && headerAuthorization.startsWith(BEARER_AUTHENTICATION)) {
        	String accessTokenValue = headerAuthorization.split(" ")[1];
        	OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessTokenValue);
        	
            if (oAuth2AccessToken != null) {
            	OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
            	if (oAuth2RefreshToken != null) {
            		logger.info("Removing refreshToken");
            		tokenStore.removeRefreshToken(oAuth2RefreshToken);
            	}
            	
            	logger.info("Removing accessToken");
                tokenStore.removeAccessToken(oAuth2AccessToken);
                
                setJsonHttpReposnse(httpServletResponse, ResponseStatus.SUCCESS);
                
            } else {
            	logger.error(ResponseStatus.ACCESS_TOKEN_NOT_FOUND.getMessage());
            	setJsonHttpReposnse(httpServletResponse, ResponseStatus.ACCESS_TOKEN_NOT_FOUND);
            }
        } else {
        	logger.error(ResponseStatus.AUTHORIZATION_HEADER_INVALID.getMessage());
        	setJsonHttpReposnse(httpServletResponse, ResponseStatus.AUTHORIZATION_HEADER_INVALID);
        }

        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Sets up HttpServletResponse using ResponseStatus
     * 
     * @param httpServletResponse
     * @param responseStatus
     * @throws IOException
     */
	private void setJsonHttpReposnse(HttpServletResponse httpServletResponse, ResponseStatus responseStatus) throws IOException {
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter out = httpServletResponse.getWriter();
		out.write(JsonUtil.getJsonStringStatus(responseStatus));
	}

}
