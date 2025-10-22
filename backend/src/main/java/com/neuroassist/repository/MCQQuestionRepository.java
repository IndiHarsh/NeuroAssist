package com.neuroassist.repository;

import com.neuroassist.model.MCQQuestion;
import com.neuroassist.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MCQQuestionRepository extends JpaRepository<MCQQuestion, Long> {
    List<MCQQuestion> findByPatient(Patient patient);
}
