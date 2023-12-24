package com.project.forestresourcesmanageapplication.repositories;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.WoodFacilities;

@Repository
public interface WoodFacilitiesRepository extends JpaRepository<WoodFacilities,String>{
    @Query("SELECT w FROM WoodFacilities w ORDER BY w.code")
    List<WoodFacilities> findAll();

    @Query("SELECT w FROM WoodFacilities w WHERE w.name = :name")
    Optional<WoodFacilities> findByName(String name);

    @Query("SELECT w.name FROM WoodFacilities w WHERE w.date <= :date ORDER BY w.name")
    List<String> findAllFacilitiesNameBeforeTime(Date date);
}
