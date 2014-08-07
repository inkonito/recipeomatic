package fr.ropiteaux.rom.core.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "USER")
public class User {

	private Long id;

	private String name;

	private String firstName;

	private String email;

	private String password;

	private Date authDate;

	private Date createDate;

	private List<Recipe> recipeList;

	private String authToken;

	public User() {
		super();
	}

	public User(Long id, String name, String firstName, String email,
			String password, Date authDate, Date createDate) {
		super();
		this.id = id;
		this.name = name;
		this.firstName = firstName;
		this.email = email;
		this.password = password;
		this.authDate = authDate;
		this.createDate = createDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Column(name = "AUTH_DATE", unique = false, nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@XmlAttribute
	@JsonIgnore
	public Date getAuthDate() {
		return authDate;
	}

	@Column(name = "AUTHTOKEN", unique = false, nullable = true)
	@XmlAttribute
	@JsonIgnore
	public String getAuthToken() {
		return authToken;
	}

	@Column(name = "CREATE_DATE", unique = false, nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@XmlAttribute
	@NotNull
	@JsonIgnore
	public Date getCreateDate() {
		return createDate;
	}

	@Column(name = "EMAIL", unique = true, nullable = false)
	@XmlAttribute
	@NotBlank
	public String getEmail() {
		return email;
	}

	@Column(name = "FIRST_NAME", unique = false, nullable = true)
	@XmlAttribute
	public String getFirstName() {
		return firstName;
	}

	@Id
	@Column(name = "ID", updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlAttribute
	public Long getId() {
		return id;
	}

	@Column(name = "NAME", unique = false, nullable = true)
	@XmlAttribute
	public String getName() {
		return name;
	}

	@Column(name = "PASSWORD", unique = false, nullable = false)
	@XmlAttribute
	@NotBlank
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@OrderBy("id")
	@XmlInverseReference(mappedBy = "user")
	@XmlElementWrapper
	@JsonManagedReference("recipes")
	public List<Recipe> getRecipeList() {
		return recipeList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public void setAuthDate(Date authDate) {
		this.authDate = authDate;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRecipeList(List<Recipe> recipeList) {
		this.recipeList = recipeList;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + "]";
	}

}
