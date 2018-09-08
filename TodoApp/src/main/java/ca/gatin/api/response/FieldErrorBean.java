package ca.gatin.api.response;

public class FieldErrorBean {

	private String field;
    private String message;
    
    public FieldErrorBean(String field, String message) {
        this.field = field;
        this.message = message;
    }
    
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
    public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}
