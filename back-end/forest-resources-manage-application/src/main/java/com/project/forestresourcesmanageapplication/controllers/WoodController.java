package com.project.forestresourcesmanageapplication.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.OperationFormDTO;
import com.project.forestresourcesmanageapplication.dtos.ProductionTypeDTO;
import com.project.forestresourcesmanageapplication.dtos.WoodFacilitiesDTO;
import com.project.forestresourcesmanageapplication.models.OperationForm;
import com.project.forestresourcesmanageapplication.models.ProductionType;
import com.project.forestresourcesmanageapplication.models.WoodFacilities;
import com.project.forestresourcesmanageapplication.services.WoodFacilitiesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/wood-facilities")
@RequiredArgsConstructor
public class WoodController {
    private final WoodFacilitiesService woodFacilitiesService;

    // ------------------------CƠ SỞ SẢN XUẤT GỖ-----------------------------
    @GetMapping("")
    public ResponseEntity<List<WoodFacilities>> getAllAnimalStorageFacilities() {
        List<WoodFacilities> list = this.woodFacilitiesService
                .getAllWoodFacilities();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{code}")
    public ResponseEntity<WoodFacilities> getWoodFacilitiesByCode(@PathVariable String code) {
        WoodFacilities woodFacilities = this.woodFacilitiesService
                .getWoodFacilitiesByCode(code);
        return ResponseEntity.ok(woodFacilities);
    }

    @PostMapping("/{code}")
    public ResponseEntity<WoodFacilities> addWoodFacilities(@PathVariable String code,
            @RequestBody WoodFacilitiesDTO woodFacilitiesDTO) {
        WoodFacilities woodFacilities = this.woodFacilitiesService
                .addWoodFacilities(woodFacilitiesDTO, code);
        return ResponseEntity.status(HttpStatus.CREATED).body(woodFacilities);
    }

    @PutMapping("/{code}")
    public ResponseEntity<WoodFacilities> updatewoodFacilities(@PathVariable String code,
            @RequestBody WoodFacilitiesDTO woodFacilitiesDTO) {
        WoodFacilities woodFacilities = this.woodFacilitiesService
                .updateWoodFacilities(code, woodFacilitiesDTO);
        return ResponseEntity.ok(woodFacilities);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<Void> deleteWoodFacilities(@PathVariable String code) {
        this.woodFacilitiesService.deleteWoodFacilitiesByCode(code);
        return ResponseEntity.ok().build();
    }

    // ---------------------------LOẠI HÌNH SẢN XUẤT--------------------------

    @GetMapping("/production-type")
    public ResponseEntity<List<ProductionType>> getProductionType() {
        List<ProductionType> list = this.woodFacilitiesService.getAllProductionType();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/production-type/{name}")
    public ResponseEntity<ProductionType> getProductionTypeByWoodName(@PathVariable String name) {
        ProductionType productionType = this.woodFacilitiesService.getProductionTypeByWoodName(name);
        return ResponseEntity.ok(productionType);
    }

    @GetMapping(value = "/production-type/images/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable String fileName) throws IOException {
        Path uploadPath = Path.of("uploads", fileName);
        InputStream in = Files.newInputStream(uploadPath);
        return IOUtils.toByteArray(in);
    }

    @PostMapping("/production-type/{name}")
    public ResponseEntity<ProductionType> addProductionType(@PathVariable String name,
            @RequestPart(name = "body") ProductionTypeDTO productionTypeDTO,
            @RequestParam(name = "file-image", required = false) MultipartFile imageFile) {
        ProductionType productionType = this.woodFacilitiesService.addProductionType(productionTypeDTO, imageFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(productionType);
    }

    @PutMapping("/production-type/{name}")
    public ResponseEntity<ProductionType> updateProductionType(@PathVariable String name,
            @RequestPart(name = "body") ProductionTypeDTO productionTypeDTO,
            @RequestParam(name = "file-image", required = false) MultipartFile imageFile) {
        ProductionType productionType = this.woodFacilitiesService.updateProductionType(productionTypeDTO,
                imageFile);
        return ResponseEntity.ok(productionType);
    }

    @DeleteMapping("/production-type/{name}")
    public ResponseEntity<Void> deleteProductionType(@PathVariable String name) {
        this.woodFacilitiesService.deleteProductionTypeByWoodName(name);
        return ResponseEntity.ok().build();
    }

    // ------------------------HÌNH THỨC HOẠT ĐỘNG-----------------------------
    @GetMapping("/operation-form")
    public ResponseEntity<List<OperationForm>> getAllOperationForm() {
        List<OperationForm> list = this.woodFacilitiesService
                .getAllOperationForm();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/operation-form/{name}")
    public ResponseEntity<OperationForm> getOperationFormByName(@PathVariable String name) {
        OperationForm operationForm = this.woodFacilitiesService
                .getOperationFormByName(name);
        return ResponseEntity.ok(operationForm);
    }

    @PostMapping("/operation-form/{name}")
    public ResponseEntity<OperationForm> addOperationForm(@PathVariable String name,
            @RequestBody OperationFormDTO operationFormDTO) {
        OperationForm operationForm = this.woodFacilitiesService
                .addOperationForm(operationFormDTO, name);
        return ResponseEntity.status(HttpStatus.CREATED).body(operationForm);
    }

    @PutMapping("/operation-form/{name}")
    public ResponseEntity<OperationForm> updateOperationForm(@PathVariable String name,
            @RequestBody OperationFormDTO operationFormDTO) {
        OperationForm operationForm = this.woodFacilitiesService
                .updateOperationForm(name, operationFormDTO);
        return ResponseEntity.ok(operationForm);
    }

    @DeleteMapping("/operation-form/{name}")
    public ResponseEntity<Void> deleteOperationForm(@PathVariable String name) {
        this.woodFacilitiesService.deleteOperationFormByName(name);
        return ResponseEntity.ok().build();
    }
    // --------------------------THỐNG KÊ DỮ LIỆU VỀ SỐ LƯỢNG----------------------

}
