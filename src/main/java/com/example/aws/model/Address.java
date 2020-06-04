package com.example.aws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity(name="address")
public class Address {
	
	@Id
	@Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@Setter
	@Getter
	@Column(name = "street", nullable = false, length=40)
	private String street;
	
	@Setter
	@Getter
	@Column(name = "city", nullable = false, length=40)
	private String city;
	
	@Setter 
	@Getter
	@Column(name = "county", nullable = false, length=40)
	private String county;

	@Setter
	@Getter
	@Column(name = "postcode", nullable = false, length=40)
	private String postcode;
	
	public Address(String street, String city, String county, String postCode) {
		this.street = street;
		this.city = city;
		this.county = county;
		this.postcode = postCode;
	}

	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
