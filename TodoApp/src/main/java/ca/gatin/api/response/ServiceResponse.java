package ca.gatin.api.response;

/**
 * Wrapper to API JSON response
 * 
 * @author RGatin
 * @since 17-Apr-2016
 *
 * @param <T>
 */
public class ServiceResponse<T> {
	private T entity;
	private ResponseStatus status;

	public ServiceResponse(ResponseStatus status) {
		this.status = status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
}
