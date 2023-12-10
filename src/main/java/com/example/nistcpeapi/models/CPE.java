package com.example.nistcpeapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.*;

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
            joinColumns = @JoinColumn(name = "deprecates_id"),
            inverseJoinColumns = @JoinColumn(name = "cpe_id")
    )
    @JsonIgnoreProperties({"deprecated","lastModified", "created", "titles", "refs", "deprecates", "deprecatedBy"})
    private Set<CPE> deprecatedBy = new HashSet<>();
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "cpe_deprecates",
            joinColumns = @JoinColumn(name = "cpe_id"),
            inverseJoinColumns = @JoinColumn(name = "deprecates_id")
    )
    @JsonIgnoreProperties({"deprecated","lastModified", "created", "titles", "refs", "deprecates", "deprecatedBy"})
    private Set<CPE> deprecates = new HashSet<>();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CPE cpe = (CPE) o;
        return Objects.equals(cpeNameId, cpe.cpeNameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpeNameId);
    }
}
