package com.project.forestresourcesmanageapplication.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.AnimalSpeciesDTO;
import com.project.forestresourcesmanageapplication.dtos.AnimalStorageFacilitiesDTO;
import com.project.forestresourcesmanageapplication.dtos.AsfAsRelationshipDTO;

import com.project.forestresourcesmanageapplication.models.AnimalSpecies;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;
import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;
import com.project.forestresourcesmanageapplication.models.Fluctuation;
import com.project.forestresourcesmanageapplication.responses.AnimalsQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInMoth;
import com.project.forestresourcesmanageapplication.responses.MonthQuantity;
import com.project.forestresourcesmanageapplication.responses.QuarterQuantity;
import com.project.forestresourcesmanageapplication.responses.YearQuantity;

public interface AnimalStorageFacilitiesService {

    // Animal-Storage-Facilities
    public List<AnimalStorageFacilities> getAllAnimalStorageFacilities();

    public AnimalStorageFacilities updateAnimalStorageFacilities(String code,
            AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO);

    public AnimalStorageFacilities getAnimalStorageFacilitiesByCode(String code);

    public AnimalStorageFacilities addAnimalStorageFacilities(AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO,
            String code);

    public void deleteAnimalStorageFacilitiesByCode(String code);

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

    public List<AsfAsRelationship> getAsfAsRelationshipWithTime(Date startDate, Date endDate);// lấy dữ liệu trong 1
                                                                                              // khoảng thời gian

    public List<AsfAsRelationship> getAsfAsRelationshipInYear(int year);// lấy dữ liệu trong 1 năm

    public List<AsfAsRelationship> getAsfAsRelationshipByFacilitiesInYear(String code, int year);// lấy dữ liệu của 1 cơ
                                                                                                 // sở trong năm
    // public Long getQuantityAnimalOfFacilitiesCode(String code, String name);//lấy
    // số lượng của 1 con vật trong 1 cơ sở tính đến thời điểm hiện tại

    // public List<AnimalQuantity> getQuantityOfAllAnimalByFacilitiesCode(String
    // code);
    public List<MonthQuantity> getQuantityAnimalWithMonthOfYear(String code, String name, int year);

    public List<QuarterQuantity> getQuantityAnimalWithQuarterOfYear(String code, String name, int year);

    public List<YearQuantity> getQuantityAnimalWithYear(String code, String name);

    public List<FacilitiesQuantity> getQuantityOfFacilitiesBeforeTime(LocalDate date);

    public List<FacilitiesQuantityInMoth> getQuantityOfFacilitiesWithTime(LocalDate endDate);

    public List<AnimalsQuantity> getQuantityOfAllAnimalBeforeTime(Date date);

}
