package com.example.nistcpeapi.repositories;

import com.example.nistcpeapi.models.CPE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CpeRepository extends JpaRepository<CPE, UUID> {
    List<CPE> findAllByCpeNameIn(List<String> names);
    @Query("SELECT c FROM CPE c " +
            "LEFT JOIN c.titles t " +
            "LEFT JOIN c.refs r " +
            "WHERE c.cpeName LIKE %:cpeName% " +
            "AND (t.title LIKE %:description% OR r.ref LIKE %:description%)")
    Page<CPE> findByCpeNameAndDescription(@Param("cpeName") String cpeName, @Param("description") String description, Pageable pageable);

}
