package ca.gatin.config.security;

import org.springframework.security.core.AuthenticationException;

/**
 * User Not Activated handler
 *
 * @author RGatin
 * @since Apr 17, 2016
 *
 */
public class UserNotActivatedException extends AuthenticationException {

	private static final long serialVersionUID = 5121925212616310098L;

	public UserNotActivatedException(String msg, Throwable t) {
        super(msg, t);
    }

    public UserNotActivatedException(String msg) {
		super(msg);
	}
}
