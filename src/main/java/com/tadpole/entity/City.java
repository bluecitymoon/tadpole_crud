package com.tadpole.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tad_city")
public class City implements Serializable {

	private static final long serialVersionUID = 5299983057729736628L;
	
	@Id
	@GeneratedValue
	protected Integer id;
	protected String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="province_id")
	protected Province province;
	
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
	
	public Province getProvince() {
		return province;
	}
	public void setProvince(Province province) {
		this.province = province;
	}
	
}
