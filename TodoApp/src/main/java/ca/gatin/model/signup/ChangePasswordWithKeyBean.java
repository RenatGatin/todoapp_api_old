package ca.gatin.model.signup;

import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Bean for changing password through forgot password procedure
 * 
 * @author RGatin
 * @since 18-Sep-2018
 *
 */
public class ChangePasswordWithKeyBean {

	@NotBlank(message = "Password is a required field")
	@Size(min = 6, message = "Password must contain at least six characters")
	private String password;
	
	@NotBlank(message = "ResetPasswordKey is a required field")
	private String resetPasswordKey;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getResetPasswordKey() {
		return resetPasswordKey;
	}

	public void setResetPasswordKey(String resetPasswordKey) {
		this.resetPasswordKey = resetPasswordKey;
	}
}
