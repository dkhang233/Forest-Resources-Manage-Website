package com.project.forestresourcesmanageapplication.servicesimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.forestresourcesmanageapplication.models.Administrative;
import com.project.forestresourcesmanageapplication.repositories.AdministrativeRepository;
import com.project.forestresourcesmanageapplication.services.AdministrativeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdministrativeServiceImpl implements AdministrativeService{
	private final AdministrativeRepository administrativeRepository;
	
	@Override
	public List<Administrative> retrieveAllAdministratives() {
		return administrativeRepository.findAll();
	}

}
