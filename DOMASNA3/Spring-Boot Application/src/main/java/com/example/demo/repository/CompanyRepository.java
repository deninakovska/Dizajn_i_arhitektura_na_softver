package com.example.demo.repository;

import com.example.demo.entity.CompanyEntity;
import com.example.demo.entity.HistoricalDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    Optional<CompanyEntity> findByCompanyCode(String companyCode);


}
