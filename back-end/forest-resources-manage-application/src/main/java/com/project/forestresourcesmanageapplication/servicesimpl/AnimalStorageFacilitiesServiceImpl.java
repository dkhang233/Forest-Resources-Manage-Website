package com.project.forestresourcesmanageapplication.servicesimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.forestresourcesmanageapplication.dtos.AnimalStorageFacilitiesDTO;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataAlreadyExistsException;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataNotFoundException;
import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.models.AnimalSpecies;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;
import com.project.forestresourcesmanageapplication.models.AsfAsRelationship;
import com.project.forestresourcesmanageapplication.models.Fluctuation;
import com.project.forestresourcesmanageapplication.repositories.AnimalSpeciesRepository;
import com.project.forestresourcesmanageapplication.repositories.AnimalStorageFacilitiesRepository;
import com.project.forestresourcesmanageapplication.repositories.AsfAsRelationshipRepository;
import com.project.forestresourcesmanageapplication.repositories.FluctuationRepository;
import com.project.forestresourcesmanageapplication.services.AdminstrationService;
import com.project.forestresourcesmanageapplication.services.AnimalStorageFacilitiesService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalStorageFacilitiesServiceImpl implements AnimalStorageFacilitiesService{
    private final AnimalStorageFacilitiesRepository animalStorageFacilitiesRepository;
    private final AnimalSpeciesRepository animalSpeciesRepository;
    private final AsfAsRelationshipRepository asfAsRelationshipRepository;
    private final FluctuationRepository fluctuationRepository;
    private final AdminstrationService adminstrationService;

    //-----------------------Animal Storage Facilities
    @Override
    public List<AnimalStorageFacilities> getAllAnimalStorageFacilities() {
        return animalStorageFacilitiesRepository.findAll();
    }

    @Override
    @Transactional
    public AnimalStorageFacilities updateAnimalStorageFacilities(String code,AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO) {
        AnimalStorageFacilities animalStorageFacilitiesExisting = this.getAnimalStorageFacilitiesByCode(code);

        //kiểm tra tên
        if(!animalStorageFacilitiesExisting.getName().equals(animalStorageFacilitiesDTO.getName())){
            Optional<AnimalStorageFacilities> tmp = this.animalStorageFacilitiesRepository.findByName(animalStorageFacilitiesDTO.getName());
            if(tmp.isPresent()){
                throw new DataAlreadyExistsException("Đã có cơ sở lưu trữ động vật với tên = "+animalStorageFacilitiesDTO.getName());
            }
            animalStorageFacilitiesExisting.setName(animalStorageFacilitiesDTO.getName());
        }
        
        //kiểm tra đơn vị hành chính
        if(!animalStorageFacilitiesExisting.getAdministration().getCode().equals(animalStorageFacilitiesDTO.getAdminstrationCode())){
            try {
                 Administration administration = this.adminstrationService.retrieveAdministrationByCode(animalStorageFacilitiesDTO.getAdminstrationCode());
                 animalStorageFacilitiesExisting.setAdministration(administration);
            }catch(Exception exception){
                throw new DataNotFoundException("Không tìm thấy cơ sở hành chính với code = "+ animalStorageFacilitiesDTO.getAdminstrationCode());
            }
        }

        animalStorageFacilitiesExisting.setCapacity(animalStorageFacilitiesDTO.getCapacity());
        animalStorageFacilitiesExisting.setDate(animalStorageFacilitiesDTO.getDate());
        animalStorageFacilitiesExisting.setDetail(animalStorageFacilitiesDTO.getDetail());

        return animalStorageFacilitiesRepository.save(animalStorageFacilitiesExisting);
    }

    @Override
    public AnimalStorageFacilities getAnimalStorageFacilitiesByCode(String code) {
        return animalStorageFacilitiesRepository.findById(code)
        .orElseThrow( () -> new DataNotFoundException("Không tìm thấy cơ sở lưu trữ động vật với code = "+code));
    }

    @Override
    public AnimalStorageFacilities addAnimalStorageFacilities(AnimalStorageFacilitiesDTO animalStorageFacilitiesDTO) {
        AnimalStorageFacilities animalStorageFacilities = new AnimalStorageFacilities();
        
        //kiểm tra tên
        Optional<AnimalStorageFacilities> tmp = this.animalStorageFacilitiesRepository.findByName(animalStorageFacilitiesDTO.getName());
            if(tmp.isPresent()){
                throw new DataAlreadyExistsException("Đã có cơ sở lưu trữ động vật với tên = "+animalStorageFacilitiesDTO.getName());
            }
            animalStorageFacilities.setName(animalStorageFacilitiesDTO.getName());
        //kiểm tra đơn vị hành chính
        try {
                 Administration administration = this.adminstrationService.retrieveAdministrationByCode(animalStorageFacilitiesDTO.getAdminstrationCode());
                 animalStorageFacilities.setAdministration(administration);
            }catch(Exception exception){
                throw new DataNotFoundException("Không tìm thấy cơ sở hành chính với code = "+ animalStorageFacilitiesDTO.getAdminstrationCode());
            }
        animalStorageFacilities.setCapacity(animalStorageFacilitiesDTO.getCapacity());
        animalStorageFacilities.setDate(animalStorageFacilitiesDTO.getDate());
        animalStorageFacilities.setDetail(animalStorageFacilitiesDTO.getDetail());

        return animalStorageFacilitiesRepository.save(animalStorageFacilities);
    }

    @Override
    public void deleteAnimalStorageFacilitiesByCode(String code) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAnimalStorageFacilitiesByCode'");
    }

    @Override
    public List<AnimalSpecies> getAllAnimalSpecies() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllAnimalSpecies'");
    }

    @Override
    public AnimalSpecies updateAnimalSpecies(AnimalSpecies animalSpecies) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAnimalSpecies'");
    }

    @Override
    public AnimalSpecies getAnimalSpeciesByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAnimalSpeciesByName'");
    }

    @Override
    public AnimalSpecies addAnimalSpecies(AnimalSpecies animalSpecies) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAnimalSpecies'");
    }

    @Override
    public void deleteAnimalSpeciesByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAnimalSpeciesByName'");
    }

    @Override
    public List<AsfAsRelationship> getAllAsfAsRelationships() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllAsfAsRelationships'");
    }

    @Override
    public AsfAsRelationship updateAsfAsRelationship(AsfAsRelationship asfAsRelationship) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAsfAsRelationship'");
    }

    @Override
    public AsfAsRelationship getAsfAsRelationshipById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAsfAsRelationshipById'");
    }

    @Override
    public AsfAsRelationship addAsfAsRelationship(AsfAsRelationship asfAsRelationship) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAsfAsRelationship'");
    }

    @Override
    public void deleteAsfAsRelationshipById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAsfAsRelationshipById'");
    }

    @Override
    public List<Fluctuation> getAllFluctuations() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllFluctuations'");
    }

    @Override
    public Fluctuation updateFluctuation(Fluctuation fluctuation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateFluctuation'");
    }

    @Override
    public Fluctuation getFluctuationById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFluctuationById'");
    }

    @Override
    public Fluctuation addFluctuation(Fluctuation fluctuation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFluctuation'");
    }

    @Override
    public void deleteFluctuationById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteFluctuationById'");
    }
    
}
