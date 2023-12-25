package com.project.forestresourcesmanageapplication.servicesimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.forestresourcesmanageapplication.dtos.ChangePasswordDTO;
import com.project.forestresourcesmanageapplication.dtos.LoginDTO;
import com.project.forestresourcesmanageapplication.dtos.NewUserDTO;
import com.project.forestresourcesmanageapplication.dtos.ResetPasswordDTO;
import com.project.forestresourcesmanageapplication.dtos.UserDTO;
import com.project.forestresourcesmanageapplication.dtos.verifyOtpDTO;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataAlreadyExistsException;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataNotFoundException;
import com.project.forestresourcesmanageapplication.exceptionhandling.InvalidDataException;
import com.project.forestresourcesmanageapplication.models.AccessLog;
import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.models.User;
import com.project.forestresourcesmanageapplication.repositories.AccessLogRepository;
import com.project.forestresourcesmanageapplication.repositories.AdministrationRepository;
import com.project.forestresourcesmanageapplication.repositories.UserRepository;
import com.project.forestresourcesmanageapplication.responses.AccessLogResponse;
import com.project.forestresourcesmanageapplication.services.UserService;
import com.project.forestresourcesmanageapplication.utils.EmailUtil;
import com.project.forestresourcesmanageapplication.utils.OtpUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final AdministrationRepository administrationRepository;
	private final AccessLogRepository accessLogRepository;
	private final EmailUtil emailUtil;
	private final OtpUtil otpUtil;

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
	public UserDTO createUser(NewUserDTO newUserDTO, MultipartFile avatarFile) {
		if (this.userRepository.findById(newUserDTO.getUsername()).isPresent()) {
			throw new DataAlreadyExistsException("Username đã tồn tại");
		}
		User user = this.mapNewUserDTOToUser(newUserDTO);
		String avatar = this.saveImage(avatarFile);
		user.setAvatar(avatar);
		this.userRepository.save(user);
		UserDTO userDTO = this.mapUserToUserDTO(user);
		return userDTO;
	}

	@Override
	public UserDTO retrieveUserByUsernameOrEmail(String usernameOrEmail) {
		User user = this.userRepository.findByUsernameOrEmail(usernameOrEmail)
				.orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
		UserDTO userDTO = this.mapUserToUserDTO(user);
		return userDTO;
	}

	@Override
	public UserDTO updateUserByAdmin(String username, UserDTO userDTO, MultipartFile avatarFile) {
		User user = this.userRepository.findById(username)
				.orElseThrow(() -> new DataNotFoundException(
						"Username không tồn tại"));
		String administrationCode = userDTO.getAdministrationCode();
		Administration administration = this.administrationRepository.findById(administrationCode)
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

	@Override
	public UserDTO updateUser(String username, UserDTO userDTO, MultipartFile avatarFile) {
		User user = this.userRepository.findById(username)
				.orElseThrow(() -> new DataNotFoundException(
						"Username không tồn tại"));
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
				.setBirthDate(userDTO.getBirthDate());
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

	@Override
	public String login(LoginDTO loginDTO) {
		User user = this.userRepository.findByUsernameOrEmail(loginDTO.getUsername())
				.orElseThrow(() -> new DataNotFoundException("Username hoặc mật khẩu không chính xác"));
		if (user.isActive() != true || !user.getPassword().equals(loginDTO.getPassword())) {
			throw new InvalidDataException("Username hoặc mật khẩu không chính xác");
		}
		return loginDTO.getUsername();
	}

	@Override
	public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
		User user = this.userRepository.findByEmail(resetPasswordDTO.getEmail())
				.orElseThrow(() -> new DataNotFoundException("Email chưa được sử dụng"));
		String otp = otpUtil.generateOtp();
		user.setOtp(otp);
		user.setOtpGeneratedTime(LocalDateTime.now());
		this.userRepository.save(user);
		this.emailUtil.sendOtpEmail(user.getUsername(), resetPasswordDTO.getEmail(), otp);
	}

	@Override
	public String verifyOtp(verifyOtpDTO verifyOtpDTO) {
		User user = this.userRepository.findByEmail(verifyOtpDTO.getEmail())
				.orElseThrow(() -> new DataNotFoundException("Mã xác thực không chính xác"));
		String otp = user.getOtp();
		if (otp.equals(verifyOtpDTO.getOtp())
				&& Duration.between(LocalDateTime.now(), user.getOtpGeneratedTime()).toSeconds() < (15 * 60)) {
			return verifyOtpDTO.getOtp();
		}
		throw new InvalidDataException("Mã xác thực không chính xác");
	}

	@Override
	public void changePassword(ChangePasswordDTO changePasswordDTO) {
		User user = this.userRepository.findByOtp(changePasswordDTO.getOtp())
				.orElseThrow(() -> new DataNotFoundException("Mã xác thực không chính xác"));
		user.setPassword(changePasswordDTO.getNewPassword());
		this.userRepository.save(user);
	}

	@Override
	public void changePasswordWithCurrentPassword(String username, ChangePasswordDTO changePasswordDTO) {
		User user = this.userRepository.findById(username)
				.orElseThrow(() -> new DataNotFoundException("Tài khoản chưa tồn tại"));
		if (user.getPassword().equals(changePasswordDTO.getPassword())) {
			user.setPassword(changePasswordDTO.getNewPassword());
			this.userRepository.save(user);
			return;
		}
		throw new InvalidDataException("Mật khẩu hiện tại chưa chính xác");
	}

	@Override
	public List<AccessLogResponse> getAllAccessLog(String username) {
		List<AccessLogResponse> accessLogs = this.accessLogRepository.findAll().stream().map((log) -> {
			AccessLogResponse accessLogResponse;
			accessLogResponse = AccessLogResponse.builder()
					.id(log.getId())
					.username(log.getUsername())
					.httpMethod(log.getHttpMethod())
					.requestUrl(log.getRequestUrl())
					.statusCode(log.getStatusCode())
					.requestBody(this.toMap(new JSONObject(log.getRequestBody())))
					.responseBody(this.toMap(new JSONObject(log.getRequestBody())))
					.build();
			return accessLogResponse;
		}).toList();
		return accessLogs;
	}

	@Override
	public void createAccessLog(AccessLog accessLog) {
		this.accessLogRepository.save(accessLog);
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
				.administrationCode(user.getAdministration().getCode())
				.build();
		return userDTO;
	}

	// Chuyển từ userDTO sang user
	public User mapUserDTOToUser(UserDTO userDTO) {
		Administration administration = this.administrationRepository.findById(userDTO.getAdministrationCode())
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

	public User mapNewUserDTOToUser(NewUserDTO newUserDTO) {
		Administration administration = this.administrationRepository.findById(newUserDTO.getAdministrationCode())
				.orElseThrow(() -> new DataNotFoundException("Đơn vị hành chính trực thuộc không tồn tại"));
		User user = User.builder()
				.username(newUserDTO.getUsername())
				.password(newUserDTO.getPassword())
				.firstName(newUserDTO.getFirstName())
				.lastName(newUserDTO.getLastName())
				.email(newUserDTO.getEmail())
				.avatar(newUserDTO.getAvatar())
				.address(newUserDTO.getAddress())
				.birthDate(newUserDTO.getBirthDate())
				.isActive(newUserDTO.isActive())
				.role(newUserDTO.getRole())
				.administration(administration)
				.build();
		return user;
	}

	public static HashMap<String, Object> toMap(JSONObject jsonobj) throws JSONException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Iterator<String> keys = jsonobj.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			Object value = jsonobj.get(key);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}
}
