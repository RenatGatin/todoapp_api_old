package ca.gatin.model.request;

/**
 * Request Bean for Change Password
 *
 * @author RGatin
 * @since May 22, 2016
 */
public class ChangePasswordRequestBean {
	
	private String currentPassword;
	private String newPassword1;
	private String newPassword2;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword1() {
		return newPassword1;
	}

	public void setNewPassword1(String newPassword1) {
		this.newPassword1 = newPassword1;
	}

	public String getNewPassword2() {
		return newPassword2;
	}

	public void setNewPassword2(String newPassword2) {
		this.newPassword2 = newPassword2;
	}
	
}
