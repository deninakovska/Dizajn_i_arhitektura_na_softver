package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_code")
    private String companyCode;

    @Column(name = "last_updated")
    private LocalDate lastUpdated;

    @OneToMany
    private List<HistoricalDataEntity> historicalData;

    public CompanyEntity(String companyCode) {
        this.companyCode = companyCode;
    }

}
