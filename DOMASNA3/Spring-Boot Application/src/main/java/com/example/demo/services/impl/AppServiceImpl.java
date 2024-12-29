package com.example.demo.services.impl;

import com.example.demo.entity.CompanyEntity;
import com.example.demo.entity.HistoricalDataEntity;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.HistoricalDataRepository;
import com.example.demo.services.AppService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppServiceImpl implements AppService {

    private final CompanyRepository comRepository;
    private final HistoricalDataRepository historicalDataRepository;

    public List<CompanyEntity> listAll() {
        return comRepository.findAll();
    }

    public List<HistoricalDataEntity> listAllData() {
        return historicalDataRepository.findAll();
    }
    public List<HistoricalDataEntity> findAllByCompanyId(Long companyId) {
        return historicalDataRepository.findAllByCompanyId(companyId);
    }






}
