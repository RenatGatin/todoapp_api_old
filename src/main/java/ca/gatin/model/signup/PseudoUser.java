package ca.gatin.model.signup;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

/**
 * pseudo_user table model
 *
 * @author RGatin
 * @since Aug 25, 2018
 */
@Entity
public class PseudoUser {

	@Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;
    
    @Column(nullable = false)
    @Size(min = 0, max = 50)
    private String username;
    
    @Column(nullable = false)
    @Size(min = 0, max = 50)
    private String firstname;
    
    @Column(nullable = false)
    @Size(min = 0, max = 50)
    private String lastname;

    @Column(nullable = false)
    @Size(min = 0, max = 500)
    private String password;

    @Email
    @Column(nullable = false)
    @Size(min = 0, max = 50)
    private String email;

    private boolean activated;

    @Size(min = 0, max = 500)
    @Column(name = "activationkey")
    private String activationKey;

    @Column(name = "date_created")
    private Date dateCreated;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public String toString() {
		return "PseudoUser [id=" + id + ", username=" + username + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", password=" + password + ", email=" + email + ", activated=" + activated + ", activationKey="
				+ activationKey + ", dateCreated=" + dateCreated + "]";
	}
}
