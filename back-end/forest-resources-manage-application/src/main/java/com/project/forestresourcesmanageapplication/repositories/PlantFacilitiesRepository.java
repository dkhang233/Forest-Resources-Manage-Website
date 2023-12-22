package com.project.forestresourcesmanageapplication.repositories;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.forestresourcesmanageapplication.models.PlantFacilities;

public interface PlantFacilitiesRepository extends JpaRepository<PlantFacilities,String>{
    @Query("SELECT p FROM PlantFacilities p ORDER BY p.code DESC")
    List<PlantFacilities> findAll();

    @Query("SELECT p FROM PlantFacilities p WHERE p.name = :name")
    Optional<PlantFacilities> findByName(String name);

    @Query("SELECT p.name FROM PlantFacilities p WHERE p.date <= :date ORDER BY p.name")
    List<String> findAllFacilitiesNameBeforeTime(Date date);
}
