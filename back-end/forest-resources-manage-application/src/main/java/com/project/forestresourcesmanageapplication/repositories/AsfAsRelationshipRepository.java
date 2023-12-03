package com.project.forestresourcesmanageapplication.repositories;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.AnimalSpecies;
import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;

@Repository
public interface AsfAsRelationshipRepository extends JpaRepository<AsfAsRelationship,Integer> {
    @Query("SELECT a FROM AsfAsRelationship a WHERE a.date BETWEEN :startDate AND :endDate")
    Optional<List<AsfAsRelationship>> selectAsfAsRelationshipWithTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT a FROM AsfAsRelationship a WHERE a.animalStorageFacilities.code = :code AND a.date BETWEEN :startDate AND :endDate")
    Optional<List<AsfAsRelationship>> selectAsfAsRelationshipByFacilitiesInYear(@Param("code") String code, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT a.animalSpecies FROM AsfAsRelationship a WHERE a.animalStorageFacilities.code = :code")
    Optional<List<AnimalSpecies>> selectAllAnimalSpeciesByFacilitiesCode(String code);
}
