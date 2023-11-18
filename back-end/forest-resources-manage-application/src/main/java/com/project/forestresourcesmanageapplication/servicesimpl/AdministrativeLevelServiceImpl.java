package com.project.forestresourcesmanageapplication.servicesimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.forestresourcesmanageapplication.models.AdministrativeLevel;
import com.project.forestresourcesmanageapplication.repositories.AdministrativeLevelRepository;
import com.project.forestresourcesmanageapplication.services.AdministrativeLevelService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdministrativeLevelServiceImpl implements AdministrativeLevelService{
	private final AdministrativeLevelRepository administrativeLevelRepository;
	
	@Override
	public List<AdministrativeLevel> retrieveAllAdministrativeLevels() {
		return administrativeLevelRepository.findAll();
	}

}
