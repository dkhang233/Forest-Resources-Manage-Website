package com.project.forestresourcesmanageapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.forestresourcesmanageapplication.models.PlantSeed;

public interface PlantSeedRepository extends JpaRepository<PlantSeed,String>{
    
}
