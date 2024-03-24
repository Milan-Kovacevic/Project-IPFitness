package dev.projectfitness.ipfitness.repositories;

import dev.projectfitness.ipfitness.models.entities.FitnessProgramEntity;
import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.ProgramState;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface FitnessProgramEntityRepository extends JpaRepository<FitnessProgramEntity, Integer> {

    @Query("SELECT p FROM FitnessProgramEntity p WHERE p.programId=?1 AND p.deleted=false")
    Optional<FitnessProgramEntity> getByIdAndNotDeleted(Integer programId);

    // Pageable queries for all fitness programs
    @Query("SELECT p FROM FitnessProgramEntity p WHERE (:categoryFilter IS NULL OR p.categoryId = :categoryFilter) AND (:locationFilter IS NULL OR p.location = :locationFilter) " +
            "and (:difficultyFilter IS NULL OR p.difficulty = :difficultyFilter) AND p.name LIKE %:searchQuery% AND p.deleted=false")
    Page<FitnessProgramEntity> findAllProgramsFiltered(Pageable page, Integer categoryFilter, TrainingLocation locationFilter, ProgramDifficulty difficultyFilter, String searchQuery);

    @Query("SELECT p FROM FitnessProgramEntity p WHERE (p.startDate <= current_date AND p.endDate > current_date) AND (:categoryFilter IS NULL OR p.categoryId = :categoryFilter) " +
            "AND (:locationFilter IS NULL OR p.location = :locationFilter) and (:difficultyFilter IS NULL OR p.difficulty = :difficultyFilter) AND p.name LIKE %:searchQuery% AND p.deleted=false")
    Page<FitnessProgramEntity> findAllProgramsFilteredAndActive(Pageable page, Integer categoryFilter, TrainingLocation locationFilter, ProgramDifficulty difficultyFilter, String searchQuery);

    @Query("SELECT p FROM FitnessProgramEntity p WHERE (p.startDate > current_date) AND (:categoryFilter IS NULL OR p.categoryId = :categoryFilter) " +
            "AND (:locationFilter IS NULL OR p.location = :locationFilter) and (:difficultyFilter IS NULL OR p.difficulty = :difficultyFilter) AND p.name LIKE %:searchQuery% AND p.deleted=false")
    Page<FitnessProgramEntity> findAllProgramsFilteredAndUpcoming(Pageable page, Integer categoryFilter, TrainingLocation locationFilter, ProgramDifficulty difficultyFilter, String searchQuery);

    @Query("SELECT p FROM FitnessProgramEntity p WHERE (p.endDate < current_date) AND (:categoryFilter IS NULL OR p.categoryId = :categoryFilter) " +
            "AND (:locationFilter IS NULL OR p.location = :locationFilter) and (:difficultyFilter IS NULL OR p.difficulty = :difficultyFilter) AND p.name LIKE %:searchQuery% AND p.deleted=false")
    Page<FitnessProgramEntity> findAllProgramsFilteredAndFinished(Pageable page, Integer categoryFilter, TrainingLocation locationFilter, ProgramDifficulty difficultyFilter, String searchQuery);


    // Pageable queries for user fitness programs
    @Query("SELECT p FROM FitnessProgramEntity p WHERE (p.fitnessUser.userId = :userId) AND (:categoryFilter IS NULL OR p.categoryId = :categoryFilter) AND (:locationFilter IS NULL OR p.location = :locationFilter) " +
            "and (:difficultyFilter IS NULL OR p.difficulty = :difficultyFilter) AND p.name LIKE %:searchQuery% AND p.deleted=false")
    Page<FitnessProgramEntity> findAllFitnessUserProgramsFiltered(Integer userId, Pageable page, Integer categoryFilter, TrainingLocation locationFilter, ProgramDifficulty difficultyFilter, String searchQuery);

    @Query("SELECT p FROM FitnessProgramEntity p WHERE (p.fitnessUser.userId = :userId) AND (p.startDate <= current_date AND p.endDate > current_date) AND (:categoryFilter IS NULL OR p.categoryId = :categoryFilter) " +
            "AND (:locationFilter IS NULL OR p.location = :locationFilter) and (:difficultyFilter IS NULL OR p.difficulty = :difficultyFilter) AND p.name LIKE %:searchQuery% AND p.deleted=false")
    Page<FitnessProgramEntity> findAllFitnessUserProgramsFilteredAndActive(Integer userId, Pageable page, Integer categoryFilter, TrainingLocation locationFilter, ProgramDifficulty difficultyFilter, String searchQuery);

    @Query("SELECT p FROM FitnessProgramEntity p WHERE (p.fitnessUser.userId = :userId) AND (p.startDate > current_date) AND (:categoryFilter IS NULL OR p.categoryId = :categoryFilter) AND (:locationFilter IS NULL OR p.location = :locationFilter) " +
            "AND (:difficultyFilter IS NULL OR p.difficulty = :difficultyFilter) AND p.name LIKE %:searchQuery% AND p.deleted=false")
    Page<FitnessProgramEntity> findAllFitnessUserProgramsFilteredAndUpcoming(Integer userId, Pageable page, Integer categoryFilter, TrainingLocation locationFilter, ProgramDifficulty difficultyFilter, String searchQuery);

    @Query("SELECT p FROM FitnessProgramEntity p WHERE (p.fitnessUser.userId = :userId) AND (p.endDate < current_date) AND (:categoryFilter IS NULL OR p.categoryId = :categoryFilter) " +
            "AND (:locationFilter IS NULL OR p.location = :locationFilter) and (:difficultyFilter IS NULL OR p.difficulty = :difficultyFilter) AND p.name LIKE %:searchQuery% AND p.deleted=false")
    Page<FitnessProgramEntity> findAllFitnessUserProgramsFilteredAndFinished(Integer userId, Pageable page, Integer categoryFilter, TrainingLocation locationFilter, ProgramDifficulty difficultyFilter, String searchQuery);

    @Query("SELECT p FROM FitnessProgramEntity p WHERE p.createdAt >= current_date AND p.categoryId=:categoryId")
    List<FitnessProgramEntity> findAllProgramsCreatedTodayForCategory(Integer categoryId);
}
