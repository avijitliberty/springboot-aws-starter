package com.example.aws.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity(name="user_image")
public class UserImage {
	
	@Id
	@Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	
	@Setter
	@Getter
	@Column(name = "s3_key", nullable = false, length=200)
	private String key;
	
	@Setter
	@Getter
	@Column(name = "url", nullable = false, length=2000)
	private String url;

	public UserImage(String key, String url) {
		super();
		this.key = key;
		this.url = url;
	}

	public UserImage() {
		super();
	}
}
