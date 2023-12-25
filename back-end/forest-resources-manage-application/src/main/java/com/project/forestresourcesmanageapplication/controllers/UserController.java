package com.project.forestresourcesmanageapplication.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.ChangePasswordDTO;
import com.project.forestresourcesmanageapplication.dtos.LoginDTO;
import com.project.forestresourcesmanageapplication.dtos.NewUserDTO;
import com.project.forestresourcesmanageapplication.dtos.ResetPasswordDTO;
import com.project.forestresourcesmanageapplication.dtos.UserDTO;
import com.project.forestresourcesmanageapplication.dtos.verifyOtpDTO;
import com.project.forestresourcesmanageapplication.models.AccessLog;
import com.project.forestresourcesmanageapplication.responses.AccessLogResponse;
import com.project.forestresourcesmanageapplication.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping("")
	public ResponseEntity<List<UserDTO>> retrieveAllUsers() {
		List<UserDTO> users = this.userService.retrieveAllUsers();
		return ResponseEntity.ok(users);
	}

	@PostMapping("")
	public ResponseEntity<UserDTO> createUser(@RequestPart(name = "body") NewUserDTO newUserDTO,
			@RequestParam(name = "avatar-file", required = false) MultipartFile avatarFile) {
		UserDTO userDTO = this.userService.createUser(newUserDTO, avatarFile);
		return ResponseEntity.ok(userDTO);
	}

	@GetMapping("/{usernameOrEmail}")
	public ResponseEntity<UserDTO> retrieveUserByUsername(
			@PathVariable(name = "usernameOrEmail") String usernameOrEmail) {
		UserDTO userDTO = this.userService.retrieveUserByUsernameOrEmail(usernameOrEmail);
		return ResponseEntity.ok(userDTO);
	}

	@PostMapping("admin/{username}")
	public ResponseEntity<UserDTO> updateUserByAdmin(@PathVariable String username,
			@RequestPart(name = "body") UserDTO userDTO,
			@RequestParam(name = "avatar-file", required = false) MultipartFile avatarFile) {
		userDTO = this.userService.updateUserByAdmin(username, userDTO, avatarFile);
		return ResponseEntity.ok(userDTO);
	}

	@PostMapping("/{username}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable String username,
			@RequestPart(name = "body") UserDTO userDTO,
			@RequestParam(name = "avatar-file", required = false) MultipartFile avatarFile) {
		userDTO = this.userService.updateUser(username, userDTO, avatarFile);
		return ResponseEntity.ok(userDTO);
	}

	@GetMapping(value = "/avatar/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getImageWithMediaType(@PathVariable String fileName) throws IOException {
		Path uploadPath = Path.of("uploads", fileName);
		InputStream in = Files.newInputStream(uploadPath);
		return IOUtils.toByteArray(in);
	}

	@PostMapping("/login")
	public ResponseEntity<String> postMethodName(@RequestBody LoginDTO loginDTO) {
		String username = this.userService.login(loginDTO);
		return ResponseEntity.ok(username);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
		this.userService.resetPassword(resetPasswordDTO);
		return ResponseEntity.ok("");
	}

	@PutMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
		this.userService.changePassword(changePasswordDTO);
		return ResponseEntity.ok("Mật khẩu đã được thay đổi");
	}

	@PutMapping("/change-password/{username}")
	public ResponseEntity<?> changePasswordWithCurrentPassword(@PathVariable String username,
			@RequestBody ChangePasswordDTO changePasswordDTO) {
		this.userService.changePasswordWithCurrentPassword(username, changePasswordDTO);
		return ResponseEntity.ok("Mật khẩu đã được thay đổi");
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(@RequestBody verifyOtpDTO verifyOtpDTO) {
		String otp = this.userService.verifyOtp(verifyOtpDTO);
		return ResponseEntity.ok(otp);
	}

	@GetMapping("/access-log/{username}")
	public ResponseEntity<?> getMethodName(@PathVariable String username) {
		return ResponseEntity.ok(this.userService.getAllAccessLog(username));
	}

}
