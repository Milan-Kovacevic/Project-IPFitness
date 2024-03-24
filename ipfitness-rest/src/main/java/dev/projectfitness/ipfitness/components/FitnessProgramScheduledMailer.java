package dev.projectfitness.ipfitness.components;

import dev.projectfitness.ipfitness.models.dtos.ProgramMailItem;
import dev.projectfitness.ipfitness.models.entities.CategoryEntity;
import dev.projectfitness.ipfitness.models.entities.FitnessProgramEntity;
import dev.projectfitness.ipfitness.repositories.CategoryEntityRepository;
import dev.projectfitness.ipfitness.repositories.FitnessProgramEntityRepository;
import dev.projectfitness.ipfitness.services.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FitnessProgramScheduledMailer {
    private final EmailSenderService emailSenderService;
    private final CategoryEntityRepository categoryEntityRepository;
    private final FitnessProgramEntityRepository fitnessProgramEntityRepository;
    public static final Integer SCHEDULE_INTERVAL = 86_400_000; // 86_400_000[ms] = 1 day

    @Scheduled(fixedRate = 86_400_000)
    public void sendNewFitnessProgramsToSubscribedUsers() {
        for (CategoryEntity category : categoryEntityRepository.findAll()) {
            List<ProgramMailItem> newPrograms = fitnessProgramEntityRepository
                    .findAllProgramsCreatedTodayForCategory(category.getCategoryId())
                    .stream()
                    .map(p -> new ProgramMailItem(p.getName(), p.getFitnessUser().getFirstName() + " " + p.getFitnessUser().getLastName()))
                    .toList();
            if (newPrograms.isEmpty())
                continue;
            List<String> recipients = new ArrayList<>();
            category.getFitnessUsers().forEach(user -> {
                recipients.add(user.getMail());
            });
            if (!recipients.isEmpty())
                emailSenderService.sendFitnessProgramsSuggestions(newPrograms, recipients, category.getName());
        }

    }
}
