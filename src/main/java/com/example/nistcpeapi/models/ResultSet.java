package com.example.nistcpeapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResultSet {
    private Integer resultsPerPage;
    private Integer startIndex;
    private Integer totalResults;
    private String format;
    private String version;
    private Timestamp timestamp;
    private List<Product> products;
}
