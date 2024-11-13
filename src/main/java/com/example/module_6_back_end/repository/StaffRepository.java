package com.example.module_6_back_end.repository;

import com.example.module_6_back_end.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query("SELECT s FROM Staff s WHERE " +
            "(:codeStaff IS NULL OR s.codeStaff LIKE :codeStaff)" +
            "AND (:name Is NULL OR s.name LIKE :name)" +
            "AND (:position IS NULL OR s.position LIKE :position)"
    )
    List<Staff> findByCodeStaffOrNameOrPosition(@Param("codeStaff") String codeStaff,
                                                @Param("name") String name,
                                                @Param("position") String position);

    boolean existsByEmail(String email);

    boolean existsByCodeStaff(String codeStaff);

    boolean existsByPhone(String phone);
}
