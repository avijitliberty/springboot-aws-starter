package com.example.aws.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.aws.model.User;
import com.example.aws.model.UserDto;
import com.example.aws.services.SpringbootAwsStarterService;

@Controller
public class SpringbootAwsStarterController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private SpringbootAwsStarterService service;

	@RequestMapping(method = RequestMethod.GET, path = "/user-form")
	public String showAddUserForm(Model model) {
		model.addAttribute("userDto", new UserDto());
		return "add-user";
	}

	@RequestMapping(value = "/add-user", method = RequestMethod.POST)
	public String addUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result,
			HttpServletRequest request, Model model) throws IOException, ParseException {
		if (result.hasErrors()) {
			model.addAttribute("userDto", userDto);
			return "add-user";
		}

		User createdUser = service.create(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(),
				userDto.getDateOfBirth(), userDto.getStreet(), userDto.getCity(), userDto.getCounty(),
				userDto.getPostcode(), userDto.getImage());
		if (createdUser != null) {
			log.info("user created: " + createdUser.toString());
			List<User> users = service.findUsers(null);
			model.addAttribute("users", users);
		}
		return "users";
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = { "/users", "/" })
	public String loadUsers(Model model) {

		List<User> users = service.findUsers(null);
		model.addAttribute("users", users);
		return "users";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/get-user/{id}")
	public String getUserById(Model model, @PathVariable Long id) {

		User user = service.findUsers(id).get(0);

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String birthDate = df.format(user.getDateOfBirth());
		
		UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(),
				user.getEmail(), birthDate, null, user.getUserImage().getUrl(), user.getAddress().getStreet(), user.getAddress().getCity(),
				user.getAddress().getCounty(), user.getAddress().getPostcode());

		model.addAttribute("userDto", userDto);

		return "edit-user";
	}

	@RequestMapping(method = RequestMethod.POST, path = "/edit-user")
	public String editUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model) throws ParseException {

		if (result.hasErrors()) {
			model.addAttribute("userDto", userDto);
			return "edit-user";
		}

		User updatedUser = service.update(userDto.getId(),userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(),
				userDto.getDateOfBirth(), userDto.getStreet(), userDto.getCity(), userDto.getCounty(),
				userDto.getPostcode());
		if (updatedUser != null) {
			log.info("user updated: " + updatedUser.toString());
			List<User> users = service.findUsers(null);
			model.addAttribute("users", users);
		}
		return "users";
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/delete-user/{id}")
	public String deleteUser(Model model, @PathVariable Long id) {

		Long deletedId = service.delete(id);

		if (deletedId != null) {
			log.info("user deleted: " + deletedId);
			List<User> users = service.findUsers(null);
			model.addAttribute("users", users);
		}
		return "users";
	}
}
