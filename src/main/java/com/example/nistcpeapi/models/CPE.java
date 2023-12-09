package com.example.nistcpeapi.models;

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
public class CPE {
    private boolean deprecated;
    private String cpeName;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private List<CPE> deprecates;
    @ManyToMany(mappedBy = "deprecates",cascade = CascadeType.ALL)
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
