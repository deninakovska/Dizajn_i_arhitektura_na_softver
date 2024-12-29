package com.example.demo.repository;


import com.example.demo.entity.CompanyEntity;
import com.example.demo.entity.HistoricalDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricalDataRepository extends JpaRepository<HistoricalDataEntity, Long> {
    Optional<HistoricalDataEntity> findByDateAndCompany(LocalDate date, CompanyEntity company);
    List<HistoricalDataEntity> findAllByCompanyId(Long companyId);

}
