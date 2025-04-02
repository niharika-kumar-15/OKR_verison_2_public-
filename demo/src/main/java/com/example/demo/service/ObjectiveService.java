package com.example.demo.service;

import com.example.demo.model.Objective;
import com.example.demo.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectiveService {

    @Autowired
    private ObjectiveRepository objectiveRepository;

    public Objective createObjective(Objective objective) {
        return objectiveRepository.save(objective);
    }

    public List<Objective> getAllObjectives() {
        return objectiveRepository.findAll();
    }
    public List<Objective> findObjectivesByUser(Long userId) {
        return objectiveRepository.findByUserId(userId);
    }


}
