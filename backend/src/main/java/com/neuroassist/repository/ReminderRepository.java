package com.neuroassist.repository;

import com.neuroassist.model.Patient;
import com.neuroassist.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByPatient(Patient patient);
    List<Reminder> findByPatientAndDoneFalseAndRemindAtBefore(Patient patient, LocalDateTime before);
}
