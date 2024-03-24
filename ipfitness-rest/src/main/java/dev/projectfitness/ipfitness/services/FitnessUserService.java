package dev.projectfitness.ipfitness.services;

import dev.projectfitness.ipfitness.models.dtos.FitnessProgram;
import dev.projectfitness.ipfitness.models.requests.FiltersRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FitnessUserService {
    Page<FitnessProgram> getOwnFitnessPrograms(Integer userId, Pageable page, FiltersRequest request);

    Page<FitnessProgram> getPurchasedFitnessPrograms(Integer userId, Pageable page, FiltersRequest request);
}
