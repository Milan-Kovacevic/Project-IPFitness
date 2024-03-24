package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.models.dtos.FitnessProgram;
import dev.projectfitness.ipfitness.models.entities.CategoryEntity;
import dev.projectfitness.ipfitness.models.entities.FitnessProgramEntity;
import dev.projectfitness.ipfitness.models.entities.ProgramPictureEntity;
import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.ProgramState;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import dev.projectfitness.ipfitness.models.requests.FiltersRequest;
import dev.projectfitness.ipfitness.repositories.*;
import dev.projectfitness.ipfitness.services.FitnessUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FitnessUserServiceImpl implements FitnessUserService {
    private final FitnessProgramEntityRepository fitnessProgramEntityRepository;
    private final CategoryEntityRepository categoryEntityRepository;
    private final ProgramPictureEntityRepository programPictureEntityRepository;
    private final ProgramPurchaseEntityRepository programPurchaseEntityRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<FitnessProgram> getOwnFitnessPrograms(Integer userId, Pageable page, FiltersRequest request) {
        return getFitnessProgramsForUser(userId, page, request.getCategoryFilter(),
                request.getLocation(), request.getDifficulty(), request.getState(), request.getSearchQuery());
    }

    @Override
    public Page<FitnessProgram> getPurchasedFitnessPrograms(Integer userId, Pageable page, FiltersRequest request) {
        return getPurchasedFitnessProgramsForUser(userId, page, request.getCategoryFilter(),
                request.getLocation(), request.getDifficulty(), request.getState(), request.getSearchQuery());
    }

    private Page<FitnessProgram> getFitnessProgramsForUser(Integer userId, Pageable page, Integer categoryFilter, TrainingLocation location,
                                                           ProgramDifficulty difficulty, ProgramState state, String searchQuery) {
        if (ProgramState.ACTIVE.equals(state))
            return fitnessProgramEntityRepository
                    .findAllFitnessUserProgramsFilteredAndActive(userId, page, categoryFilter, location, difficulty, searchQuery)
                    .map(this::mapEntityToDto);
        else if (ProgramState.UPCOMING.equals(state)) {
            return fitnessProgramEntityRepository
                    .findAllFitnessUserProgramsFilteredAndUpcoming(userId, page, categoryFilter, location, difficulty, searchQuery)
                    .map(this::mapEntityToDto);
        } else if (ProgramState.FINISHED.equals(state)) {
            return fitnessProgramEntityRepository
                    .findAllFitnessUserProgramsFilteredAndFinished(userId, page, categoryFilter, location, difficulty, searchQuery)
                    .map(this::mapEntityToDto);
        } else {
            return fitnessProgramEntityRepository
                    .findAllFitnessUserProgramsFiltered(userId, page, categoryFilter, location, difficulty, searchQuery)
                    .map(this::mapEntityToDto);
        }
    }

    private Page<FitnessProgram> getPurchasedFitnessProgramsForUser(Integer userId, Pageable page, Integer categoryFilter, TrainingLocation location,
                                                                    ProgramDifficulty difficulty, ProgramState state, String searchQuery) {

        if (ProgramState.ACTIVE.equals(state))
            return programPurchaseEntityRepository
                    .findAllPurchasedProgramsFilteredAndActive(userId, page, categoryFilter, location, difficulty, searchQuery)
                    .map(this::mapEntityToDto);
        else if (ProgramState.UPCOMING.equals(state)) {
            return programPurchaseEntityRepository
                    .findAllPurchasedProgramsFilteredAndUpcoming(userId, page, categoryFilter, location, difficulty, searchQuery)
                    .map(this::mapEntityToDto);
        } else if (ProgramState.FINISHED.equals(state)) {
            return programPurchaseEntityRepository
                    .findAllPurchasedProgramsFilteredAndFinished(userId, page, categoryFilter, location, difficulty, searchQuery)
                    .map(this::mapEntityToDto);
        } else {
            return programPurchaseEntityRepository
                    .findAllPurchasedProgramsFiltered(userId, page, categoryFilter, location, difficulty, searchQuery)
                    .map(this::mapEntityToDto);
        }
    }

    private FitnessProgram mapEntityToDto(FitnessProgramEntity entity) {
        FitnessProgram program = modelMapper.map(entity, FitnessProgram.class);
        if (entity.getCategoryId() != null) {
            CategoryEntity category = categoryEntityRepository.findById(entity.getCategoryId()).orElseThrow(NotFoundException::new);
            program.setCategoryName(category.getName());
        }
        program.setUserId(entity.getFitnessUser().getUserId());
        program.setCreatedBy(entity.getFitnessUser().getFirstName() + " " + entity.getFitnessUser().getLastName());
        List<String> images = new ArrayList<>();
        List<ProgramPictureEntity> pictureEntities = programPictureEntityRepository.findByProgramIdOrderByOrderNumberAsc(entity.getProgramId());
        pictureEntities.forEach((p) -> {
            images.add(p.getPictureLocation());
        });
        program.setImages(images);
        return program;
    }
}
