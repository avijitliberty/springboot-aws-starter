package com.example.aws.model;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.example.aws.validation.ValidImage;

import lombok.Getter;
import lombok.Setter;

public class UserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Setter
	@Getter
	private Long id;

	@Setter
	@Getter
	@NotEmpty
	private String firstName;

	@Setter
	@Getter
	@NotEmpty
	private String lastName;

	@Setter
	@Getter
	@Email
	@NotEmpty
	private String email;

	@Setter
	@Getter
	@NotEmpty
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String dateOfBirth;

	@Setter
	@Getter
	@ValidImage
	private MultipartFile image;
	
	@Setter
	@Getter
	private String imageUrl;
	

	@Setter
	@Getter
	@NotEmpty
	private String street;

	@Setter
	@Getter
	@NotEmpty
	private String city;

	@Setter
	@Getter
	private String county;

	@Setter
	@Getter
	@NotEmpty
	private String postcode;

	public UserDto(Long id, @NotEmpty String firstName, @NotEmpty String lastName, @Email @NotEmpty String email,
			String birtdate, MultipartFile image, String imageUrl, @NotEmpty String street, @NotEmpty String city, String county,
			@NotEmpty String postcode) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.dateOfBirth = birtdate;
		this.image = image;
		this.imageUrl = imageUrl;
		this.street = street;
		this.city = city;
		this.county = county;
		this.postcode = postcode;
	}

	public UserDto() {
		super();
	}
}
