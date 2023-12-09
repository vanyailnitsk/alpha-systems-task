package com.example.nistcpeapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CPE {
    private boolean deprecated;
    private String cpeName;
    @Id
    private UUID cpeNameId;
    private Timestamp lastModified;
    private Timestamp created;
    @ElementCollection
    private List<Title> titles;
    @ElementCollection
    private List<Ref> refs;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "cpe_deprecates",
            joinColumns = @JoinColumn(name = "cpe_id"),
            inverseJoinColumns = @JoinColumn(name = "deprecates_id")
    )
    @JsonIgnoreProperties({"deprecated","lastModified", "created", "titles", "refs", "deprecates", "deprecatedBy"})
    private List<CPE> deprecates;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "cpe_deprecates",
            joinColumns = @JoinColumn(name = "deprecates_id"),
            inverseJoinColumns = @JoinColumn(name = "cpe_id")
    )
    @JsonIgnoreProperties({"deprecated","lastModified", "created", "titles", "refs", "deprecates", "deprecatedBy"})
    private List<CPE> deprecatedBy;

    @Override
    public String toString() {
        return "CPE{" +
                "deprecated=" + deprecated +
                ", cpeName='" + cpeName + '\'' +
                ", cpeNameId='" + cpeNameId + '\'' +
                ", lastModified=" + lastModified +
                ", created=" + created +
                ", titles=" + titles +
                ", refs=" + refs +
                ", deprecatedBy=" + deprecatedBy +
                ", deprecates=" + deprecates +
                '}';
    }
}
