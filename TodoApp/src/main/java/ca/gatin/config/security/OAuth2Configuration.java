package ca.gatin.config.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ca.gatin.model.security.Authorities;

/**
 * Main Oauth2 Configuration
 *
 * @author RGatin
 * @since Apr 17, 2016
 */
@Configuration
public class OAuth2Configuration {

	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		@Autowired
		private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

		@Autowired
		private CustomLogoutSuccessHandler customLogoutSuccessHandler;

		@Override
		public void configure(HttpSecurity http) throws Exception {

			http
					.exceptionHandling()
					.authenticationEntryPoint(customAuthenticationEntryPoint)
					.and()
					.logout()
					.logoutUrl("/oauth/logout")
					.logoutSuccessHandler(customLogoutSuccessHandler)
					.and()
					.csrf()
					.requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
					.disable()
					.headers()
					.frameOptions().disable()
					.and()
					.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
					.authorizeRequests()
					.antMatchers("/test/").permitAll()
					.antMatchers("/open/**").permitAll()
					.antMatchers("/common/**").authenticated()
					.antMatchers("/user/**").hasAuthority(Authorities.ROLE_USER.name()) //.antMatchers("/secure/**").authenticated()
					.antMatchers("/admin/**").hasAuthority(Authorities.ROLE_ADMIN.name()) // .antMatchers("/admin/**").hasAnyAuthority(Authorities.ROLE_ADMIN.name(), Authorities.ROLE_SUPERADMIN.name())
					.antMatchers("/superadmin/**").hasAuthority(Authorities.ROLE_SUPERADMIN.name());
		}
	}

	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration	extends AuthorizationServerConfigurerAdapter implements EnvironmentAware {

		private RelaxedPropertyResolver relaxedPropertyResolver;
		private static final String ENV_OAUTH = "authentication.oauth.";
		
		private static final String PROP_CLIENTID_WEB = "clientid.web";
		private static final String PROP_SECRET_WEB = "secret.web";
		private static final String PROP_TOKEN_VALIDITY_SECONDS_WEB = "tokenValidityInSeconds.web";
		
		private static final String PROP_ADMINID_RENAT = "adminid.renat";
		private static final String PROP_SECRET_RENAT = "secret.renat";
		private static final String PROP_TOKEN_VALIDITY_SECONDS_RENAT = "tokenValidityInSeconds.renat";
		
		@Autowired
		private DataSource dataSource;

		@Bean
		public TokenStore tokenStore() {
			return new JdbcTokenStore(dataSource);
		}

		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints
					.tokenStore(tokenStore())
					.authenticationManager(authenticationManager);
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory()
			
					.withClient(relaxedPropertyResolver.getProperty(PROP_ADMINID_RENAT))
					.secret(relaxedPropertyResolver.getProperty(PROP_SECRET_RENAT))
					.accessTokenValiditySeconds(relaxedPropertyResolver.getProperty(PROP_TOKEN_VALIDITY_SECONDS_RENAT, Integer.class, 1800))
		            .scopes("read", "write")
		            .authorities(Authorities.ROLE_USER.name(), Authorities.ROLE_ADMIN.name(), Authorities.ROLE_SUPERADMIN.name())
		            .authorizedGrantTypes("password", "refresh_token")
			
					.and()
					
	                .withClient(relaxedPropertyResolver.getProperty(PROP_CLIENTID_WEB))
	                .secret(relaxedPropertyResolver.getProperty(PROP_SECRET_WEB))
	                .accessTokenValiditySeconds(relaxedPropertyResolver.getProperty(PROP_TOKEN_VALIDITY_SECONDS_WEB, Integer.class, 1800))
	                .scopes("read", "write")
	                .authorities(Authorities.ROLE_USER.name())
	                .authorizedGrantTypes("password", "refresh_token")
		    ;
		}

		@Override
		public void setEnvironment(Environment environment) {
			this.relaxedPropertyResolver = new RelaxedPropertyResolver(environment, ENV_OAUTH);
		}
	}
}