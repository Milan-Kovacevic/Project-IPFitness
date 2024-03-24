package dev.projectfitness.ipfitness.services;

import dev.projectfitness.ipfitness.models.dtos.FitnessProgram;
import dev.projectfitness.ipfitness.models.dtos.FitnessProgramDetails;
import dev.projectfitness.ipfitness.models.dtos.SingleFitnessProgram;
import dev.projectfitness.ipfitness.models.requests.FiltersRequest;
import dev.projectfitness.ipfitness.models.requests.FitnessProgramAddRequest;
import dev.projectfitness.ipfitness.models.requests.FitnessProgramRequest;
import dev.projectfitness.ipfitness.models.requests.ProgramInformationUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FitnessProgramService {
    Page<FitnessProgram> getAllFitnessPrograms(Pageable page, FiltersRequest request);

    SingleFitnessProgram getFitnessProgram(Integer programId);

    FitnessProgramDetails getFitnessProgramDetails(Integer programId);

    FitnessProgram addFitnessProgram(FitnessProgramAddRequest request);

    void deleteFitnessProgram(Integer programId);

    void updateFitnessProgramInformation(Integer programId, ProgramInformationUpdateRequest request);

    void updateFitnessProgramPictures(Integer programId, MultipartFile[] pictures);

    void updateFitnessProgramDemonstrations(Integer programId, List<String> videos);

    List<String> getFitnessProgramDemonstrations(Integer programId);
}
