package com.project.forestresourcesmanageapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.forestresourcesmanageapplication.models.AccessHistory;


@Repository
public interface AccessHistoryRepository extends JpaRepository<AccessHistory, Integer>{

}
