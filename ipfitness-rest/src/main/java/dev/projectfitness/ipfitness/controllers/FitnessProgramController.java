package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.models.dtos.FitnessProgram;
import dev.projectfitness.ipfitness.models.dtos.FitnessProgramDetails;
import dev.projectfitness.ipfitness.models.dtos.SingleFitnessProgram;
import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.ProgramState;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import dev.projectfitness.ipfitness.models.requests.FiltersRequest;
import dev.projectfitness.ipfitness.models.requests.FitnessProgramAddRequest;
import dev.projectfitness.ipfitness.models.requests.ProgramInformationUpdateRequest;
import dev.projectfitness.ipfitness.security.models.JwtUser;
import dev.projectfitness.ipfitness.services.FitnessProgramService;
import dev.projectfitness.ipfitness.util.Utility;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${ipfitness.base-url}/programs")
@RequiredArgsConstructor
public class FitnessProgramController {
    private final FitnessProgramService fitnessProgramService;

    @GetMapping
    public Page<FitnessProgram> getAllFitnessPrograms(@RequestParam(name = "categoryFilter", required = false, defaultValue = "") Integer categoryFilter,
                                                      @RequestParam(name = "locationFilter", required = false, defaultValue = "") String locationFilter,
                                                      @RequestParam(name = "difficultyFilter", required = false, defaultValue = "") String difficultyFilter,
                                                      @RequestParam(name = "stateFilter", required = false, defaultValue = "") String stateFilter,
                                                      @RequestParam(name = "searchQuery", required = false, defaultValue = "") String searchQuery,
                                                      Pageable page) {
        FiltersRequest request = Utility.parseFiltersRequest(categoryFilter, locationFilter, difficultyFilter, stateFilter, searchQuery);
        return fitnessProgramService.getAllFitnessPrograms(page, request);
    }

    @GetMapping("/{programId}")
    public SingleFitnessProgram getFitnessProgram(@PathVariable("programId") Integer programId) {
        return fitnessProgramService.getFitnessProgram(programId);
    }

    @GetMapping("/{programId}/details")
    public FitnessProgramDetails getFitnessProgramDetails(@PathVariable("programId") Integer programId) {
        return fitnessProgramService.getFitnessProgramDetails(programId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FitnessProgram addFitnessProgram(@ModelAttribute @Valid FitnessProgramAddRequest request, Authentication auth) {
        Utility.authorizeFitnessUser(auth, request.getUserId());
        return this.fitnessProgramService.addFitnessProgram(request);
    }

    @PutMapping("/{programId}/info")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProgramInformation(@PathVariable("programId") Integer programId,
                                         @RequestBody ProgramInformationUpdateRequest request, Authentication auth) {
        Utility.authorizeFitnessUser(auth, request.getUserId());
        this.fitnessProgramService.updateFitnessProgramInformation(programId, request);
    }

    @PutMapping("/{programId}/pictures")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProgramPictures(@PathVariable("programId") Integer programId, @ModelAttribute MultipartFile[] pictures) {
        this.fitnessProgramService.updateFitnessProgramPictures(programId, pictures);
    }

    @PutMapping("/{programId}/videos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProgramDemonstrations(@PathVariable("programId") Integer programId, @RequestBody List<String> videos) {
        this.fitnessProgramService.updateFitnessProgramDemonstrations(programId, videos);
    }

    @GetMapping("/{programId}/videos")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getProgramDemonstrations(@PathVariable("programId") Integer programId) {
        return this.fitnessProgramService.getFitnessProgramDemonstrations(programId);
    }

    @DeleteMapping("/{programId}")
    public void deleteFitnessProgram(@PathVariable("programId") Integer programId) {
        this.fitnessProgramService.deleteFitnessProgram(programId);
    }
}
