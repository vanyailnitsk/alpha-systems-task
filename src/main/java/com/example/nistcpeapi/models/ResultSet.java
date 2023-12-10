package com.example.nistcpeapi.models;

import com.example.nistcpeapi.json.CustomCpeListDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultSet {
    private Integer resultsPerPage;
    private Integer startIndex;
    private Integer totalResults;
    private String format;
    private String version;
    private Timestamp timestamp;
    @JsonDeserialize(using = CustomCpeListDeserializer.class)
    private List<CPE> products;
    public ResultSet(Page<CPE> page) {

    }
}
