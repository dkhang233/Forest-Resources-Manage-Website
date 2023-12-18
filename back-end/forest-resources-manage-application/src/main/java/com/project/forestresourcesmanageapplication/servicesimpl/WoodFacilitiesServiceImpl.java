package com.project.forestresourcesmanageapplication.servicesimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.OperationFormDTO;
import com.project.forestresourcesmanageapplication.dtos.ProductionTypeDTO;
import com.project.forestresourcesmanageapplication.dtos.WfPtRelationshipDTO;
import com.project.forestresourcesmanageapplication.dtos.WoodFacilitiesDTO;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataAlreadyExistsException;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataNotFoundException;
import com.project.forestresourcesmanageapplication.exceptionhandling.InvalidDataException;
import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.models.OperationForm;
import com.project.forestresourcesmanageapplication.models.ProductionType;
import com.project.forestresourcesmanageapplication.models.WfPtRelationship;
import com.project.forestresourcesmanageapplication.models.WoodFacilities;
import com.project.forestresourcesmanageapplication.repositories.OperationFormRepository;
import com.project.forestresourcesmanageapplication.repositories.ProductionTypeRepository;
import com.project.forestresourcesmanageapplication.repositories.WfPtRelationshipRepository;
import com.project.forestresourcesmanageapplication.repositories.WoodFacilitiesRepository;
import com.project.forestresourcesmanageapplication.services.AdminstrationService;
import com.project.forestresourcesmanageapplication.services.WoodFacilitiesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WoodFacilitiesServiceImpl implements WoodFacilitiesService {
    private final WoodFacilitiesRepository woodFacilitiesRepository;
    private final ProductionTypeRepository productionTypeRepository;
    private final OperationFormRepository operationFormRepository;
    private final WfPtRelationshipRepository wfPtRelationshipRepository;
    private final AdminstrationService adminstrationService;


    //-------------------------CƠ SỞ SẢN XUẤT CHẾ BIẾN GỖ------------------
    @Override
    public List<WoodFacilities> getAllWoodFacilities() {
        return woodFacilitiesRepository.findAll();
    }

    @Override
    @Transactional
    public WoodFacilities updateWoodFacilities(String code, WoodFacilitiesDTO woodFacilitiesDTO) {
        WoodFacilities woodFacilitiesExisting = this.getWoodFacilitiesByCode(code);

        // kiểm tra tên
        if (!woodFacilitiesExisting.getName().equals(woodFacilitiesDTO.getName())) {
            Optional<WoodFacilities> tmp = this.woodFacilitiesRepository
                    .findByName(woodFacilitiesDTO.getName());
            if (tmp.isPresent()) {
                throw new DataAlreadyExistsException(
                        "Đã có cơ sở sản xuất gỗ với tên = " + woodFacilitiesDTO.getName());
            }
            woodFacilitiesExisting.setName(woodFacilitiesDTO.getName());
        }

        // kiểm tra đơn vị hành chính
        if (!woodFacilitiesExisting.getAdministration().getCode()
                .equals(woodFacilitiesDTO.getAdminstrationCode())) {
            try {
                Administration administration = this.adminstrationService
                        .retrieveAdministrationByCode(woodFacilitiesDTO.getAdminstrationCode());
                woodFacilitiesExisting.setAdministration(administration);
            } catch (Exception exception) {
                throw new DataNotFoundException("Không tìm thấy cơ sở hành chính với code = "
                        + woodFacilitiesDTO.getAdminstrationCode());
            }
        }

        //kiểm tra hình thức hoạt động
        if (!woodFacilitiesExisting.getOperationForm().getName()
                .equals(woodFacilitiesDTO.getOperationFormName())) {
                OperationForm operationForm = this.getOperationFormByName(woodFacilitiesDTO.getOperationFormName());
                woodFacilitiesExisting.setOperationForm(operationForm);
        }

        woodFacilitiesExisting.setCapacity(woodFacilitiesDTO.getCapacity());
        woodFacilitiesExisting.setDate(woodFacilitiesDTO.getDate());

        return woodFacilitiesRepository.save(woodFacilitiesExisting);
    }

    @Override
    public WoodFacilities getWoodFacilitiesByCode(String code) {
        return woodFacilitiesRepository.findById(code)
                .orElseThrow(
                        () -> new DataNotFoundException("Không tìm thấy cơ sở sản xuất gỗ với code = " + code));
    }

    @Override
    @Transactional
    public WoodFacilities addWoodFacilities(WoodFacilitiesDTO woodFacilitiesDTO, String code) {
        WoodFacilities woodFacilities = new WoodFacilities();

        // kiểm tra code
        Optional<WoodFacilities> tmp1 = this.woodFacilitiesRepository.findById(code);
        if (tmp1.isPresent()) {
            throw new DataAlreadyExistsException("Đã có cơ sở sản xuất gỗ với code = " + code);
        }
        // kiểm tra tên
        Optional<WoodFacilities> tmp2 = this.woodFacilitiesRepository
                .findByName(woodFacilitiesDTO.getName());
        if (tmp2.isPresent()) {
            throw new DataAlreadyExistsException(
                    "Đã có cơ sở sản xuất gỗ với tên = " + woodFacilitiesDTO.getName());
        }
        // kiểm tra đơn vị hành chính
        try {
            Administration administration = this.adminstrationService
                    .retrieveAdministrationByCode(woodFacilitiesDTO.getAdminstrationCode());
            woodFacilities.setAdministration(administration);
        } catch (Exception exception) {
            throw new DataNotFoundException(
                    "Không tìm thấy cơ sở hành chính với code = " + woodFacilitiesDTO.getAdminstrationCode());
        }
        //kiểm tra hình thức hoạt động
        OperationForm operationForm = this.getOperationFormByName(woodFacilitiesDTO.getOperationFormName());

        woodFacilities.setCode(code);
        woodFacilities.setName(woodFacilitiesDTO.getName());
        woodFacilities.setCapacity(woodFacilitiesDTO.getCapacity());
        woodFacilities.setDate(woodFacilitiesDTO.getDate());
        woodFacilities.setOperationForm(operationForm);

        return woodFacilitiesRepository.save(woodFacilities);
    }

    @Override
    @Transactional
    public void deleteWoodFacilitiesByCode(String code) {
        WoodFacilities woodFacilities = this.getWoodFacilitiesByCode(code);
        this.woodFacilitiesRepository.deleteById(woodFacilities.getCode());
    }


    //-------------------------LOẠI HÌNH SẢN XUẤT------------------
    // lƯu file ảnh avatar và trả về đường dẫn đến ảnh
    public String saveImage(MultipartFile avatarFile) {
        if (avatarFile == null) {
            return "";
        }

        // Kiểm tra kích thước file
        if (avatarFile.getSize() > 10 * 1024 * 1024) { // kích thước file phải <= 10 MB
            throw new InvalidDataException("Kích thước ảnh đại diện phải nhỏ hơn 10MB");
        }

        // Kiểm tra định dạng file
        String contentType = avatarFile.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) { // Phải là file ảnh
            throw new InvalidDataException("Ảnh đại diện phải là ảnh");
        }

        // Trích xuất và làm sạch tên file gốc từ hệ thống file của client
        String fileName = StringUtils.cleanPath(avatarFile.getOriginalFilename());

        // Tạo ra một tên file duy nhất
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

        // Tạo đường dẫn để lưu file
        Path uploadDir = Path.of("uploads");

        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path uploadPath = Path.of(uploadDir.toString(), uniqueFileName);
            Files.copy(avatarFile.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return uniqueFileName;
    }
    
    @Override
    public List<ProductionType> getAllProductionType() {
        return productionTypeRepository.findAll();
    }

    @Override
    @Transactional
    public ProductionType updateProductionType(ProductionTypeDTO productionTypeDTO, MultipartFile imageFile) {
        ProductionType productionType = this.getProductionTypeByWoodName(productionTypeDTO.getWoodType());

        // Kiểm tra image đã thay đổi chưa, nếu đã thay đổi -> gọi hàm để lưu file
        if (!productionType.getImage().equals(productionTypeDTO.getImage())) {
            String image = this.saveImage(imageFile);
            productionType.setImage(image);
        }

        productionType.setCapacity(productionTypeDTO.getCapacity());

        return productionTypeRepository.save(productionType);
    }

    @Override
    public ProductionType getProductionTypeByWoodName(String woodType) {
        return this.productionTypeRepository.findById(woodType)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy loại hình sản xuất với loại gỗ = " + woodType));
    }

    @Override
    @Transactional
    public ProductionType addProductionType(ProductionTypeDTO productionTypeDTO, MultipartFile imageFile) {
        // kiểm tra tên
        Optional<ProductionType> tmp = this.productionTypeRepository.findById(productionTypeDTO.getWoodType());
        if (tmp.isPresent()) {
            throw new DataAlreadyExistsException("Đã tồn tại loại hình sản xuất với loại gỗ = " + productionTypeDTO.getWoodType());
        }
        String image = this.saveImage(imageFile);

        ProductionType productionType = ProductionType.builder()
                .woodType(productionTypeDTO.getWoodType())
                .capacity(productionTypeDTO.getCapacity())
                .image(image)
                .build();

        return productionTypeRepository.save(productionType);
    }

    @Override
    @Transactional
    public void deleteProductionTypeByWoodName(String name) {
        ProductionType productionType = this.getProductionTypeByWoodName(name);
        this.productionTypeRepository.deleteById(productionType.getWoodType());
    }

    @Override
    public List<ProductionType> getAllProductionTypeByFacilitiesCode(String code) {
        List<ProductionType> productionTypes = this.wfPtRelationshipRepository
                .selectAllProductionTypeByFacilitiesCode(code)
                .orElseThrow(() -> new DataNotFoundException("cơ sở sản xuất này không có loại hình sản xuất nào"));
        return productionTypes;
    }


    //-------------------------HÌNH THỨC HOẠT ĐỘNG------------------
    @Override
    public List<OperationForm> getAllOperationForm() {
        return operationFormRepository.findAll();
    }

    @Override
    @Transactional
    public OperationForm updateOperationForm(String name, OperationFormDTO operationFormDTO) {
        OperationForm operationForm = this.getOperationFormByName(operationFormDTO.getName());

        operationForm.setDecription(operationFormDTO.getDecription());

        return operationFormRepository.save(operationForm);
    }

    @Override
    public OperationForm getOperationFormByName(String name) {
        return this.operationFormRepository.findById(name)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy hình thức hoạt động với tên = " + name));
    }

    @Override
    @Transactional
    public OperationForm addOperationForm(OperationFormDTO operationFormDTO, String name) {
        // kiểm tra tên
        Optional<OperationForm> tmp = this.operationFormRepository.findById(operationFormDTO.getName());
        if (tmp.isPresent()) {
            throw new DataAlreadyExistsException("Đã tồn tại hình thức hoạt động với tên = " + operationFormDTO.getName());
        }

        OperationForm operationForm = OperationForm.builder()
                .name(operationFormDTO.getName())
                .decription(operationFormDTO.getDecription())
                .build();

        return operationFormRepository.save(operationForm);
    }

    @Override
    public void deleteOperationFormByName(String name) {
        OperationForm operationForm = this.getOperationFormByName(name);
        this.operationFormRepository.deleteById(operationForm.getName());
    }


    //-------------------------QUAN HỆ CSSX GỖ VÀ LOẠI HÌNH SX------------------
    @Override
    public List<WfPtRelationship> getAllWfPtRelationship() {
        return this.wfPtRelationshipRepository.findAll();
    }

    @Override
    @Transactional
    public WfPtRelationship updateWfPtRelationship(int id, WfPtRelationshipDTO wfPtRelationshipDTO) {
        WfPtRelationship wfPtRelationship = this.getWfPtRelationshipById(id);
        // kiểm tra tên CSSX gỗ và tên loại hình sản xuất
        WoodFacilities woodFacilities = this
                .getWoodFacilitiesByCode(wfPtRelationshipDTO.getCodeWF());
        ProductionType productionType = this.getProductionTypeByWoodName(wfPtRelationshipDTO.getNamePT());

        wfPtRelationship.setWoodFacilities(woodFacilities);
        wfPtRelationship.setProductionType(productionType);
        wfPtRelationship.setQuantity(wfPtRelationshipDTO.getQuantity());
        wfPtRelationship.setDate(wfPtRelationshipDTO.getDate());

        return wfPtRelationshipRepository.save(wfPtRelationship);
    }

    @Override
    public WfPtRelationship getWfPtRelationshipById(int id) {
        return this.wfPtRelationshipRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tồn tại bảng quan hệ Wf-Pt với id = " + id));
    }

    @Override
    @Transactional
    public WfPtRelationship addWfPtRelationship(WfPtRelationshipDTO wfPtRelationshipDTO) {
        WfPtRelationship wfPtRelationship = new WfPtRelationship();
        // kiểm tra tên CSSX gỗ và tên loại hình sản xuất
        WoodFacilities woodFacilities = this
                .getWoodFacilitiesByCode(wfPtRelationshipDTO.getCodeWF());
        ProductionType productionType = this.getProductionTypeByWoodName(wfPtRelationshipDTO.getNamePT());

        wfPtRelationship.setWoodFacilities(woodFacilities);
        wfPtRelationship.setProductionType(productionType);
        wfPtRelationship.setQuantity(wfPtRelationshipDTO.getQuantity());
        wfPtRelationship.setDate(wfPtRelationshipDTO.getDate());

        return wfPtRelationshipRepository.save(wfPtRelationship);
    }

    @Override
    @Transactional
    public void deleteWfPtRelationshipById(int id) {
        WfPtRelationship wfPtRelationship = this.getWfPtRelationshipById(id);
        this.wfPtRelationshipRepository.deleteById(wfPtRelationship.getId());
    }

    // --------------------------THỐNG KÊ--------------------------------
    
}
