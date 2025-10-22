package com.neuroassist.controller;

import com.neuroassist.model.MCQQuestion;
import com.neuroassist.model.Patient;
import com.neuroassist.model.User;
import com.neuroassist.repository.MCQQuestionRepository;
import com.neuroassist.service.CurrentUserService;
import com.neuroassist.service.McqService;
import com.neuroassist.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mcq")
public class McqController {

    private final McqService mcqService;
    private final MCQQuestionRepository mcqQuestionRepository;
    private final CurrentUserService currentUserService;
    private final PatientService patientService;

    public McqController(McqService mcqService, MCQQuestionRepository mcqQuestionRepository, CurrentUserService currentUserService, PatientService patientService) {
        this.mcqService = mcqService;
        this.mcqQuestionRepository = mcqQuestionRepository;
        this.currentUserService = currentUserService;
        this.patientService = patientService;
    }

    @PostMapping("/generate")
    public List<MCQQuestion> generate(@RequestParam(defaultValue = "5") int count) {
        User user = currentUserService.getCurrentUser();
        Patient p = patientService.ensurePatientFor(user);
        return mcqService.generateForPatient(p, Math.max(1, Math.min(count, 10)));
    }

    @GetMapping
    public List<MCQQuestion> list() {
        User user = currentUserService.getCurrentUser();
        Patient p = patientService.ensurePatientFor(user);
        return mcqQuestionRepository.findByPatient(p);
    }
}
