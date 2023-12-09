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
public class CPE {
    private boolean deprecated;
    private String cpeName;
    private String cpeNameId;
    private Timestamp lastModified;
    private Timestamp created;
    private List<Title> titles;
    private List<Ref> refs;
    private List<DeprecatedBy> deprecatedBy;
    private List<Deprecates> deprecates;

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

    @Getter
    @Setter
    public static class DeprecatedBy {
        private String cpeName;
        private String cpeNameId;

        @Override
        public String toString() {
            return "DeprecatedBy{" +
                    "cpeName='" + cpeName + '\'' +
                    ", cpeNameId='" + cpeNameId + '\'' +
                    '}';
        }
    }

    @Getter
    @Setter
    public static class Deprecates {
        private String cpeName;
        private String cpeNameId;

        @Override
        public String toString() {
            return "Deprecates{" +
                    "cpeName='" + cpeName + '\'' +
                    ", cpeNameId='" + cpeNameId + '\'' +
                    '}';
        }
    }
}
