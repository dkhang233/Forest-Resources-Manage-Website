package com.project.forestresourcesmanageapplication.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.ChangePasswordDTO;
import com.project.forestresourcesmanageapplication.dtos.LoginDTO;
import com.project.forestresourcesmanageapplication.dtos.NewUserDTO;
import com.project.forestresourcesmanageapplication.dtos.ResetPasswordDTO;
import com.project.forestresourcesmanageapplication.dtos.UserDTO;
import com.project.forestresourcesmanageapplication.dtos.verifyOtpDTO;
import com.project.forestresourcesmanageapplication.models.AccessLog;
import com.project.forestresourcesmanageapplication.responses.AccessLogResponse;

public interface UserService {
	List<UserDTO> retrieveAllUsers();

	UserDTO createUser(NewUserDTO newUserDTO, MultipartFile avatarFile);

	UserDTO retrieveUserByUsernameOrEmail(String usernameOrEmail);

	UserDTO updateUserByAdmin(String username, UserDTO userDTO, MultipartFile avatarFile);

	UserDTO updateUser(String username, UserDTO userDTO, MultipartFile avatarFile);

	String login(LoginDTO loginDTO);

	void resetPassword(ResetPasswordDTO resetPasswordDTO);

	String verifyOtp(verifyOtpDTO verifyOtpDTO);

	void changePassword(ChangePasswordDTO changePasswordDTO);

	void changePasswordWithCurrentPassword(String username, ChangePasswordDTO changePasswordDTO);

	List<AccessLogResponse> getAllAccessLog(String username);

	void createAccessLog(AccessLog accessLog);
}
