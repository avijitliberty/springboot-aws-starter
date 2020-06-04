package com.example.aws.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	@Id
	@Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Setter
	@Getter
	@Column(name = "first_name")
	private String firstName;

	@Setter
	@Getter
	@Column(name = "last_name")
	private String lastName;

	@Setter
	@Getter
	@Column(name = "email")
	private String email;

	@Setter	
	@Getter
	@Column(name = "date_of_birth")
	private Date dateOfBirth;

	@Setter
	@Getter
	@OneToOne(cascade = { CascadeType.ALL })
	private UserImage userImage;

	@Setter
	@Getter
	@OneToOne(cascade = { CascadeType.ALL })
	private Address address;

	public User() {
	}

	public User(String firstName, String lastName, String email, Date dateOfBirth,
			UserImage userImage, Address address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.userImage = userImage;
		this.address = address;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + "]";
	}
}
