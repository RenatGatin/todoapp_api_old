package ca.gatin.api.response;

public class SimpleServiceResponse {

	public final static SimpleServiceResponse SuccessResponse = new SimpleServiceResponse(ResponseStatus.SUCCESS);

	private ResponseStatus status;
	private String message;

	public SimpleServiceResponse() {
	}

	public SimpleServiceResponse(ResponseStatus status) {
		setStatus(status);
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
		this.message = status.getMessage();
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void addReason(String reasonMessage) {
		this.message += ". Reason: " + reasonMessage;
	}
}
