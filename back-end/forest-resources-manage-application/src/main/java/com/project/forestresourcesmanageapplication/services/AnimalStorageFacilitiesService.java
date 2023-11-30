package com.project.forestresourcesmanageapplication.services;

import java.util.List;

import com.project.forestresourcesmanageapplication.dtos.AnimalStorageFacilitiesDTO;
import com.project.forestresourcesmanageapplication.models.AnimalSpecies;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;
import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;
import com.project.forestresourcesmanageapplication.models.Fluctuation;

public interface AnimalStorageFacilitiesService {

    //Animal-Storage-Facilities
    public List<AnimalStorageFacilities> getAllAnimalStorageFacilities() ;
    public AnimalStorageFacilities updateAnimalStorageFacilities(String code,AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO);
    public AnimalStorageFacilities getAnimalStorageFacilitiesByCode(String code);
    public AnimalStorageFacilities addAnimalStorageFacilities(AnimalStorageFacilities animalStorageFacilities);
    public void deleteAnimalStorageFacilitiesByCode(String code);

    //Animal-Species
    public List<AnimalSpecies> getAllAnimalSpecies();
    public AnimalSpecies updateAnimalSpecies(AnimalSpecies animalSpecies);
    public AnimalSpecies getAnimalSpeciesByName(String name);
    public AnimalSpecies addAnimalSpecies(AnimalSpecies animalSpecies);
    public void deleteAnimalSpeciesByName(String name);

    //ASF-AS-Relationship
    public List<AsfAsRelationship> getAllAsfAsRelationships();
    public AsfAsRelationship updateAsfAsRelationship(AsfAsRelationship asfAsRelationship);
    public AsfAsRelationship getAsfAsRelationshipById(int id);
    public AsfAsRelationship addAsfAsRelationship(AsfAsRelationship asfAsRelationship);
    public void deleteAsfAsRelationshipById(int id);

    //Fluctuation
    public List<Fluctuation> getAllFluctuations();
    public Fluctuation updateFluctuation(Fluctuation fluctuation);
    public Fluctuation getFluctuationById(int id);
    public Fluctuation addFluctuation(Fluctuation fluctuation);
    public void deleteFluctuationById(int id); 
}
