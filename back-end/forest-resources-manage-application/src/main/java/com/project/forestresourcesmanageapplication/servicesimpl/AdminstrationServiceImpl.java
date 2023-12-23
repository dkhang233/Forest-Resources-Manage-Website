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
import com.project.forestresourcesmanageapplication.services.AdminstrationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminstrationServiceImpl implements AdminstrationService {
	private final AdministrationRepository administrationRepository;
	private final AdministrativeLevelRepository administrativeLevelRepository;

	// ---------------------Administration------------------------

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
						"Không tìm thấy đơn vị hành chính với mã : " + code));

		// Kiểm tra cấp hành chính
		String administrativeLevelName = administrationDTO.getAdministrativeLevelName();
		if (!administration.getAdministrativeLevel().getName().equals(administrativeLevelName)) {
			AdministrativeLevel administrativeLevel = this.administrativeLevelRepository
					.findByName(administrationDTO.getAdministrativeLevelName())
					.orElseThrow(() -> new DataNotFoundException(
							"Không thể tìm thấy cấp hành chính: "
									+ administrationDTO.getAdministrativeLevelName()));
			if (administration.getAdministrativeLevel().getLevel() == 1
					&& administration.getAdministrativeLevel().getLevel() != administrativeLevel.getLevel()) {
				throw new InvalidDataException("Đơn vị hành chính cấp Tỉnh không thể đổi cấp");
			}
			if (administration.getAdministrativeLevel().getLevel() == 2
					&& administration.getAdministrativeLevel().getLevel() != administrativeLevel.getLevel()) {
				throw new InvalidDataException(
						"Đơn vị hành chính cấp Thành phố, Huyện chỉ có thể đổi cho nhau");
			}
			if (administration.getAdministrativeLevel().getLevel() == 3
					&& administration.getAdministrativeLevel().getLevel() != administrativeLevel.getLevel()) {
				throw new InvalidDataException(
						"Đơn vị hành chính cấp Phường, Thị trấn, Xã chỉ có thể đổi cho nhau");
			}
			administration.setAdministrativeLevel(administrativeLevel);
		}

		// Kiểm tra trực thuộc
		String subordinate = administrationDTO.getSubordinateCode() == null ? "" : administrationDTO.getSubordinateCode();
		if (administration.getSubordinate() != null && !administration.getSubordinate().getCode().equals(subordinate)) {
			if (administration.getAdministrativeLevel().getLevel() == 1) {
				throw new InvalidDataException(
						"Đơn vị hành chính cấp Tỉnh không thể trực thuộc đơn vị hành chính khác");
			}
			Administration subordinateAdministration = this.administrationRepository
					.findById(subordinate)
					.orElseThrow(() -> new DataNotFoundException(
							"Không tìm thấy đơn vị hành chính với mã : " + subordinate));
			if (administration.getAdministrativeLevel().getLevel() == 2) {
				if (subordinateAdministration.getAdministrativeLevel().getLevel() != 1) {
					throw new InvalidDataException(
							"Đơn vị hành chính cấp Thành phố, Huyện chỉ có thể trực thuộc đơn vị hành chính cấp tỉnh");
				}
				administration.setSubordinate(subordinateAdministration);
			}
			if (administration.getAdministrativeLevel().getLevel() == 3) {
				if (subordinateAdministration.getAdministrativeLevel().getLevel() != 3) {
					throw new InvalidDataException(
							"Đơn vị hành chính cấp Phường, Thị trấn, Xã chỉ có thể trực thuộc đơn vị hành chính cấp thành phố, huyện");
				}
				administration.setSubordinate(subordinateAdministration);
			}
		}
		administration.setName(administrationDTO.getName());
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
		Administration administration = this.administrationRepository.findByName(name)
				.orElseThrow(() -> new DataNotFoundException("Not found administration with name: " + name));
		return administration;
	}

	// ----------------------Administrative_Level-------------------------
	@Override
	public List<AdministrativeLevel> retrieveAllAdministrativeLevels() {
		return administrativeLevelRepository.findAll();
	}
}
