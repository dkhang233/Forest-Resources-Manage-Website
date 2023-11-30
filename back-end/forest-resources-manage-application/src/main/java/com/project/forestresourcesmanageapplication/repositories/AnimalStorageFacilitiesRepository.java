package com.project.forestresourcesmanageapplication.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;

@Repository
public interface AnimalStorageFacilitiesRepository extends JpaRepository<AnimalStorageFacilities,String>{
    @Query("SELECT a FROM AnimalStorageFacilities a ORDER BY a.code DESC")
        List<AnimalStorageFacilities> findAll();

    @Query("SELECT a FROM AnimalStorageFacilities a WHERE a.name = :name")
    Optional<AnimalStorageFacilities> findByName(String name);
} 
