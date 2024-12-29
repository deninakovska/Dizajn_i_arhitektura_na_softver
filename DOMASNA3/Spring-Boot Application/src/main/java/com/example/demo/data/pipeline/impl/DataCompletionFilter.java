package com.example.demo.data.pipeline.impl;

import com.example.demo.data.DataParser;
import com.example.demo.data.pipeline.Filter;
import com.example.demo.entity.CompanyEntity;
import com.example.demo.entity.HistoricalDataEntity;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.HistoricalDataRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class DataCompletionFilter implements Filter<List<CompanyEntity>> {

    private final CompanyRepository companyRepository;
    private final HistoricalDataRepository historicalDataRepository;

    private static final String HISTORICAL_DATA_URL = "https://www.mse.mk/mk/stats/symbolhistory/";

    public DataCompletionFilter(CompanyRepository companyRepository, HistoricalDataRepository historicalDataRepository) {
        this.companyRepository = companyRepository;
        this.historicalDataRepository = historicalDataRepository;
    }

    public List<CompanyEntity> execute(List<CompanyEntity> input) throws IOException, ParseException {

        for (CompanyEntity company : input) {
            LocalDate fromDate = LocalDate.now();
            LocalDate toDate = LocalDate.now().plusYears(1);
            addHistoricalData(company, fromDate, toDate);
        }

        writeHistoricalDataToCsv("historical_data.csv");

        return null;
    }

    private void addHistoricalData(CompanyEntity company, LocalDate fromDate, LocalDate toDate) throws IOException, ParseException {
        Connection.Response response = Jsoup.connect(HISTORICAL_DATA_URL + company.getCompanyCode())
                .data("FromDate", fromDate.toString())
                .data("ToDate", toDate.toString())
                .method(Connection.Method.POST)
                .execute();

        Document document = response.parse();

        Element table = document.select("table#resultsTable").first();

        if (table != null) {
            Elements rows = table.select("tbody tr");

            for (Element row : rows) {
                Elements columns = row.select("td");

                if (columns.size() > 0) {
                    LocalDate date = DataParser.parseDate(columns.get(0).text(), "d.M.yyyy");

                    if (date != null && historicalDataRepository.findByDateAndCompany(date, company).isEmpty()) {
                        if (company.getLastUpdated() == null || company.getLastUpdated().isBefore(date)) {
                            company.setLastUpdated(date);
                            companyRepository.save(company);
                        }

                        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);

                        Double lastTransactionPrice = DataParser.parseDouble(columns.get(1).text(), format);
                        Double maxPrice = DataParser.parseDouble(columns.get(2).text(), format);
                        Double minPrice = DataParser.parseDouble(columns.get(3).text(), format);
                        if(maxPrice == null || minPrice == null){
                            continue;
                        }
                        Double averagePrice = DataParser.parseDouble(columns.get(4).text(), format);
                        Double percentageChange = DataParser.parseDouble(columns.get(5).text(), format);
                        Integer quantity = DataParser.parseInteger(columns.get(6).text(), format);
                        Integer turnoverBest = DataParser.parseInteger(columns.get(7).text(), format);
                        Integer totalTurnover = DataParser.parseInteger(columns.get(8).text(), format);

                        HistoricalDataEntity historicalDataEntity = new HistoricalDataEntity(
                                date, lastTransactionPrice, maxPrice, minPrice, averagePrice, percentageChange,
                                quantity, turnoverBest, totalTurnover);
                        historicalDataEntity.setCompany(company);

                        historicalDataRepository.save(historicalDataEntity);
                    }
                }
            }
        }
    }
    private void writeHistoricalDataToCsv(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write header
            writer.append("Date,CompanyCode,LastTransactionPrice,MaxPrice,MinPrice,AveragePrice,PercentageChange,Quantity,TurnoverBest,TotalTurnover");
            writer.append("\n");

            // Fetch all historical data
            List<HistoricalDataEntity> historicalData = historicalDataRepository.findAll();

            // Write data rows
            for (HistoricalDataEntity data : historicalData) {
                writer.append(data.getDate().toString()).append(",");
                writer.append(data.getCompany().getCompanyCode()).append(",");
                writer.append(data.getLastTransactionPrice() != null ? data.getLastTransactionPrice().toString() : "").append(",");
                writer.append(data.getMaxPrice() != null ? data.getMaxPrice().toString() : "").append(",");
                writer.append(data.getMinPrice() != null ? data.getMinPrice().toString() : "").append(",");
                writer.append(data.getAveragePrice() != null ? data.getAveragePrice().toString() : "").append(",");
                writer.append(data.getPercentageChange() != null ? data.getPercentageChange().toString() : "").append(",");
                writer.append(data.getQuantity() != null ? data.getQuantity().toString() : "").append(",");
                writer.append(data.getTurnoverBest() != null ? data.getTurnoverBest().toString() : "").append(",");
                writer.append(data.getTotalTurnover() != null ? data.getTotalTurnover().toString() : "");
                writer.append("\n");
            }

            System.out.println("CSV file created successfully: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

}