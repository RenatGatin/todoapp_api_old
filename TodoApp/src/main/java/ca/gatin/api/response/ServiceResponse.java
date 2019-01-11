package ca.gatin.api.response;

/**
 * Wrapper to API JSON response
 * 
 * @author RGatin
 * @since 17-Apr-2016
 *
 * @param <T>
 */
public class ServiceResponse<T> extends SimpleServiceResponse {
	
	private T entity;

	public ServiceResponse() {
		super();
	}
	
	public ServiceResponse(ResponseStatus status) {
		super(status);
	}
	
	public ServiceResponse(ResponseStatus status, String message) {
		super(status);
		super.setMessage(message);
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
}
