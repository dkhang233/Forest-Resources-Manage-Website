package com.project.forestresourcesmanageapplication.controllers;

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

import com.project.forestresourcesmanageapplication.dtos.AnimalStorageFacilitiesDTO;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;
import com.project.forestresourcesmanageapplication.services.AnimalStorageFacilitiesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/animal-storage-facilities")
@RequiredArgsConstructor
public class AnimalController {
    private final AnimalStorageFacilitiesService animalStorageFacilitiesService;

    //------------------------CƠ SỞ LƯU TRỮ ĐỘNG VẬT
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
}
