package ca.gatin.model.security;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * autority table model
 *
 * @author RGatin
 * @since Apr 17, 2016
 */
@Entity
public class Authority {

	@Id
	@NotNull
    @GeneratedValue
    private Long id;
	
    @NotNull
    @Size(min = 0, max = 50)
    private String name;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authority authority = (Authority) o;

        if (!id.equals(authority.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Authority{" +
        		"id='" + id + '\'' +
                "name='" + name + '\'' +
				'}';
	}

}
