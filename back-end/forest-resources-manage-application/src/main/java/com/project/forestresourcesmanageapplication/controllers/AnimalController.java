package com.project.forestresourcesmanageapplication.controllers;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.forestresourcesmanageapplication.dtos.AnimalSpeciesDTO;
import com.project.forestresourcesmanageapplication.dtos.AnimalStorageFacilitiesDTO;
import com.project.forestresourcesmanageapplication.models.AnimalSpecies;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;
import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;
import com.project.forestresourcesmanageapplication.models.Fluctuation;
import com.project.forestresourcesmanageapplication.services.AnimalStorageFacilitiesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/animal-storage-facilities")
@RequiredArgsConstructor
public class AnimalController {
    private final AnimalStorageFacilitiesService animalStorageFacilitiesService;

    //------------------------CƠ SỞ LƯU TRỮ ĐỘNG VẬT-----------------------------
    @GetMapping("")
    public ResponseEntity<List<AnimalStorageFacilities>> getAllAnimalStorageFacilities(){
        List<AnimalStorageFacilities> listAnimalStorageFacilities = this.animalStorageFacilitiesService.getAllAnimalStorageFacilities();
        return ResponseEntity.ok(listAnimalStorageFacilities);
    }

    @GetMapping("/{code}")
    public ResponseEntity<AnimalStorageFacilities> getAnimalStorageFacilitiesByCode(@PathVariable String code){
        AnimalStorageFacilities animalStorageFacilities = this.animalStorageFacilitiesService.getAnimalStorageFacilitiesByCode(code);
        return ResponseEntity.ok(animalStorageFacilities);
    }

    @PostMapping("/{code}")
    public ResponseEntity<AnimalStorageFacilities> addAnimalStorageFacilities(@PathVariable String code, @RequestBody AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO){
        AnimalStorageFacilities animalStorageFacilities = this.animalStorageFacilitiesService.addAnimalStorageFacilities(animalStorageFacilitiesDTO,code);
        return ResponseEntity.status(HttpStatus.CREATED).body(animalStorageFacilities);
    }

    @PutMapping("/{code}")
    public ResponseEntity<AnimalStorageFacilities> updateAnimalStorageFacilities(@PathVariable String code, @RequestBody AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO){
        AnimalStorageFacilities animalStorageFacilities = this.animalStorageFacilitiesService.updateAnimalStorageFacilities(code, animalStorageFacilitiesDTO);
        return ResponseEntity.ok(animalStorageFacilities);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<Void> deleteAnimalStorageFacilities(@PathVariable String code){
        this.animalStorageFacilitiesService.deleteAnimalStorageFacilitiesByCode(code);
        return ResponseEntity.ok().build();
    }

//---------------------------LOÀI ĐỘNG VẬT--------------------------
    
    @GetMapping("/species")
    public ResponseEntity<List<AnimalSpecies>> getAllAnimalSpecies(){
        List<AnimalSpecies> listAnimalSpecies = this.animalStorageFacilitiesService.getAllAnimalSpecies();
        return ResponseEntity.ok(listAnimalSpecies);
    }

    @GetMapping("/species/{name}")
    public ResponseEntity<AnimalSpecies> getAnimalSpeciesByName(@PathVariable String name){
        AnimalSpecies animalSpecies = this.animalStorageFacilitiesService.getAnimalSpeciesByName(name);
        return ResponseEntity.ok(animalSpecies);
    }

    @PostMapping("/species/{name}")
    public ResponseEntity<AnimalSpecies> addAnimalSpecies(@PathVariable String name, @RequestBody AnimalSpeciesDTO animalSpeciesDTO){
        AnimalSpecies animalSpecies = this.animalStorageFacilitiesService.addAnimalSpecies(animalSpeciesDTO,name);
        return ResponseEntity.status(HttpStatus.CREATED).body(animalSpecies);
    }

    @PutMapping("/species/{name}")
    public ResponseEntity<AnimalSpecies> updateAnimalSpecies(@PathVariable String name, @RequestBody AnimalSpeciesDTO animalSpeciesDTO){
        AnimalSpecies animalSpecies = this.animalStorageFacilitiesService.updateAnimalSpecies(name, animalSpeciesDTO);
        return ResponseEntity.ok(animalSpecies);
    }

    @DeleteMapping("/species/{name}")
    public ResponseEntity<Void> deleteAnimalSpecies(@PathVariable String name){
        this.animalStorageFacilitiesService.deleteAnimalSpeciesByName(name);
        return ResponseEntity.ok().build();
    }

//---------------------------LOẠI BIẾN ĐỘNG--------------------------

    @GetMapping("/fluctuation")
    public ResponseEntity<List<Fluctuation>> getAllFluctuation(){
        List<Fluctuation> fluctuations = this.animalStorageFacilitiesService.getAllFluctuations();
        return ResponseEntity.ok(fluctuations);
    }

    @GetMapping("/fluctuation/{id}")
    public ResponseEntity<Fluctuation> getFluctuationById(@PathVariable int id){
        Fluctuation fluctuation = this.animalStorageFacilitiesService.getFluctuationById(id);
        return ResponseEntity.ok(fluctuation);
    }
    
//--------------------------THỐNG KÊ DỮ LIỆU----------------------
    //lấy tất cả dữ liệu theo năm
    // @GetMapping("/statistical")
    // public ResponseEntity<List<AsfAsRelationship>> getAsfAsRelationship(){
    //     String str = "2013-05-05";
    //     Date date = Date.valueOf(str);
    //     List<AsfAsRelationship> asRelationships = this.animalStorageFacilitiesService.getAsfAsRelationshipWithYear(date);
    //     return ResponseEntity.ok(asRelationships);
    // }
}
