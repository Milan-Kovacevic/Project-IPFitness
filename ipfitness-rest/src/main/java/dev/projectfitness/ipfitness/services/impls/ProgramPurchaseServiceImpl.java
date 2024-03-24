package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.exceptions.ForbiddenException;
import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.models.dtos.ProgramPurchaseItem;
import dev.projectfitness.ipfitness.models.entities.FitnessProgramEntity;
import dev.projectfitness.ipfitness.models.entities.ProgramPurchaseEntity;
import dev.projectfitness.ipfitness.models.requests.ProgramPurchaseRequest;
import dev.projectfitness.ipfitness.repositories.FitnessProgramEntityRepository;
import dev.projectfitness.ipfitness.repositories.ProgramPurchaseEntityRepository;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import dev.projectfitness.ipfitness.services.ProgramPurchaseService;
import dev.projectfitness.ipfitness.util.ActionMessageResolver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramPurchaseServiceImpl implements ProgramPurchaseService {
    private final ProgramPurchaseEntityRepository programPurchaseEntityRepository;
    private final FitnessProgramEntityRepository fitnessProgramEntityRepository;
    private final ModelMapper modelMapper;
    private final ActionLoggingService actionLoggingService;

    @Override
    public void purchaseFitnessProgram(Integer programId, ProgramPurchaseRequest request) {
        if (programPurchaseEntityRepository.existsByFitnessProgramProgramIdAndUserId(programId, request.getUserId()))
            return;

        FitnessProgramEntity fitnessProgram = fitnessProgramEntityRepository.findById(programId).orElseThrow(NotFoundException::new);
        if (Objects.equals(fitnessProgram.getFitnessUser().getUserId(), request.getUserId()))
            throw new ForbiddenException();

        ProgramPurchaseEntity entity = modelMapper.map(request, ProgramPurchaseEntity.class);
        entity.setFitnessProgram(fitnessProgram);
        entity.setPrice(fitnessProgram.getPrice());
        entity.setPurchaseId(null);
        entity.setPurchaseDate(new Date());

        programPurchaseEntityRepository.saveAndFlush(entity);
        actionLoggingService.logSensitiveAction(ActionMessageResolver.resolveFitnessProgramPurchase(entity));
    }

    @Override
    public List<ProgramPurchaseItem> getProgramPurchaseItemsForUser(Integer userId) {
        return programPurchaseEntityRepository.findAllByUserId(userId).stream().map(e -> {
            ProgramPurchaseItem item = modelMapper.map(e, ProgramPurchaseItem.class);
            modelMapper.map(e.getFitnessProgram(), item);
            item.setOwnerFirstName(e.getFitnessProgram().getFitnessUser().getFirstName());
            item.setOwnerLastName(e.getFitnessProgram().getFitnessUser().getLastName());
            item.setPrice(e.getPrice());
            if (e.getFitnessProgram().getPictures() != null && e.getFitnessProgram().getPictures().size() > 0)
                item.setProgramPicture(e.getFitnessProgram().getPictures().get(0).getPictureLocation());
            return item;
        }).collect(Collectors.toList());
    }

}
