package com.project.forestresourcesmanageapplication.servicesimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.project.forestresourcesmanageapplication.dtos.CoordinatesDTO;
import com.project.forestresourcesmanageapplication.dtos.OperationFormDTO;
import com.project.forestresourcesmanageapplication.dtos.ProductionTypeDTO;
import com.project.forestresourcesmanageapplication.dtos.WfPtRelationshipDTO;
import com.project.forestresourcesmanageapplication.dtos.WoodFacilitiesDTO;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataAlreadyExistsException;
import com.project.forestresourcesmanageapplication.exceptionhandling.DataNotFoundException;
import com.project.forestresourcesmanageapplication.exceptionhandling.InvalidDataException;
import com.project.forestresourcesmanageapplication.models.Administration;
import com.project.forestresourcesmanageapplication.models.AnimalStorageFacilities;
import com.project.forestresourcesmanageapplication.models.OperationForm;
import com.project.forestresourcesmanageapplication.models.ProductionType;
import com.project.forestresourcesmanageapplication.models.WfPtRelationship;
import com.project.forestresourcesmanageapplication.models.WoodFacilities;
import com.project.forestresourcesmanageapplication.repositories.OperationFormRepository;
import com.project.forestresourcesmanageapplication.repositories.ProductionTypeRepository;
import com.project.forestresourcesmanageapplication.repositories.WfPtRelationshipRepository;
import com.project.forestresourcesmanageapplication.repositories.WoodFacilitiesRepository;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantity;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInMoth;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInQuarter;
import com.project.forestresourcesmanageapplication.responses.FacilitiesQuantityInYear;
import com.project.forestresourcesmanageapplication.responses.FacilityQuantity;
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

    // -------------------------CƠ SỞ SẢN XUẤT CHẾ BIẾN GỖ------------------
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
                throw new DataNotFoundException("Không tìm thấy cơ sở hành chính với mã = "
                        + woodFacilitiesDTO.getAdminstrationCode());
            }
        }

        // kiểm tra hình thức hoạt động
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
                    "Không tìm thấy cơ sở hành chính với mã = " + woodFacilitiesDTO.getAdminstrationCode());
        }
        // kiểm tra hình thức hoạt động
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

    // --------------------Tọa độ trên bản đồ-----------------------------
    public List<CoordinatesDTO> retrieveAllCoordinates() {
        List<WoodFacilities> woodFacilities = this.getAllWoodFacilities();
        List<CoordinatesDTO> coordinatesDTOs = woodFacilities.stream().map((facility) -> {
            CoordinatesDTO coordinatesDTO = CoordinatesDTO.builder()
                    .code(facility.getCode())
                    .lat(facility.getLat())
                    .lng(facility.getLng())
                    .build();
            return coordinatesDTO;
        }).toList();
        return coordinatesDTOs;
    }

    public CoordinatesDTO retrieveCoordinates(String code) {
        WoodFacilities woodFacilities = this.getWoodFacilitiesByCode(code);
        CoordinatesDTO coordinatesDTO = new CoordinatesDTO(woodFacilities.getCode(),
                woodFacilities.getLat(), woodFacilities.getLng());
        return coordinatesDTO;
    }

    public CoordinatesDTO updateCoordinates(CoordinatesDTO coordinatesDTO) {
        WoodFacilities woodFacilities = this
                .getWoodFacilitiesByCode(coordinatesDTO.getCode());
        woodFacilities.setLat(coordinatesDTO.getLat());
        woodFacilities.setLng(coordinatesDTO.getLng());
        this.woodFacilitiesRepository.save(woodFacilities);
        return coordinatesDTO;
    }

    public void deleteCoordinates(String code) {
        WoodFacilities woodFacilities = this
                .getWoodFacilitiesByCode(code);
        woodFacilities.setLat("");
        woodFacilities.setLng("");
        this.woodFacilitiesRepository.save(woodFacilities);
    }

    // -------------------------LOẠI HÌNH SẢN XUẤT------------------
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
                .orElseThrow(
                        () -> new DataNotFoundException("Không tìm thấy loại hình sản xuất với loại gỗ = " + woodType));
    }

    @Override
    @Transactional
    public ProductionType addProductionType(ProductionTypeDTO productionTypeDTO, MultipartFile imageFile) {
        // kiểm tra tên
        Optional<ProductionType> tmp = this.productionTypeRepository.findById(productionTypeDTO.getWoodType());
        if (tmp.isPresent()) {
            throw new DataAlreadyExistsException(
                    "Đã tồn tại loại hình sản xuất với loại gỗ = " + productionTypeDTO.getWoodType());
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

    // -------------------------HÌNH THỨC HOẠT ĐỘNG------------------
    @Override
    public List<OperationForm> getAllOperationForm() {
        return operationFormRepository.findAll();
    }

    @Override
    @Transactional
    public OperationForm updateOperationForm(String name, OperationFormDTO operationFormDTO) {
        OperationForm operationForm = this.getOperationFormByName(operationFormDTO.getName());

        operationForm.setDescription(operationFormDTO.getDescription());

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
            throw new DataAlreadyExistsException(
                    "Đã tồn tại hình thức hoạt động với tên = " + operationFormDTO.getName());
        }

        OperationForm operationForm = OperationForm.builder()
                .name(operationFormDTO.getName())
                .description(operationFormDTO.getDescription())
                .build();

        return operationFormRepository.save(operationForm);
    }

    @Override
    public void deleteOperationFormByName(String name) {
        try {
            OperationForm operationForm = this.getOperationFormByName(name);
            this.operationFormRepository.deleteById(operationForm.getName());
        } catch (Exception e) {
            throw new InvalidDataException("Hình thức hoạt động này vẫn đang được sử dụng trong các cơ sở");
        }
    }

    // -------------------------QUAN HỆ CSSX GỖ VÀ LOẠI HÌNH SX------------------
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
        HashMap<String, List<FacilityQuantity>> data = this.getAllQuantityOfAnimal(Date.valueOf(LocalDate.now()));
        List<FacilityQuantity> facilityQuantitys = data.get(woodFacilities.getCode()) == null ? new ArrayList<>()
                : data.get(woodFacilities.getCode());
        for (int i = 0; i < facilityQuantitys.size(); i++) {
            if (facilityQuantitys.get(i).getObjName().equals(productionType.getWoodType())) {
                if ((facilityQuantitys.get(i).getQuantity() + wfPtRelationshipDTO.getQuantity()) < 0) {
                    throw new InvalidDataException("Số lượng thống kê không hợp lệ");
                }
            }
        }
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
    // Hàm hỗ trợ
    // -------------START--------------
    private LocalDate caculateDate(LocalDate date) {
        int yearData = date.getYear();
        int monthData = date.getMonthValue();
        int day = date.lengthOfMonth();
        return LocalDate.of(yearData, monthData, day);
    }

    private String changeToQuarter(LocalDate date) {
        int month = date.getMonthValue();
        int year = date.getYear();

        return (month >= 1 && month <= 3) ? "Q1-" + year
                : (month >= 4 && month <= 6) ? "Q2-" + year : (month >= 7 && month <= 9) ? "Q3-" + year : "Q4-" + year;
    }

    private LocalDate getLastDayOfQuarter(LocalDate date) {
        LocalDate firstDayOfQuarter = date.with(date.getMonth().firstMonthOfQuarter())
                .with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfQuarter = firstDayOfQuarter.plusMonths(2)
                .with(TemporalAdjusters.lastDayOfMonth());
        return lastDayOfQuarter;
    }

    private LocalDate getFirstDayOfQuarter(LocalDate date) {
        LocalDate firstDayOfQuarter = date.with(date.getMonth().firstMonthOfQuarter())
                .with(TemporalAdjusters.firstDayOfMonth());
        return firstDayOfQuarter;
    }

    private long getMonthQuantityOfFacilities(String name, LocalDate date) {
        LocalDate date2 = this.caculateDate(date);
        Date dateExisting = Date.valueOf(date2);
        try {
            long quantity = this.wfPtRelationshipRepository.getMonthQuantityOfFacilitiesNew(name, dateExisting);
            return quantity;
        } catch (Exception e) {
            return 0;
        }
    }

    private int getTotalBeforeFuntion(String name, LocalDate date, int i) {
        LocalDate date2 = this.caculateDate(date);
        Date dateExisting = Date.valueOf(date2);
        try {
            long quantity = this.wfPtRelationshipRepository.getMonthQuantityOfFacilitiesNew(name, dateExisting);
            return i + 1;
        } catch (Exception e) {
            return i;
        }

    }

    private List<FacilitiesQuantity> getQuarterFacilitiesWithStartDate(LocalDate startDate) {
        LocalDate dateTmp = startDate;
        List<FacilitiesQuantity> list = new ArrayList<>();
        LocalDate lastDayOfQuarter = this.getLastDayOfQuarter(startDate);
        List<String> listWF = this.woodFacilitiesRepository
                .findAllFacilitiesNameBeforeTime(Date.valueOf(lastDayOfQuarter));
        for (String wf : listWF) {
            long sum = 0;
            int i = 0;
            for (int j = 1; j <= 3; j++) {
                sum += this.getMonthQuantityOfFacilities(wf, startDate);
                i = this.getTotalBeforeFuntion(wf, startDate, i);

                startDate = startDate.plusMonths(1);
            }
            if (i == 0) {
                throw new DataNotFoundException("Dữ liệu trong " + this.changeToQuarter(startDate) + " của "
                        + wf + " không tồn tại");
            }
            long quantity = sum / i;
            FacilitiesQuantity facilitiesQuantity = new FacilitiesQuantity(wf, quantity);

            list.add(facilitiesQuantity);
            startDate = dateTmp;
        }
        return list;
    }

    private List<FacilitiesQuantity> getYearQuantityFacilitiesWithYear(int year) {
        List<FacilitiesQuantity> list = new ArrayList<>();
        String endDate = year + "-12-30";
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate dateTmp = startDate;
        List<String> listWF = this.woodFacilitiesRepository
                .findAllFacilitiesNameBeforeTime(Date.valueOf(endDate));
        for (String wf : listWF) {
            long sum = 0;
            int i = 0;
            for (int j = 1; j <= 12; j++) {
                sum += this.getMonthQuantityOfFacilities(wf, startDate);
                i = this.getTotalBeforeFuntion(wf, startDate, i);
                startDate = startDate.plusMonths(1);
            }
            if (i == 0) {
                throw new DataNotFoundException(
                        "Dữ liệu trong năm " + year + " của " + wf + " không tồn tại");
            }
            long quantity = sum / i;
            FacilitiesQuantity facilitiesQuantity = new FacilitiesQuantity(wf, quantity);
            list.add(facilitiesQuantity);
            startDate = dateTmp;
        }
        return list;
    }
    // ----------------END-----------------

    @Override
    public List<FacilitiesQuantity> getQuantityOfFacilitiesBeforeTime(LocalDate date) {
        List<FacilitiesQuantity> facilitiesQuantities = this.wfPtRelationshipRepository
                .selectAllQuantityOfFacilities(Date.valueOf(date));
        if (facilitiesQuantities.isEmpty()) {
            throw new DataNotFoundException(
                    "Dữ liệu trong tháng " + date.getMonthValue() + " năm " + date.getYear() + " chưa tồn tại");
        }
        return facilitiesQuantities;
    }

    // thống kê theo tháng
    @Override
    public List<FacilitiesQuantityInMoth> getMonthQuantityFacilitiesWithTime(LocalDate beginDate, LocalDate endDate) {
        List<FacilitiesQuantityInMoth> allData = new ArrayList<>();
        beginDate = this.caculateDate(beginDate);
        endDate = this.caculateDate(endDate);
        if (endDate.isAfter(LocalDate.now())) {
            endDate = LocalDate.now();
            while (beginDate.isBefore(endDate.plusDays(1))) {
                List<FacilitiesQuantity> data = this.getQuantityOfFacilitiesBeforeTime(beginDate);
                FacilitiesQuantityInMoth tmp = FacilitiesQuantityInMoth.builder().date(beginDate).data(data).build();
                allData.add(tmp);
                beginDate = beginDate.plusMonths(1);
                beginDate = this.caculateDate(beginDate);
            }
            List<FacilitiesQuantity> data = this.getQuantityOfFacilitiesBeforeTime(endDate);
            FacilitiesQuantityInMoth tmp = FacilitiesQuantityInMoth.builder().date(endDate).data(data).build();
            allData.add(tmp);
        } else {
            while (beginDate.isBefore(endDate.plusDays(1))) {
                List<FacilitiesQuantity> data = this.getQuantityOfFacilitiesBeforeTime(beginDate);
                FacilitiesQuantityInMoth tmp = FacilitiesQuantityInMoth.builder().date(beginDate).data(data).build();
                allData.add(tmp);
                beginDate = beginDate.plusMonths(1);
                beginDate = this.caculateDate(beginDate);
            }
        }
        return allData;
    }

    // thống kê theo quý
    @Override
    public List<FacilitiesQuantityInQuarter> getQuarterQuantityOfFacilitiesWithTime(LocalDate startDate,
            LocalDate endDate) {
        if (this.changeToQuarter(startDate).equals(this.changeToQuarter(endDate))
                || startDate.isAfter(endDate)) {
            throw new DataAccessResourceFailureException(
                    "Ngày bắt đầu và ngày kết thúc không hợp lệ .Vui lòng nhập lại");
        }
        List<FacilitiesQuantityInQuarter> list = new ArrayList<>();
        while (true) {
            FacilitiesQuantityInQuarter tmp = new FacilitiesQuantityInQuarter();
            List<FacilitiesQuantity> data = this.getQuarterFacilitiesWithStartDate(startDate);
            tmp.setQuarter(this.changeToQuarter(startDate));
            tmp.setData(data);
            list.add(tmp);
            LocalDate firstDayNextQuarter = this.getLastDayOfQuarter(startDate).plusDays(1);
            startDate = firstDayNextQuarter;
            if (startDate.isAfter(endDate))
                break;
        }
        return list;
    }

    // thống kê theo năm
    @Override
    public List<FacilitiesQuantityInYear> getYearQuantityOfFacilitiesWithTime(int startYear, int endYear) {
        List<FacilitiesQuantityInYear> list = new ArrayList<>();
        if (startYear > endYear) {
            throw new DataAccessResourceFailureException("Năm bắt đầu và năm kết thúc không hợp lệ .Vui lòng nhập lại");
        }
        if (startYear == endYear) {
            List<FacilitiesQuantity> data = this.getYearQuantityFacilitiesWithYear(endYear);
            FacilitiesQuantityInYear tmp = new FacilitiesQuantityInYear(endYear, data);
            list.add(tmp);
            return list;
        }
        while (true) {
            FacilitiesQuantityInYear tmp = new FacilitiesQuantityInYear();
            List<FacilitiesQuantity> data = this.getYearQuantityFacilitiesWithYear(startYear);
            tmp.setYear(startYear);
            tmp.setData(data);
            list.add(tmp);
            int nextYear = startYear + 1;
            if (nextYear == endYear) {
                List<FacilitiesQuantity> data2 = this.getYearQuantityFacilitiesWithYear(endYear);
                FacilitiesQuantityInYear tmp2 = new FacilitiesQuantityInYear(endYear, data2);
                list.add(tmp2);
                break;
            }
            startYear = nextYear;
        }
        return list;
    }

    @Override
    public HashMap<String, List<FacilityQuantity>> getAllQuantityOfAnimal(Date date) {
        List<FacilityQuantity> woodQuantities = this.wfPtRelationshipRepository.selectAllQuantityOfAllWoodType(date);
        HashMap<String, List<FacilityQuantity>> woodQuantitiesMap = new HashMap<>();
        woodQuantities.stream().forEach((res) -> {
            String facilitiesName = res.getFacilityName();
            List<FacilityQuantity> data = new ArrayList<>();
            if (woodQuantitiesMap.containsKey(facilitiesName)) {
                data = woodQuantitiesMap.get(facilitiesName);
                data.add(res);
                woodQuantitiesMap.put(facilitiesName, data);
            } else {
                data.add(res);
                woodQuantitiesMap.put(facilitiesName, data);
            }
        });
        return woodQuantitiesMap;
    }
    // test

}
