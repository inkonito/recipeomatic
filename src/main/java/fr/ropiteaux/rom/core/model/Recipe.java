package fr.ropiteaux.rom.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "RECIPE")
public class Recipe {
	// Season, lunch/diner, weight;

	private Long id;

	private String name;

	private Integer numberOfPerson;

	private Moment moment;

	private User user;

	public Recipe() {
		super();
	}

	public Recipe(Long id, String name, Integer numberOfPerson, Moment moment,
			User user) {
		super();
		this.id = id;
		this.name = name;
		this.numberOfPerson = numberOfPerson;
		this.moment = moment;
		this.user = user;
	}

	@Id
	@Column(name = "ID", updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlAttribute
	public Long getId() {
		return id;
	}

	public Moment getMoment() {
		return moment;
	}

	@Column(name = "NAME", unique = false, nullable = true)
	@XmlAttribute
	@NotBlank
	public String getName() {
		return name;
	}

	@Column(name = "NB_OF_PERS", unique = false, nullable = true)
	@XmlAttribute
	public Integer getNumberOfPerson() {
		return numberOfPerson;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "USER_ID", nullable = false)
	@NotNull
	@JsonBackReference("user")
	public User getUser() {
		return user;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMoment(Moment moment) {
		this.moment = moment;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumberOfPerson(Integer numberOfPerson) {
		this.numberOfPerson = numberOfPerson;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Recipe [id=" + id + ", name=" + name + ", user=" + user + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe other = (Recipe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
