package com.example.aws.services;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import com.example.aws.model.User;

public interface SpringbootAwsStarterService {

	User create(String firstName, String lastName, String email, String dateOfBirth, String street, String city,
			String county, String postcode, MultipartFile image) throws ParseException;

	User update(Long id, String firstName, String lastName, String email, String dateOfBirth, String street,
			String city, String county, String postcode) throws ParseException;

	List<User> findUsers(Long id);

	Long delete(Long id);

}
