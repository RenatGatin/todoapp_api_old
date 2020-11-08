package ca.gatin.model.todo;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Date;
import java.util.Set;

/**
 * todo_item table model
 *
 * @author RGatin
 * @since Oct 24, 2018
 */
@Entity
public class TodoItem {

	@Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;
    
    @Column(name = "list_id", nullable = false)
    private Long listId;

    @Column(nullable = false)
    @Size(min = 0, max = 500)
    private String title;

    @Column(nullable = false)
    @Size(min = 0, max = 5000)
    private String notes;
    
    private boolean completed;
    
    private boolean hidden;
    
    @Column(name = "date_created", nullable = false)
    private Date dateCreated;
    
    @Column(name = "date_last_modified")
    private Date dateLastModified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	@Override
	public String toString() {
		return "TodoItem [id=" + id + ", listId=" + listId + ", title=" + title + ", notes=" + notes + ", completed="
				+ completed + ", hidden=" + hidden + ", dateCreated=" + dateCreated + ", dateLastModified="
				+ dateLastModified + "]";
	}
	
}
