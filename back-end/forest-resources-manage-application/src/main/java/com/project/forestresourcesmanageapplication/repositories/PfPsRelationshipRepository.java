package com.project.forestresourcesmanageapplication.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.forestresourcesmanageapplication.models.PfPsRelationship;
import com.project.forestresourcesmanageapplication.models.PlantSeed; 

public interface PfPsRelationshipRepository extends JpaRepository<PfPsRelationship,Integer>{
    @Query("SELECT p.plantSeed FROM PfPsRelationship p WHERE p.plantFacilities.code = :code")
    Optional<List<PlantSeed>> selectAllPlantSeedByFacilitiesCode(String code);
}
