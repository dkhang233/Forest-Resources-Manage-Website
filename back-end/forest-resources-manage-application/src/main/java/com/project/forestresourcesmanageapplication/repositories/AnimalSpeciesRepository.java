package com.project.forestresourcesmanageapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.AnimalSpecies;

@Repository
public interface AnimalSpeciesRepository extends JpaRepository<AnimalSpecies,String>{
    
}
