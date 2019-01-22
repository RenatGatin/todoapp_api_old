package ca.gatin.model.request;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Simple request Bean with one string property (validated that is not empty). 
 * Can be re used for multiple cases.
 * 
 * @author RGatin
 * @since 21-Jan-2019
 */
public class SimpleStringBean {
	
	@NotEmpty
	private String stringVar;

	public String getStringVar() {
		return stringVar;
	}

	public void setStringVar(String stringVar) {
		this.stringVar = stringVar;
	}
}
