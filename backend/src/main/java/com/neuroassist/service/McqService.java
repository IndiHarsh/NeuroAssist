package com.neuroassist.service;

import com.neuroassist.model.MCQQuestion;
import com.neuroassist.model.MemoryData;
import com.neuroassist.model.Patient;
import com.neuroassist.repository.MCQQuestionRepository;
import com.neuroassist.repository.MemoryDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class McqService {
    private final MemoryDataRepository memoryDataRepository;
    private final MCQQuestionRepository mcqQuestionRepository;

    private final String openAIApiKey;

    public McqService(MemoryDataRepository memoryDataRepository, MCQQuestionRepository mcqQuestionRepository,
                      @Value("${app.openai.apiKey:}") String openAIApiKey) {
        this.memoryDataRepository = memoryDataRepository;
        this.mcqQuestionRepository = mcqQuestionRepository;
        this.openAIApiKey = openAIApiKey == null ? "" : openAIApiKey;
    }

    public List<MCQQuestion> generateForPatient(Patient patient, int count) {
        List<MemoryData> memories = memoryDataRepository.findByPatient(patient);
        if (memories.isEmpty()) return List.of();
        List<MCQQuestion> created = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            MemoryData m = memories.get(random.nextInt(memories.size()));
            MCQQuestion q = new MCQQuestion();
            q.setPatient(patient);
            String prompt = m.getTitle() + ": " + m.getDetail();
            // Free fallback generator: simple distractors using other memory details
            List<String> pool = memories.stream().map(MemoryData::getDetail).distinct().collect(Collectors.toList());
            Collections.shuffle(pool, random);
            String correct = m.getDetail();
            List<String> options = new ArrayList<>();
            options.add(correct);
            for (String d : pool) {
                if (options.size() >= 4) break;
                if (!d.equals(correct)) options.add(d);
            }
            while (options.size() < 4) {
                options.add("Option " + (options.size() + 1));
            }
            Collections.shuffle(options, random);
            q.setOptions(options);
            q.setCorrectIndex(options.indexOf(correct));
            q.setQuestion("What matches this fact: " + prompt + "?");
            created.add(mcqQuestionRepository.save(q));
        }
        return created;
    }
}
