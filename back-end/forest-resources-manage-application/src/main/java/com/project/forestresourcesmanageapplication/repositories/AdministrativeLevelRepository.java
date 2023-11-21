package com.project.forestresourcesmanageapplication.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.forestresourcesmanageapplication.models.AdministrativeLevel;

public interface AdministrativeLevelRepository extends JpaRepository<AdministrativeLevel, Integer>{

    @Query("SELECT a from AdministrativeLevel a WHERE a.name = :name")
    Optional<AdministrativeLevel> findByName(String name);

}
