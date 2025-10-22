package com.neuroassist.repository;

import com.neuroassist.model.LocationPing;
import com.neuroassist.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationPingRepository extends JpaRepository<LocationPing, Long> {
    Optional<LocationPing> findTopByPatientOrderByCreatedAtDesc(Patient patient);
}
