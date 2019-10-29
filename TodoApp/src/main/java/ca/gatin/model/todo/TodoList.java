package ca.gatin.model.todo;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import ca.gatin.model.security.User;

/**
 * todo_list table model
 *
 * @author RGatin
 * @since Oct 24, 2018
 */
@Entity
public class TodoList {

	@Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;
    
	@ManyToOne
	@JoinColumn(name="creator_id", nullable=false)
	private User creator;

    @Column(nullable = false)
    @Size(min = 0, max = 200)
    private String name;

    @Column(name = "date_created", nullable = false)
    private Date dateCreated;
    
    @Column(name = "date_last_modified")
    private Date dateLastModified;
    
    @Column(name = "hide_completed")
    private boolean hideCompleted;

	@OneToMany(targetEntity=ca.gatin.model.todo.TodoItem.class, mappedBy="listId")
    private Set<TodoItem> todoItems;
    
    @ManyToMany
    @JoinTable(
            name = "todo_list_share",
            joinColumns = @JoinColumn(name = "list_id"),
            inverseJoinColumns = @JoinColumn(name = "share_user_id"))
    private Set<User> shares;
    
    public boolean isHideCompleted() {
		return hideCompleted;
	}

	public void setHideCompleted(boolean hideCompleted) {
		this.hideCompleted = hideCompleted;
	}

	public Set<User> getShares() {
		return shares;
	}

	public void setShares(Set<User> shares) {
		this.shares = shares;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Set<TodoItem> getTodoItems() {
		return todoItems;
	}

	public void setTodoItems(Set<TodoItem> todoItems) {
		this.todoItems = todoItems;
	}

	@Override
	public String toString() {
		return "TodoList [id=" + id + ", creator=" + creator + ", name=" + name + ", dateCreated=" + dateCreated
				+ ", dateLastModified=" + dateLastModified + ", todoItems=" + todoItems + ", shares=" + shares + "]";
	}

}
