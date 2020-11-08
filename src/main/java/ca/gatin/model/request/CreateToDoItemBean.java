package ca.gatin.model.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Create ToDoItem bean
 * 
 * @author RGatin
 * @since 28-Oct-2019
 */
public class CreateToDoItemBean {
	
	@NotEmpty
	@Length(min = 3)
	private String title;	
	private String notes;
	@NotNull
	private Long listId;
	
	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String value) {
		this.notes = value;
	}
}
