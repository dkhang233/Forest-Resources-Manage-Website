package com.project.forestresourcesmanageapplication.repositories;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;

@Repository
public interface AnimalStorageFacilitiesRepository extends JpaRepository<AnimalStorageFacilities,String>{
    @Query("SELECT a FROM AnimalStorageFacilities a ORDER BY a.code")
    List<AnimalStorageFacilities> findAll();

    @Query("SELECT a FROM AnimalStorageFacilities a WHERE a.name = :name")
    Optional<AnimalStorageFacilities> findByName(String name);

    @Query("SELECT a.name FROM AnimalStorageFacilities a WHERE a.date <= :date ORDER BY a.name")
    List<String> findAllFacilitiesNameBeforeTime(Date date);
} 
