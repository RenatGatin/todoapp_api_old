package ca.gatin.model.signup;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class PreSignupUser {

	@NotBlank(message = "First name is a required field")
	@Pattern(regexp="^[a-zA-Z0-9\\s\\-\\.\\']*$", message="First name has to follow the pattern: [a-zA-Z0-9\\s\\-\\.\\']")
	@Size(min = 2, max = 20, message = "First name has to be between 2 - 20 characters")
	private String firstName;
	
	@NotBlank(message = "Last name is a required field")
	@Pattern(regexp="^[a-zA-Z0-9\\s\\-\\.\\']*$", message="Last name has to follow the pattern: [a-zA-Z0-9\\s\\-\\.\\']")
	@Size(min = 2, max = 20, message = "First name has to be between 2 - 20 characters")
	private String lastName;
	
	@NotBlank(message = "Email is a required field")
	@Email(message="Email has to follow valid email pattern")
	private String email;
	
	@NotBlank(message = "Password is a required field")
	@Size(min = 6, message = "Password has to contain min 6 characters")
	private String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
