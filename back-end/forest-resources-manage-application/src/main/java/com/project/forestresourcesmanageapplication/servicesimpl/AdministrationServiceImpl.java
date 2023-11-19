package com.project.forestresourcesmanageapplication.servicesimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.forestresourcesmanageapplication.dtos.AdministrationDTO;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataAlreadyExistsException;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataNotFoundException;
import com.project.forestresourcesmanageapplication.exceptionhandling.InvalidDataException;
import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.models.AdministrativeLevel;
import com.project.forestresourcesmanageapplication.repositories.AdministrationRepository;
import com.project.forestresourcesmanageapplication.repositories.AdministrativeLevelRepository;
import com.project.forestresourcesmanageapplication.responses.AdministrationHierarchyResponse;
import com.project.forestresourcesmanageapplication.services.AdministrationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdministrationServiceImpl implements AdministrationService {
	private final AdministrationRepository administrationRepository;
	private final AdministrativeLevelRepository administrativeLevelRepository;

	@Override
	public List<Administration> retrieveAllAdministrations() {
		return administrationRepository.findAll();
	}

	@Override
	public List<AdministrationHierarchyResponse> retrieveAllSubAdministrations(String code) {
		List<AdministrationHierarchyResponse> res = new ArrayList<>();
		Administration tmp = administrationRepository.findById(code)
				.orElseThrow(() -> new RuntimeException("Not found administration with code = " + code));
		res.add(new AdministrationHierarchyResponse(tmp));
		findChildren(res);
		return res;
	}

	public void findChildren(List<AdministrationHierarchyResponse> data) {
		data.stream().forEach((el) -> {
			List<Administration> tmp1 = administrationRepository.findChildren(el.getCode());
			if (tmp1.isEmpty()) {
				return;
			}
			List<AdministrationHierarchyResponse> tmp2 = tmp1.stream()
					.map((el2) -> new AdministrationHierarchyResponse(el2)).toList();
			findChildren(tmp2);
			el.setChildren(tmp2);
		});
	}

	@Override
	public Administration retrieveAdministrationByCode(String code) {
		Administration administration = this.administrationRepository.findById(code)
				.orElseThrow(() -> new RuntimeException("Not found administration with code = " + code));
		return administration;
	}

	@Override
	public Administration updateAdministration(AdministrationDTO administrationDTO) {
		Administration administration = this.administrationRepository
				.findById(administrationDTO.getCode())
				.orElseThrow(() -> new DataNotFoundException(
						"Can not found administration with code = " + administrationDTO.getCode()));

		if (!administration.getCode().equals(administrationDTO.getCode())) {
			Optional<Administration> tmp = this.administrationRepository
					.findById(administrationDTO.getCode());
			if (tmp.isPresent()) {
				throw new DataAlreadyExistsException(
						"Administration already exists with code = " + administrationDTO.getCode());
			}
			administration.setCode(administrationDTO.getCode());
		}

		if (!administration.getName().equals(administrationDTO.getName())) {
			Optional<Administration> tmp = this.administrationRepository
					.findByName(administrationDTO.getName());
			if (tmp.isPresent()) {
				throw new DataAlreadyExistsException(
						"Administration already exists with name = " + administrationDTO.getName());
			}
			administration.setName(administrationDTO.getName());
		}

		if (!administration.getSubordinate().getName().equals(administrationDTO.getSubordinateName())) {
			Administration subrbodinate = this.administrationRepository
					.findByName(administrationDTO.getSubordinateName())
					.orElseThrow(() -> new DataNotFoundException(
							"Can not found administration with name = " + administrationDTO.getSubordinateName()));
			administration.setSubordinate(subrbodinate);
		}

		if (!administration.getAdministrativeLevel().getName().equals(administrationDTO.getAdministrativeLevelName())) {
			AdministrativeLevel administrativeLevel = this.administrativeLevelRepository
					.findByName(administrationDTO.getAdministrativeLevelName())
					.orElseThrow(() -> new DataNotFoundException(
							"Không thể tìm thấy cấp hành chính với tên = " + administrationDTO.getAdministrativeLevelName()));
			if(!administrationRepository.findChildren(administration.getCode()).isEmpty()){
				throw new InvalidDataException("Cấp hành chính không hợp lệ");
			}
			administration.setAdministrativeLevel(administrativeLevel);
		}

		administration = administrationRepository.save(administration);
		return administration;
	}

}
