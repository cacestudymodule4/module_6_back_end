package com.example.module_6_back_end.service;

import com.example.module_6_back_end.dto.ReportRequest;
import com.example.module_6_back_end.model.Contract;
import com.example.module_6_back_end.model.Customer;
import com.example.module_6_back_end.model.Ground;
import com.example.module_6_back_end.model.Staff;
import com.example.module_6_back_end.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {
    @Autowired
    private ContractRepository contractRepository;

    @Override
    public List<Contract> getContracts() {
        return contractRepository.findAll();
    }

    @Override
    public Contract getContractById(Long id) {
        return contractRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Contract> searchContract(LocalDate startDate, LocalDate endDate, String taxCode, String nameCustomer, Long id, Pageable pageable) {
        return contractRepository.searchContract(startDate, endDate, taxCode, nameCustomer, id, pageable);
    }

    @Override
    public Page<Contract> searchAllContract(LocalDate startDate, LocalDate endDate, String taxCode, String name, Pageable pageable) {
        return contractRepository.searchAllContract(startDate, endDate, taxCode, name, pageable);
    }

    @Override
    public Contract saveContract(Contract contract) {
        return contractRepository.save(contract);
    }

    @Override
    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }

    public String generateUniqueTaxCode() {
        long texCode;
        String texCodeStr;
        boolean isUnique;
        List<Contract> list = getContracts();
        do {
            texCode = (long) (Math.random() * 1000000000);
            texCodeStr = String.valueOf(texCode);
            isUnique = true;
            for (Contract con : list) {
                if (con.getTaxCode().equals(texCodeStr)) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);
        return texCodeStr;
    }

    @Override
    public List<Contract> getContractsByCustomer(Customer customer) {
        return contractRepository.findByCustomer(customer);
    }

    @Override
    public void deleteContracts(Customer customer) {
        List<Contract> contracts = getContractsByCustomer(customer);
        contractRepository.deleteAll(contracts);
    }

    @Override
    public List<Contract> getContractsByStaff(Staff staff) {
        return contractRepository.findByStaff(staff);
    }

    @Override
    public void deleteContracts(Staff staff) {
        List<Contract> contracts = getContractsByStaff(staff);
        contractRepository.deleteAll(contracts);
    }

    @Override
    public String generateCode() {
        String id;
        boolean isUnique;
        List<Contract> list = getContracts();
        do {
            long randomNum = (long) (Math.random() * 100000);
            String randomNumStr = String.format("%05d", randomNum);
            id = "MHD" + randomNumStr;
            isUnique = true;
            for (Contract con : list) {
                if (con.getTaxCode().equals(id)) {
                    isUnique = false;
                    break;
                }
            }
        } while (!isUnique);
        return id;
    }

    public List<Contract> getContractsByStartDateAndEndDate(ReportRequest reportRequest) {
        LocalDate startDate = reportRequest.getStartDate();
        LocalDate endDate = reportRequest.getEndDate();
        if (startDate == null && endDate == null) {
            return contractRepository.findAll();
        }
        if (startDate != null && endDate != null) {
            return contractRepository.findByStartDateAndEndDate(startDate, endDate);
        }
        if (startDate != null) {
            return contractRepository.findByStartDate(startDate);
        }
        return contractRepository.findByEndDate(endDate);
    }

    @Override
    public Page<Contract> getAllContracts(Long id, Pageable pageable) {
        return contractRepository.findByStaffIdOrderByIdDesc(id, pageable);
    }

    @Override
    public List<Ground> getAddGroundH(LocalDate oneMonthFromNow) {
        return contractRepository.findGroundsWithConditions(oneMonthFromNow);
    }

    @Override
    public Page<Contract> getContractForAdmin(Pageable pageable) {
        return contractRepository.findAllByOrderByIdDesc(pageable);
    }

    @Override
    public Page<Contract> filterContract(Long id, Pageable pageable,String filter) {
        LocalDate now = LocalDate.now();
        if (id != 1) {
            if ("Có hiệu lực".equals(filter)) {
                return contractRepository.findByStartDateLessThanEqualAndEndDateGreaterThanAndStaffId(now, now, id, pageable);
            } else if ("Hết hiệu lực".equals(filter)) {
                return contractRepository.findByEndDateLessThanAndStaffId(now, id, pageable);
            } else if ("Chưa có hiệu lực".equals(filter)) {
                return contractRepository.findByStartDateGreaterThanAndStaffId(now, id, pageable);
            } else {
                return contractRepository.findAllByOrderByIdDesc( pageable);
            }
        }else{
            if ("Có hiệu lực".equals(filter)) {
                return contractRepository.findByStartDateLessThanEqualAndEndDateGreaterThan(now, now,pageable);
            } else if ("Hết hiệu lực".equals(filter)) {
                return contractRepository.findByEndDateLessThan(now, pageable);
            } else if ("Chưa có hiệu lực".equals(filter)) {
                return contractRepository.findByStartDateGreaterThan(now, pageable);
            } else {
                return contractRepository.findAllByOrderByIdDesc( pageable);
            }
        }
    }

    @Override
    public List<Contract> getActiveContractByStaff(Staff staff) {
        LocalDate now = LocalDate.now();
        return contractRepository.findByStaff(staff);
    }

}