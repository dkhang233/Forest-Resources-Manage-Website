package com.project.forestresourcesmanageapplication.servicesimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.repositories.AdministrationRepository;
import com.project.forestresourcesmanageapplication.responses.AdministrationHierarchyResponse;
import com.project.forestresourcesmanageapplication.services.AdministrationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdministrationServiceImpl implements AdministrationService{
	private final AdministrationRepository administrationRepository;
	
	@Override
	public List<Administration> retrieveAllAdministrations() {
		return administrationRepository.findAll();
	}

	@Override
	public List<AdministrationHierarchyResponse> retrieveAllSubAdministrations(String code) {
		List<AdministrationHierarchyResponse> res = new ArrayList<>();
		Administration tmp =  administrationRepository.findById(code)
														.orElseThrow(() -> new RuntimeException("Not found administration with code = " + code));
		res.add(new AdministrationHierarchyResponse(tmp));
		findChildren(res);
		return res;
	}


	public void findChildren(List<AdministrationHierarchyResponse> data ) {
		data.stream().forEach( (el) -> {
			List<Administration> tmp1 = administrationRepository.findChildren(el.getCode());
			if(tmp1.isEmpty()) {
				return;
			}
			List<AdministrationHierarchyResponse> tmp2 = tmp1.stream().map((el2) -> new AdministrationHierarchyResponse(el2)).toList();
			findChildren(tmp2);
			el.setChildren(tmp2);
		});
	}
}
