package ca.gatin.model.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Create ToDoList bean
 * 
 * @author RGatin
 * @since 07-Feb-2019
 */
public class CreateToDoListBean {
	
	@NotEmpty
	@Length(max = 200)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
