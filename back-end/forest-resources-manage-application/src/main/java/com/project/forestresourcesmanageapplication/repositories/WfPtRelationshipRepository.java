package com.project.forestresourcesmanageapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.WfPtRelationship;

@Repository
public interface WfPtRelationshipRepository extends JpaRepository<WfPtRelationship,Integer>{
    
}
