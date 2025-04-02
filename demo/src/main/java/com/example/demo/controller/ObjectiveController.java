package com.example.demo.controller;

import com.example.demo.model.Objective;
import com.example.demo.service.ObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/objectives")
public class ObjectiveController {

    @Autowired
    private ObjectiveService objectiveService;

    @PostMapping("/create")
    public ResponseEntity<Objective> createObjective(@RequestBody Objective objective) {
        Objective createdObjective = objectiveService.createObjective(objective);
        return ResponseEntity.ok(createdObjective);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Objective>> getAllObjectives() {
        return ResponseEntity.ok(objectiveService.getAllObjectives());
    }
}