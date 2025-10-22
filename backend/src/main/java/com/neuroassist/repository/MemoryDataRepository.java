package com.neuroassist.repository;

import com.neuroassist.model.MemoryData;
import com.neuroassist.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoryDataRepository extends JpaRepository<MemoryData, Long> {
    List<MemoryData> findByPatient(Patient patient);
}
