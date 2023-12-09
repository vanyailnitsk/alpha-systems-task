package com.example.nistcpeapi.models;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class Title {
    private String title;
    private String lang;
}
