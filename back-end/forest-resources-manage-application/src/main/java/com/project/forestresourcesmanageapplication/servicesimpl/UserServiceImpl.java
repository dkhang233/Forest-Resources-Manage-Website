package com.project.forestresourcesmanageapplication.servicesimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.forestresourcesmanageapplication.dtos.UserDTO;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataNotFoundException;
import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.models.User;
import com.project.forestresourcesmanageapplication.repositories.AdministrationRepository;
import com.project.forestresourcesmanageapplication.repositories.UserRepository;
import com.project.forestresourcesmanageapplication.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final AdministrationRepository administrationRepository;

	@Override
	public List<UserDTO> retrieveAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserDTO> result = users.stream().map((user) -> {
			UserDTO userResponse = this.mapUserToUserDTO(user);
			return userResponse;
		}).toList();
		return result;
	}

	@Override
	public UserDTO retrieveUserByUsername(String username) {
		User user = this.userRepository.findById(username)
				.orElseThrow(() -> new DataNotFoundException("Not found user with username : " + username));
		UserDTO userDTO = this.mapUserToUserDTO(user);
		return userDTO;
	}

	@Override
	public UserDTO updateUser(String username, UserDTO userDTO) {
		User user = this.userRepository.findById(username)
				.orElseThrow(() -> new DataNotFoundException(
						"Not found user with username : " + username));
		String administrationName = userDTO.getAdministrationName();
		Administration administration = this.administrationRepository.findByName(administrationName)
				.orElseThrow(() -> new DataNotFoundException(
						"Not found administration with name: " + administrationName));
		user.setFirstName(userDTO.getFirstName())
				.setLastName(userDTO.getLastName())
				.setEmail(userDTO.getEmail())
				.setAvatar(userDTO.getAvatar())
				.setAddress(userDTO.getAddress())
				.setBirthDate(userDTO.getBirthDate())
				.setActive(userDTO.isActive())
				.setRole(userDTO.getRole())
				.setAdministration(administration);
		user = this.userRepository.save(user);
		userDTO = this.mapUserToUserDTO(user);
		return userDTO;
	}

	public UserDTO mapUserToUserDTO(User user) {
		UserDTO userDTO = UserDTO.builder()
				.username(user.getUsername())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.avatar(user.getAvatar())
				.address(user.getAddress())
				.birthDate(user.getBirthDate())
				.isActive(user.isActive())
				.role(user.getRole())
				.administrationName(user.getAdministration().getName())
				.build();
		return userDTO;
	}

	public User mapUserDTOToUser(UserDTO userDTO) {
		Administration administration = this.administrationRepository.findByName(userDTO.getAdministrationName())
				.orElseThrow(() -> new DataNotFoundException(null));
		User user = User.builder()
				.username(userDTO.getUsername())
				.firstName(userDTO.getFirstName())
				.lastName(userDTO.getLastName())
				.email(userDTO.getEmail())
				.avatar(userDTO.getAvatar())
				.address(userDTO.getAddress())
				.birthDate(userDTO.getBirthDate())
				.isActive(userDTO.isActive())
				.role(userDTO.getRole())
				.administration(administration)
				.build();
		return user;
	}
}
