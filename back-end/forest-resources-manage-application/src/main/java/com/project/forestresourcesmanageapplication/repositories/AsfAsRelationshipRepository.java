package com.project.forestresourcesmanageapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;

@Repository
public interface AsfAsRelationshipRepository extends JpaRepository<AsfAsRelationship,Integer> {
    
}
