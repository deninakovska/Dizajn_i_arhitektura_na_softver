package com.example.demo.services;

import com.example.demo.entity.CompanyEntity;
import com.example.demo.entity.HistoricalDataEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppService {
    List<CompanyEntity> listAll();
     List<HistoricalDataEntity> findAllByCompanyId(Long id);

     List<HistoricalDataEntity> listAllData();
}
