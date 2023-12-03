package com.project.forestresourcesmanageapplication.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.project.forestresourcesmanageapplication.dtos.AnimalSpeciesDTO;
import com.project.forestresourcesmanageapplication.dtos.AnimalStorageFacilitiesDTO;
import com.project.forestresourcesmanageapplication.dtos.AsfAsRelationshipDTO;
import com.project.forestresourcesmanageapplication.models.AnimalSpecies;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;
import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;
import com.project.forestresourcesmanageapplication.models.Fluctuation;

public interface AnimalStorageFacilitiesService {

    //Animal-Storage-Facilities
    public List<AnimalStorageFacilities> getAllAnimalStorageFacilities() ;
    public AnimalStorageFacilities updateAnimalStorageFacilities(String code, AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO);
    public AnimalStorageFacilities getAnimalStorageFacilitiesByCode(String code);
    public AnimalStorageFacilities addAnimalStorageFacilities(AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO,String code);
    public void deleteAnimalStorageFacilitiesByCode(String code);

    //Animal-Species
    public List<AnimalSpecies> getAllAnimalSpecies();
    public AnimalSpecies updateAnimalSpecies(String name, AnimalSpeciesDTO animalSpeciesDTO);
    public AnimalSpecies getAnimalSpeciesByName(String name);
    public AnimalSpecies addAnimalSpecies(AnimalSpeciesDTO animalSpeciesDTO, String name);
    public void deleteAnimalSpeciesByName(String name);
    public List<AnimalSpecies> getAllAnimalSpeciesByFacilitiesCode(String code);

    //ASF-AS-Relationship
    public List<AsfAsRelationship> getAllAsfAsRelationships();
    public AsfAsRelationship updateAsfAsRelationship(int id , AsfAsRelationshipDTO asfAsRelationshipDTO);
    public AsfAsRelationship getAsfAsRelationshipById(int id);
    public AsfAsRelationship addAsfAsRelationship(AsfAsRelationshipDTO asfAsRelationshipDTO);
    public void deleteAsfAsRelationshipById(int id);

    //Fluctuation
    public List<Fluctuation> getAllFluctuations();
    public Fluctuation getFluctuationById(int id);
    
    //Thống kê
    public List<AsfAsRelationship> getAsfAsRelationshipWithTime(Date startDate,Date endDate);//lấy dữ liệu trong 1 khoảng thời gian
    public List<AsfAsRelationship> getAsfAsRelationshipInYear(int year);
    public List<AsfAsRelationship> getAsfAsRelationshipByFacilitiesInYear(String code, int year);
    public List<AsfAsRelationship> getAll();
}
