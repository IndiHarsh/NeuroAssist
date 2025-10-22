package com.neuroassist.controller;

import com.neuroassist.model.LocationPing;
import com.neuroassist.model.Patient;
import com.neuroassist.model.User;
import com.neuroassist.repository.LocationPingRepository;
import com.neuroassist.service.CurrentUserService;
import com.neuroassist.service.PatientService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private record PingReq(@NotNull Double latitude, @NotNull Double longitude) {}

    private final LocationPingRepository locationPingRepository;
    private final CurrentUserService currentUserService;
    private final PatientService patientService;

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public LocationController(LocationPingRepository locationPingRepository, CurrentUserService currentUserService, PatientService patientService) {
        this.locationPingRepository = locationPingRepository;
        this.currentUserService = currentUserService;
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<?> ping(@RequestBody PingReq req) {
        User user = currentUserService.getCurrentUser();
        Patient p = patientService.ensurePatientFor(user);
        LocationPing lp = new LocationPing();
        lp.setPatient(p);
        lp.setLatitude(req.latitude());
        lp.setLongitude(req.longitude());
        locationPingRepository.save(lp);
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("location").data(Map.of("lat", lp.getLatitude(), "lng", lp.getLongitude())));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitter);
            }
        }
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(Duration.ofMinutes(30).toMillis());
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }
}
