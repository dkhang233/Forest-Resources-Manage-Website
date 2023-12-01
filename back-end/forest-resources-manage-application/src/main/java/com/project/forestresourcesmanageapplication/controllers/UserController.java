package com.project.forestresourcesmanageapplication.controllers;

import java.io.FileInputStream;
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

import com.project.forestresourcesmanageapplication.dtos.UserDTO;
import com.project.forestresourcesmanageapplication.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

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

	@GetMapping("/{username}")
	public ResponseEntity<UserDTO> retrieveUserByUsername(@PathVariable String username) {
		UserDTO userDTO = this.userService.retrieveUserByUsername(username);
		return ResponseEntity.ok(userDTO);
	}

	@PostMapping("/{username}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable String username,
			@RequestPart(name = "body") UserDTO userDTO,
			@RequestParam(name = "avatar-file" , required = false) MultipartFile avatarFile) {
		userDTO = this.userService.updateUser(username, userDTO, avatarFile);
		return ResponseEntity.ok(userDTO);
	}

	@GetMapping(value = "/avatar/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getImageWithMediaType(@PathVariable String fileName) throws IOException {
		Path uploadPath = Path.of("uploads",fileName);
		InputStream in = Files.newInputStream(uploadPath);
		return IOUtils.toByteArray(in);
	}

	// @PostMapping("{username}/avatar")
	// public ResponseEntity<String> uploadAvatar(@RequestParam(name = "model")
	// String model,@RequestParam(name = "avatar") MultipartFile file){
	// return ResponseEntity.ok(model);
	// }

	// @PostMapping("/{username}")
	// public ResponseEntity<String> updateUser(@PathVariable String
	// username,@RequestBody UserDTO userDTO) {
	// userDTO = this.userService.updateUser(username,userDTO);
	// return ResponseEntity.badRequest().build();
	// }

}
