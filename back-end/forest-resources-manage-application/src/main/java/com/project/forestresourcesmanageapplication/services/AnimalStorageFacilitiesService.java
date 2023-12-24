package com.project.forestresourcesmanageapplication.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.AnimalSpeciesDTO;
import com.project.forestresourcesmanageapplication.dtos.AnimalStorageFacilitiesDTO;
import com.project.forestresourcesmanageapplication.dtos.AsfAsRelationshipDTO;
import com.project.forestresourcesmanageapplication.dtos.CoordinatesDTO;
import com.project.forestresourcesmanageapplication.models.AnimalSpecies;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;
import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;
import com.project.forestresourcesmanageapplication.models.Fluctuation;

import com.project.forestresourcesmanageapplication.responses.AnimalMonthQuantity;
import com.project.forestresourcesmanageapplication.responses.AnimalQuarterQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInMoth;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInQuarter;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInYear;
import com.project.forestresourcesmanageapplication.responses.FacilityQuantity;

public interface AnimalStorageFacilitiesService {

    // Animal-Storage-Facilities
    public List<AnimalStorageFacilities> getAllAnimalStorageFacilities();

    public AnimalStorageFacilities updateAnimalStorageFacilities(String code,
            AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO);

    public AnimalStorageFacilities getAnimalStorageFacilitiesByCode(String code);

    public AnimalStorageFacilities addAnimalStorageFacilities(AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO,
            String code);

    public void deleteAnimalStorageFacilitiesByCode(String code);

    // Tọa độ trên bản đồ
    public List<CoordinatesDTO> retrieveAllCoordinates();
    public CoordinatesDTO retrieveCoordinates(String code);
    public CoordinatesDTO updateCoordinates(CoordinatesDTO coordinatesDTO);
    public void deleteCoordinates(String code);

    // Animal-Species
    public List<AnimalSpecies> getAllAnimalSpecies();

    public AnimalSpecies updateAnimalSpecies(AnimalSpeciesDTO animalSpeciesDTO, MultipartFile imageFile);

    public AnimalSpecies getAnimalSpeciesByName(String name);

    public AnimalSpecies addAnimalSpecies(AnimalSpeciesDTO animalSpeciesDTO, MultipartFile imageFile);

    public void deleteAnimalSpeciesByName(String name);

    public List<AnimalSpecies> getAllAnimalSpeciesByFacilitiesCode(String code);

    // ASF-AS-Relationship
    public List<AsfAsRelationship> getAllAsfAsRelationships();

    public AsfAsRelationship updateAsfAsRelationship(int id, AsfAsRelationshipDTO asfAsRelationshipDTO);

    public AsfAsRelationship getAsfAsRelationshipById(int id);

    public AsfAsRelationship addAsfAsRelationship(AsfAsRelationshipDTO asfAsRelationshipDTO);

    public void deleteAsfAsRelationshipById(int id);

    // Fluctuation
    public List<Fluctuation> getAllFluctuations();

    public Fluctuation getFluctuationById(int id);

    // Thống kê
    public List<AsfAsRelationship> getAll();
    public List<AsfAsRelationship> getAsfAsRelationshipWithTime(Date startDate,Date endDate);//lấy dữ liệu trong 1 khoảng thời gian
    public List<AsfAsRelationship> getAsfAsRelationshipInYear(int year);//lấy dữ liệu trong 1 năm
    public List<AsfAsRelationship> getAsfAsRelationshipByFacilitiesInYear(String code, int year);//lấy dữ liệu của 1 cơ sở trong năm

    // public List<AnimalQuantity> getQuantityOfAllAnimalByFacilitiesCode(String code);
    public List<FacilitiesQuantity> getQuantityOfFacilitiesBeforeTime(LocalDate date);
    public List<FacilitiesQuantityInMoth> getQuantityOfFacilitiesWithTime(LocalDate beginDate,LocalDate endDate);
    public HashMap<String, List<FacilityQuantity>> getQuantityOfAllAnimalBeforeTime(LocalDate date);
    public List<AnimalMonthQuantity> getMonthQuantityOfFacilities(int year);
    public List<AnimalQuarterQuantity> getQuarterQuantityOfFacilities(int year);

    public List<FacilitiesQuantityInQuarter> getQuarterQuantityOfFacilitiesWithTime(LocalDate startDate, LocalDate endDate);
    public List<FacilitiesQuantityInYear> getYearQuantityOfFacilitiesWithTime(int startYear, int endYear);

}
