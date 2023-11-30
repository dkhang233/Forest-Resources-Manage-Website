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

	// Truy suất toàn bộ đơn vị hành chính
	@Override
	public List<Administration> retrieveAllAdministrations() {
		return administrationRepository.findAll();
	}

	// Truy suất đơn vị hành chính theo mã
	@Override
	public Administration retrieveAdministrationByCode(String code) {
		Administration administration = this.administrationRepository.findById(code)
				.orElseThrow(() -> new DataNotFoundException("Not found administration with code = " + code));
		return administration;
	}

	// Truy suất toàn bộ đơn vị hành chính trực thuộc đơn vị có mã là tham số truyển
	// vào
	@Override
	public List<AdministrationHierarchyResponse> retrieveAllSubAdministrations(String code) {
		List<AdministrationHierarchyResponse> res = new ArrayList<>();
		Administration tmp = administrationRepository.findById(code)
				.orElseThrow(() -> new DataNotFoundException("Not found administration with code = " + code));
		res.add(new AdministrationHierarchyResponse(tmp));
		findChildren(res);
		return res;
	}

	// Hàm hỗ trợ cho hàm: retrieveAllSubAdministrations
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

	// Cập nhập đơn vị hành chính
	@Override
	public Administration updateAdministration(String code, AdministrationDTO administrationDTO) {

		Administration administration = this.administrationRepository
				.findById(code)
				.orElseThrow(() -> new DataNotFoundException(
						"Can not found administration with code = " + code));

		// Kiểm tra tên đơn vị hành chính
		if (!administration.getName().equals(administrationDTO.getName())) {
			Optional<Administration> tmp = this.administrationRepository
					.findByName(administrationDTO.getName());
			if (tmp.isPresent()) {
				throw new DataAlreadyExistsException(
						"Administration already exists with name = " + administrationDTO.getName());
			}
			administration.setName(administrationDTO.getName());
		}

		// Kiểm tra trực thuộc
		if (!administration.getSubordinate().getName().equals(administrationDTO.getSubordinateName())) {
			Administration subrbodinate = this.administrationRepository
					.findByName(administrationDTO.getSubordinateName())
					.orElseThrow(() -> new DataNotFoundException(
							"Can not found administration with name = " + administrationDTO.getSubordinateName()));
			administration.setSubordinate(subrbodinate);
		}

		// Kiểm tra cáp hành chính
		if (!administration.getAdministrativeLevel().getName().equals(administrationDTO.getAdministrativeLevelName())) {
			AdministrativeLevel administrativeLevel = this.administrativeLevelRepository
					.findByName(administrationDTO.getAdministrativeLevelName())
					.orElseThrow(() -> new DataNotFoundException(
							"Không thể tìm thấy cấp hành chính với tên = "
									+ administrationDTO.getAdministrativeLevelName()));
			if (!administrationRepository.findChildren(administration.getCode()).isEmpty()) {
				throw new InvalidDataException("Cấp hành chính không hợp lệ");
			}
			administration.setAdministrativeLevel(administrativeLevel);
		}

		administration = administrationRepository.save(administration);
		return administration;
	}

	// Xóa đơn vị hành chính
	@Override
	public void deleteByCode(String code) {
		this.administrationRepository.deleteById(code);
	}

	// Tìm kiếm đơn vị hành chính theo tên
	@Override
	public Administration retrieveAdministrationByName(String name) {
		Administration administration =  this.administrationRepository.findByName(name)
				.orElseThrow(() -> new DataNotFoundException("Not found administration with name: " + name));
		return administration;
	}

}
