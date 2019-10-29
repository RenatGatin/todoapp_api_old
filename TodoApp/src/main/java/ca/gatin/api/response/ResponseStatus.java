package ca.gatin.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The Enum class responses the status code for each API request.
 * For all successful call, the service responses status code 0;
 * 
 * The error code is started from 1000:
 * 1001-1500 system reserved
 * 1501-2000 general
 * 2001-2500 user / account related
 * 2501-3000 TodoItems related codes
 * 
 * @author RGatin
 * @since 02-Apr-2016
 */

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseStatus {
	
	SUCCESS																	(0,"OK"),
	
	SYSTEM_UNAVAILABLE														(1001, "System Unavailable"),
	SYSTEM_INTERNAL_ERROR													(1002, "System Internal Error"),
	
	MISSING_REQUIRED_FIELD													(1501, "Missing required field(s)"),
	RECORD_CREATE_ID_FAILURE												(1502, "Can not create record with specified ID value"),
	AUTHORIZATION_HEADER_INVALID											(1503, "Authorization header is invalid"),
	ACCESS_TOKEN_NOT_FOUND													(1504, "Provided access token not found"),
	NOT_ENOUGH_PRIVILEGIES													(1505, "Not enough privilegies for this operation"),
	ACTION_NOT_PERMITTED													(1506, "This action not permitted"),
	DATABASE_PERSISTANCE_ERROR												(1507, "Database persistance error"),
	DATABASE_RECORD_DUPLICATION 											(1508, "Database record duplication"),

	
	ACCOUNT_NOT_FOUND														(2001, "Account(s) Not Found"),
	ACCOUNT_INVALID															(2002, "Invalid Account"),
	ACCOUNT_LOCKED															(2003, "Locked Account"),
	INVALID_DATA															(2004, "Invalid Data"),
	ACCOUNT_DB_CREATION_FAILURE												(2005, "Account DB creation failure"),
	ACCOUNT_UNIQUE_FIELD_DUPLICATION 										(2006, "Account unique field(s) duplication"),
	ACCOUNT_NOT_UPDATED														(2008, "Account is not updated"),
	ACCOUNT_DB_UPDATION_FAILURE												(2009, "Account DB updation failure"),
	ACCOUNT_IS_ALREADY_ACTIVE												(2010, "Account is already active"),
	ACCOUNT_IS_ALREADY_INACTIVE												(2011, "Account is already inactive"),
	ACCOUNT_DB_DELETION_FAILURE												(2012, "Account DB deletion failure"),
	INVALID_ROLE															(2013, "Invalid role(s)"),
	AUTHORITIES_DB_FETCHING_FAILURE											(2014, "Authorities DB fetching failure"),
	NEW_PASSWORD_FIELDS_DOES_NOT_MATCH										(2015, "New password fields does not match"),
	NEW_PASSWORD_HAS_TO_BE_DIFFERENT										(2016, "New password has to be different than current"),
	OLD_PASSWORD_DOES_NOT_MATCH_CURRENT_VALUE								(2017, "Old password field does not match current value"),
	ERROR_SAVING_USER_IN_DATABASE											(2018, "Error saving user in database"),
	EMAIL_TRANSMISSION_ERROR												(2019, "Email transmission error"),
	PRESIGNUP_USER_STATUS_ACTIVATED											(2020, "PreSignupUser status is activated"),
	PRESIGNUP_USER_STATUS_NOT_ACTIVATED										(2021, "PreSignupUser status is not activated"),
	PRESIGNUP_USER_NOT_EXIST												(2022, "PreSignupUser does not exist"),
	PRESIGNUP_USER_INVALID_ACTIVATION_KEY									(2023, "PreSignupUser invalid activation key"),
	ACCOUNT_NOT_ACTIVATED													(2024, "Account is not activated"),
	ACCOUNT_NOT_ENABLED														(2025, "Account is not enabled"),
	RESET_PASSWORD_EMAIL_WAS_SENT											(2026, "Reset password email was already sent"),

	TODOITEM_CREATE_ID_FAILURE												(2501, "Can't TodoItem with specified ID value"),
	TODOITEM_NOT_FOUND														(2502, "TodoItem(s) Not Found"),
	TODOITEM_DB_CREATION_FAILURE											(2503, "TodoItem DB creation failure"),
	TODOITEM_DB_UPDATION_FAILURE											(2504, "TodoItem DB updation failure"),
	TODOITEM_NOT_PERSISTED_UPDATION_FAILURE									(2505, "Can't update TodoItem that hasn't been persisted"),
	TODOITEM_DB_DELETION_FAILURE											(2506, "TodoItem DB deletion failure"),
	UNAUTHORIZED_TODOITEM_REQUEST											(2507, "Unauthorized TodoItem request"),
	
	TODOLISTITEM_NOT_FOUND													(2601, "TodoListItem(s) Not Found");
	
	private int code;
	private String message;
	
	private ResponseStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
