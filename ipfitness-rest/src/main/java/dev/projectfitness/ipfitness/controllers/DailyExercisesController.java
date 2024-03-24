package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.models.dtos.DailyExercises;
import dev.projectfitness.ipfitness.services.ExerciseService;
import dev.projectfitness.ipfitness.util.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${ipfitness.base-url}/exercises")
public class DailyExercisesController {
    private final ExerciseService exerciseService;

    @GetMapping
    public DailyExercises getDailyExercises() {
        return exerciseService.getDailyExercises();
    }
}
