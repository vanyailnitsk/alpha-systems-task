package com.example.nistcpeapi.repositories;

import com.example.nistcpeapi.models.CPE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CpeRepository extends JpaRepository<CPE, UUID> {
    List<CPE> findAllByCpeNameIn(List<String> names);
}
