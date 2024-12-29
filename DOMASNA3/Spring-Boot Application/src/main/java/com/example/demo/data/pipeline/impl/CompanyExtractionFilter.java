package com.example.demo.data.pipeline.impl;

import com.example.demo.data.pipeline.Filter;
import com.example.demo.entity.CompanyEntity;
import com.example.demo.repository.CompanyRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class CompanyExtractionFilter implements Filter<List<CompanyEntity>> {

    private final CompanyRepository companyRepository;

    public CompanyExtractionFilter(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    private static final String STOCK_MARKET_URL = "https://www.mse.mk/mk/stats/symbolhistory/kmb";

    @Override
    public List<CompanyEntity> execute(List<CompanyEntity> input) throws IOException {
        Document document = Jsoup.connect(STOCK_MARKET_URL).get();
        Element selectMenu = document.select("select#Code").first();

        if (selectMenu != null) {
            Elements options = selectMenu.select("option");
            for (Element option : options) {
                String code = option.attr("value");
                if (!code.isEmpty() && code.matches("^[a-zA-Z]+$")) {
                    if (companyRepository.findByCompanyCode(code).isEmpty()) {
                        companyRepository.save(new CompanyEntity(code));
                    }
                }
            }
        }
        return companyRepository.findAll();
    }
}
