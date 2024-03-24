package dev.projectfitness.ipfitness.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.models.dtos.DailyExercises;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ExerciseRepository {
    private final ObjectMapper objectMapper;

    @Value("${ipfitness.exercises.location}")
    private String exercisesStoragePath;
    private static final String FILE_NAME = "daily_exercises.json";
    private Path exercisesPath;

    @PostConstruct
    private void postConstruct() {
        exercisesPath = Paths.get(exercisesStoragePath).normalize().toAbsolutePath();
        File folder = exercisesPath.toFile();
        if (!folder.exists()) {
            try {
                Files.createDirectories(exercisesPath);
            } catch (IOException e) {
                // TODO: Log
            }
        }
    }

    public Optional<DailyExercises> getAllExercises() {
        File file = Paths.get(String.valueOf(exercisesPath), FILE_NAME).toFile();

        if (!file.exists()) {
            return Optional.empty();
        }

        try {
            DailyExercises dailyExercises = objectMapper.readValue(file, DailyExercises.class);
            return Optional.ofNullable(dailyExercises);

        } catch (Exception e) {
            throw new NotFoundException();
            // TODO: Log
        }
    }

    public void saveExercises(DailyExercises dailyExercises) {
        File file = Paths.get(String.valueOf(exercisesPath), FILE_NAME).toFile();

        try {
            objectMapper.writeValue(file, dailyExercises);
        } catch (IOException e) {
            // TODO: Log
        }
    }
}
