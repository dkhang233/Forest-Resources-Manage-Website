package com.project.forestresourcesmanageapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.Fluctuation;

@Repository
public interface FluctuationRepository extends JpaRepository<Fluctuation,Integer>{
    
}
