package com.project.forestresourcesmanageapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.OperationForm;

@Repository
public interface OperationFormRepository extends JpaRepository<OperationForm,String>{
    
}
