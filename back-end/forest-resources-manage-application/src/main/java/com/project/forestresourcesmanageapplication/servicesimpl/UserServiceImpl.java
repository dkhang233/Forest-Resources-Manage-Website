package com.project.forestresourcesmanageapplication.servicesimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.forestresourcesmanageapplication.models.User;
import com.project.forestresourcesmanageapplication.repositories.UserRepository;
import com.project.forestresourcesmanageapplication.services.AdminstrationService;
import com.project.forestresourcesmanageapplication.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final AdminstrationService adminstrationService;

    //----------------------User-----------------------
	@Override
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}
}
