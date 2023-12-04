package com.project.forestresourcesmanageapplication.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;

@Repository
public interface AsfAsRelationshipRepository extends JpaRepository<AsfAsRelationship,Integer> {
    // @Query("SELECT a FROM AsfAsRelationship a WHERE a.dateSet = :date")
    // List<AsfAsRelationship> selectAsfAsRelationshipWithYear(@Param("date") Date date);
}
