package com.example.aws.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import com.example.aws.model.Address;
import com.example.aws.model.User;
import com.example.aws.model.UserImage;
import com.example.aws.repository.SpringbootAwsStarterRepository;

@Service
public class SpringbootAwsStarterServiceImpl implements SpringbootAwsStarterService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private SpringbootAwsStarterRepository repository;

	@Autowired
	private SpringCloudS3Service springCloudS3Service;

	@Override
	public List<User> findUsers(Long id) {
		List<User> users = new ArrayList<User>();

		if (id != null) {
			User userById = repository.findById(id).get();
			if (userById == null) {
				throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "user does not exist");
			} else {
				users.add(userById);
			}
		} else {
			for (User user : repository.findAll()) {
				users.add(user);
			}
		}
		return users;
	}

	@Override
	public Long delete(Long id) {
		User existing = repository.findById(id).get();

		if (existing == null) {
			log.info("user does not exist: " + id);
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "user does not exist");
		} else {
			springCloudS3Service.deleteImageFromS3(existing.getUserImage());
			repository.delete(existing);
			log.info("deleted user: {}", id);
			return id;
		}
	}

	@Override
	public User create(String firstName, String lastName, String email, String dateOfBirth, String street, String city,
			String county, String postcode, MultipartFile image) throws ParseException {
		UserImage userImage = springCloudS3Service.saveFileToS3(image);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date birthDate = df.parse(dateOfBirth);
		
		User user = new User(firstName, lastName, email, birthDate, userImage,
				new Address(street, city, county, postcode));
		return repository.save(user);
	}

	@Override
	public User update(Long id,String firstName, String lastName, String email, String dateOfBirth, String street, String city,
			String county, String postcode) throws ParseException {
		User existing = repository.findById(id).get();

		if (existing == null) {
			log.info("user does not exist: " + firstName);
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "user does not exist");
		} else {

			existing.setFirstName(firstName);
			existing.setLastName(lastName);
			existing.setEmail(email);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date birthDate = df.parse(dateOfBirth);
			existing.setDateOfBirth(birthDate);
			Address updatedAddress = new Address(street, city, county, postcode);
			existing.setAddress(updatedAddress);
											
			log.info("user updated: {}", existing.toString());
			return repository.save(existing);
		} 
	}

}
