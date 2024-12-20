package com.example.module_6_back_end.service;

import com.example.module_6_back_end.dto.PageRequestDTO;
import com.example.module_6_back_end.dto.SalaryResponse;
import com.example.module_6_back_end.model.Staff;
import com.example.module_6_back_end.repository.StaffRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalaryServiceImpl implements SalaryService {
    private final StaffRepository staffRepository;
    private final UserService userService;

    public SalaryServiceImpl(StaffRepository staffRepository, UserService userService) {
        this.staffRepository = staffRepository;
        this.userService = userService;
    }

    @Override
    public Page<SalaryResponse> getSalary(PageRequestDTO pageRequest, String positionName) {
        System.out.println(positionName);
        userService.isAdmin();
        Sort sort = pageRequest.getSortDir().equalsIgnoreCase("asc")
                ? Sort.by(pageRequest.getSort()).ascending()
                : Sort.by(pageRequest.getSort()).descending();
        Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), sort);
        return staffRepository.findStaff("%" + pageRequest.getQ() + "%", pageable, "%" + positionName + "%")
                .map(staffEntity -> {
                    SalaryResponse salaryResponse = new SalaryResponse();
                    BeanUtils.copyProperties(staffEntity, salaryResponse);
                    return salaryResponse;
                });
    }

    @Override
    public List<SalaryResponse> getSalary() {
        userService.isAdmin();
        List<Staff> staffList = staffRepository.findAll();
        List<SalaryResponse> salaryResponseList = new ArrayList<>();
        for (Staff staff : staffList) {
            SalaryResponse salaryResponse = new SalaryResponse();
            BeanUtils.copyProperties(staff, salaryResponse);
            salaryResponseList.add(salaryResponse);
        }
        return salaryResponseList;
    }
}
