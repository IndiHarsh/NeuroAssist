package com.neuroassist.controller;

import com.neuroassist.model.Patient;
import com.neuroassist.model.Reminder;
import com.neuroassist.model.User;
import com.neuroassist.repository.ReminderRepository;
import com.neuroassist.service.CurrentUserService;
import com.neuroassist.service.PatientService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private record ReminderReq(@NotBlank String text, String remindAt) {}

    private final ReminderRepository reminderRepository;
    private final CurrentUserService currentUserService;
    private final PatientService patientService;

    public ReminderController(ReminderRepository reminderRepository, CurrentUserService currentUserService, PatientService patientService) {
        this.reminderRepository = reminderRepository;
        this.currentUserService = currentUserService;
        this.patientService = patientService;
    }

    @GetMapping
    public List<Reminder> list() {
        User user = currentUserService.getCurrentUser();
        Patient p = patientService.ensurePatientFor(user);
        return reminderRepository.findByPatient(p);
    }

    @PostMapping
    public Reminder create(@RequestBody ReminderReq req) {
        User user = currentUserService.getCurrentUser();
        Patient p = patientService.ensurePatientFor(user);
        Reminder r = new Reminder();
        r.setPatient(p);
        r.setText(req.text());
        if (req.remindAt() != null && !req.remindAt().isBlank()) {
            r.setRemindAt(LocalDateTime.parse(req.remindAt()));
        }
        r.setDone(false);
        return reminderRepository.save(r);
    }

    @PatchMapping("/{id}")
    public Reminder markDone(@PathVariable Long id, @RequestParam boolean done) {
        Reminder r = reminderRepository.findById(id).orElseThrow();
        r.setDone(done);
        return reminderRepository.save(r);
    }
}
