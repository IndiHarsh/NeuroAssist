package com.neuroassist.service;

import com.neuroassist.model.Patient;
import com.neuroassist.model.User;
import com.neuroassist.repository.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient ensurePatientFor(User user) {
        return patientRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Patient p = new Patient();
                    p.setUser(user);
                    p.setDisplayName(user.getEmail());
                    return patientRepository.save(p);
                });
    }
}
