package com.attendance.backend.domain.repository;

import com.attendance.backend.domain.entity.Company;
import com.attendance.backend.domain.entity.CompanySetting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanySettingRepository extends JpaRepository<CompanySetting, Long> {
    Optional<CompanySetting> findByCompany(Company company);
}
