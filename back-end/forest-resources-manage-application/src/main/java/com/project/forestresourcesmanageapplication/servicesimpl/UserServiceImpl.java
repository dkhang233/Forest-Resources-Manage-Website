package com.project.forestresourcesmanageapplication.servicesimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.project.forestresourcesmanageapplication.dtos.UserDTO;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataAlreadyExistsException;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataNotFoundException;
import com.project.forestresourcesmanageapplication.exceptionhandling.InvalidDataException;
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
	public UserDTO createUser(UserDTO userDTO, MultipartFile avatarFile) {
		if (this.userRepository.findById(userDTO.getUsername()).isPresent()) {
			throw new DataAlreadyExistsException("Username đã tồn tại");
		}
		User user = this.mapUserDTOToUser(userDTO);
		String avatar = this.saveImage(avatarFile);
		user.setAvatar(avatar);
		user.setPassword("password");
		this.userRepository.save(user);
		userDTO = this.mapUserToUserDTO(user);
		return userDTO;
	}

	@Override
	public UserDTO retrieveUserByUsername(String username) {
		User user = this.userRepository.findById(username)
				.orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
		UserDTO userDTO = this.mapUserToUserDTO(user);
		return userDTO;
	}

	@Override
	public UserDTO updateUser(String username, UserDTO userDTO, MultipartFile avatarFile) {
		User user = this.userRepository.findById(username)
				.orElseThrow(() -> new DataNotFoundException(
						"Username không tồn tại"));
		String administrationName = userDTO.getAdministrationName();
		Administration administration = this.administrationRepository.findByName(administrationName)
				.orElseThrow(() -> new DataNotFoundException(
						"Đơn vị hành chính trực thuộc không tồn tại"));

		// Kiểm tra avatar đã thay đổi chưa, nếu đã thay đổi -> gọi hàm để lưu file
		// avatar và thay đổi avatar
		if (!user.getAvatar().equals(userDTO.getAvatar())) {
			String avatar = this.saveImage(avatarFile);
			user.setAvatar(avatar);
		}

		user.setFirstName(userDTO.getFirstName())
				.setLastName(userDTO.getLastName())
				.setEmail(userDTO.getEmail())
				.setAddress(userDTO.getAddress())
				.setBirthDate(userDTO.getBirthDate())
				.setActive(userDTO.isActive())
				.setRole(userDTO.getRole())
				.setAdministration(administration);
		user = this.userRepository.save(user);
		userDTO = this.mapUserToUserDTO(user);
		return userDTO;
	}

	// lƯu file ảnh avatar và trả về đường dẫn đến ảnh
	public String saveImage(MultipartFile avatarFile) {
		if (avatarFile == null) {
			return "";
		}

		// Kiểm tra kích thước file
		if (avatarFile.getSize() > 10 * 1024 * 1024) { // kích thước file phải <= 10 MB
			throw new InvalidDataException("Kích thước ảnh đại diện phải nhỏ hơn 10MB");
		}

		// Kiểm tra định dạng file
		String contentType = avatarFile.getContentType();

		if (contentType == null || !contentType.startsWith("image/")) { // Phải là file ảnh
			throw new InvalidDataException("Ảnh đại diện phải là ảnh");
		}

		// Trích xuất và làm sạch tên file gốc từ hệ thống file của client
		String fileName = StringUtils.cleanPath(avatarFile.getOriginalFilename());

		// Tạo ra một tên file duy nhất
		String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

		// Tạo đường dẫn để lưu file
		Path uploadDir = Path.of("uploads");

		try {
			if (!Files.exists(uploadDir)) {
				Files.createDirectories(uploadDir);
			}
			Path uploadPath = Path.of(uploadDir.toString(), uniqueFileName);
			Files.copy(avatarFile.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}

		return uniqueFileName;
	}

	// Chuyển từ user sang userDTO
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

	// Chuyển từ userDTO sang user
	public User mapUserDTOToUser(UserDTO userDTO) {
		Administration administration = this.administrationRepository.findByName(userDTO.getAdministrationName())
				.orElseThrow(() -> new DataNotFoundException("Đơn vị hành chính trực thuộc không tồn tại"));
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
