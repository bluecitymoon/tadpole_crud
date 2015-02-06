package com.tadpole.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tad_user")
public class User implements Serializable {

	private static final long serialVersionUID = -9150498313341113868L;
	@Id
	@GeneratedValue
	protected Integer id;
	protected String name;
	protected String username;
	protected String password;
	protected Boolean active;
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "tad_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	protected List<Role> roles;

	public User() {

		super();
	}

	public User(String name, String username, String password, Boolean active) {

		super();
		this.name = name;
		this.username = username;
		this.password = password;
		this.active = active;
	}

	public Boolean getActive() {

		return active;
	}

	public void setActive(Boolean active) {

		this.active = active;
	}

	public Integer getId() {

		return id;
	}

	public void setId(Integer id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getUsername() {

		return username;
	}

	public void setUsername(String username) {

		this.username = username;
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password;
	}

	public List<Role> getRoles() {

		return roles;
	}

	public void setRoles(List<Role> roles) {

		this.roles = roles;
	}
	
}
