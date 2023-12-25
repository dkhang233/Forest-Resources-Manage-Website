package com.project.forestresourcesmanageapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.forestresourcesmanageapplication.models.AccessLog;

public interface AccessLogRepository extends JpaRepository<AccessLog, Integer> {

}
