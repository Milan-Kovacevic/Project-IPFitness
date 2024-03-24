package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.models.dtos.FitnessProgram;
import dev.projectfitness.ipfitness.models.requests.FiltersRequest;
import dev.projectfitness.ipfitness.services.FitnessUserService;
import dev.projectfitness.ipfitness.util.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${ipfitness.base-url}/users")
public class UserFitnessProgramController {
    private final FitnessUserService fitnessUserService;

    @GetMapping("/{userId}/own-programs")
    public Page<FitnessProgram> getOwnFitnessProgramsForUser(@RequestParam(name = "categoryFilter", required = false, defaultValue = "") Integer categoryFilter,
                                                             @RequestParam(name = "locationFilter", required = false, defaultValue = "") String locationFilter,
                                                             @RequestParam(name = "difficultyFilter", required = false, defaultValue = "") String difficultyFilter,
                                                             @RequestParam(name = "stateFilter", required = false, defaultValue = "") String stateFilter,
                                                             @RequestParam(name = "searchQuery", required = false, defaultValue = "") String searchQuery,
                                                             @PathVariable("userId") Integer userId, Pageable page, Authentication auth) {
        Utility.authorizeFitnessUser(auth, userId);
        FiltersRequest request = Utility.parseFiltersRequest(categoryFilter, locationFilter, difficultyFilter, stateFilter, searchQuery);

        return fitnessUserService.getOwnFitnessPrograms(userId, page, request);
    }

    @GetMapping("/{userId}/purchased-programs")
    public Page<FitnessProgram> getPurchasedFitnessProgramsForUser(@RequestParam(name = "categoryFilter", required = false, defaultValue = "") Integer categoryFilter,
                                                                   @RequestParam(name = "locationFilter", required = false, defaultValue = "") String locationFilter,
                                                                   @RequestParam(name = "difficultyFilter", required = false, defaultValue = "") String difficultyFilter,
                                                                   @RequestParam(name = "stateFilter", required = false, defaultValue = "") String stateFilter,
                                                                   @RequestParam(name = "searchQuery", required = false, defaultValue = "") String searchQuery,
                                                                   @PathVariable("userId") Integer userId, Pageable page, Authentication auth) {
        Utility.authorizeFitnessUser(auth, userId);
        FiltersRequest request = Utility.parseFiltersRequest(categoryFilter, locationFilter, difficultyFilter, stateFilter, searchQuery);

        return fitnessUserService.getPurchasedFitnessPrograms(userId, page, request);
    }

}
