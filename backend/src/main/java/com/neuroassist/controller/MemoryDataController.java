package com.neuroassist.controller;

import com.neuroassist.model.MemoryData;
import com.neuroassist.model.Patient;
import com.neuroassist.model.User;
import com.neuroassist.repository.MemoryDataRepository;
import com.neuroassist.service.CurrentUserService;
import com.neuroassist.service.PatientService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memory")
public class MemoryDataController {

    private record MemoryReq(@NotBlank String category, @NotBlank String title, @NotBlank String detail) {}

    private final MemoryDataRepository memoryDataRepository;
    private final CurrentUserService currentUserService;
    private final PatientService patientService;

    public MemoryDataController(MemoryDataRepository memoryDataRepository, CurrentUserService currentUserService, PatientService patientService) {
        this.memoryDataRepository = memoryDataRepository;
        this.currentUserService = currentUserService;
        this.patientService = patientService;
    }

    @GetMapping
    public List<MemoryData> list() {
        User user = currentUserService.getCurrentUser();
        Patient p = patientService.ensurePatientFor(user);
        return memoryDataRepository.findByPatient(p);
    }

    @PostMapping
    public MemoryData create(@RequestBody MemoryReq req) {
        User user = currentUserService.getCurrentUser();
        Patient p = patientService.ensurePatientFor(user);
        MemoryData md = new MemoryData();
        md.setPatient(p);
        md.setCategory(req.category());
        md.setTitle(req.title());
        md.setDetail(req.detail());
        return memoryDataRepository.save(md);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        memoryDataRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
