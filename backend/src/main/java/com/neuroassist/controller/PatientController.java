package com.neuroassist.controller;

import com.neuroassist.model.Patient;
import com.neuroassist.model.User;
import com.neuroassist.service.CurrentUserService;
import com.neuroassist.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    private final CurrentUserService currentUserService;
    private final PatientService patientService;

    public PatientController(CurrentUserService currentUserService, PatientService patientService) {
        this.currentUserService = currentUserService;
        this.patientService = patientService;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        User user = currentUserService.getCurrentUser();
        Patient patient = patientService.ensurePatientFor(user);
        return ResponseEntity.ok(Map.of(
                "id", patient.getId(),
                "displayName", patient.getDisplayName()
        ));
    }
}
