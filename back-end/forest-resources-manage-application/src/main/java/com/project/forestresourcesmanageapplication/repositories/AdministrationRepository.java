package com.project.forestresourcesmanageapplication.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.forestresourcesmanageapplication.models.Administration;

public interface AdministrationRepository extends JpaRepository<Administration, String>{
	
	@Query("SELECT a FROM Administration a ORDER BY a.code DESC")
	List<Administration> findAll();

	@Query("SELECT a FROM Administration a JOIN a.administrativeLevel b WHERE a.administrativeLevel.id = :level")
	List<Administration> findByAdministrativeLevel(int level);

	@Query("SELECT a FROM Administration a WHERE a.subordinate = (SELECT a FROM Administration a WHERE a.code = :code)")
	List<Administration> findChildren(String code);

	@Query("SELECT a FROM Administration a WHERE a.name = :name")
    Optional<Administration> findByName(String name);

}
