package com.example.module_6_back_end.service;

import com.example.module_6_back_end.exception.UnauthorizedException;
import com.example.module_6_back_end.exception.ValidationException;
import com.example.module_6_back_end.model.Contract;
import com.example.module_6_back_end.model.Role;
import com.example.module_6_back_end.model.Staff;
import com.example.module_6_back_end.model.User;
import com.example.module_6_back_end.repository.ContractRepository;
import com.example.module_6_back_end.repository.RoleRepository;
import com.example.module_6_back_end.repository.StaffRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final ContractService contractService;
    private final RegisterService registerService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final ContractRepository contractRepository;

    public StaffServiceImpl(StaffRepository staffRepository, ContractService contractService, RegisterService registerService, UserService userService, RoleRepository roleRepository, ContractRepository contractRepository) {
        this.staffRepository = staffRepository;
        this.contractService = contractService;
        this.registerService = registerService;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.contractRepository = contractRepository;
    }

    @Override
    public Page<Staff> getAllStaff(PageRequest pageRequest) {
        return staffRepository.findAll(pageRequest);
    }

    @Override
    public Staff getStaffId(Long id) {
        return staffRepository.findById(id).orElse(null);
    }

    @Override
    public void disableStaff(Long staffId) {
        userService.isAdmin();

        // Tìm nhân viên
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhân viên"));
        staff.setDisabled(true);
        staffRepository.save(staff);
//        // Kiểm tra hợp đồng còn hiệu lực
//        List<Contract> contracts = contractRepository.findByStaff_Id(staffId);
//        for (Contract contract : contracts) {
//            if (contract.getEndDate() == null || contract.getEndDate().isAfter(LocalDate.now())) {
//                throw new IllegalStateException("Nhân viên này đang có hợp đồng còn hiệu lực, không thể vô hiệu hóa.");
//            }
//        }
//
//        // Đánh dấu vô hiệu hóa
//        staff.setDisabled(true);
//        staffRepository.save(staff);
//        return "Nhân viên đã được vô hiệu hóa.";
    }

    @Override
    public List<Staff> getActiveStaff() {
        return staffRepository.findByIsDisabledFalse();
    }

    @Override
    public Page<Staff> searchStaff(String codeStaff, String name, String position, Pageable pageable) {
        return staffRepository.findByCodeStaffOrNameOrPosition(codeStaff, name, position, pageable);
    }

    @Override
    public Staff updateStaff(Long id, Staff staff) {
        Optional<Staff> existingStaff = staffRepository.findById(id);
        if (existingStaff.isPresent()) {
            Staff staffToUpdate = existingStaff.get();
            staffToUpdate.setName(staff.getName());
            staffToUpdate.setEmail(staff.getEmail());
            staffToUpdate.setPhone(staff.getPhone());
            staffToUpdate.setAddress(staff.getAddress());
            staffToUpdate.setBirthday(staff.getBirthday());
            staffToUpdate.setSalary(staff.getSalary());
            staffToUpdate.setStartDate(staff.getStartDate());
            staffToUpdate.setPosition(staff.getPosition());
            return staffRepository.save(staffToUpdate);
        }
        return null;
    }

    @Override
    public Staff getStaffById(Long id) {
        Optional<Staff> staff = staffRepository.findById(id);
        return staff.orElse(null);
    }

    @Override
    public Staff saveStaff(Staff staff) {
        userService.isAdmin();
        Map<String, String> error = new HashMap<>();
        if (staffRepository.existsByIdentification(staff.getIdentification())) {
            Staff oldStaff = staffRepository.findByIdentification(staff.getIdentification());
            oldStaff.setDisabled(false);
            return staffRepository.save(oldStaff);
        }
        if (staffRepository.existsByEmail(staff.getEmail())) {
            error.put("Email", "Email đã có người sử dụng");
        }
        if (staffRepository.existsByPhone(staff.getPhone())) {
            error.put("Phone", "SĐT đã có người sử dụng");
        }
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
        Staff newStaff = staffRepository.save(staff);
        registerService.registerUser(newStaff);
        return newStaff;
    }

    public boolean existsByEmail(String email) {
        return staffRepository.existsByEmail(email);
    }

    public boolean existsByPhone(String phone) {
        return staffRepository.existsByPhone(phone);
    }

    @Override
    public boolean existsByIdentification(String identification) {
        return staffRepository.existsByIdentification(identification);
    }

    @Override
    public List<Staff> getStaffByNameContaining(String name) {
        return staffRepository.findByNameContaining(name);
    }

    @Override
    public List<Staff> getStaffList() {
        return staffRepository.findByCodeStaffNotContaining("STF001");
    }
}
