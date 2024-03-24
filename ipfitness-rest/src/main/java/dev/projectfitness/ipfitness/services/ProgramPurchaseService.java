package dev.projectfitness.ipfitness.services;

import dev.projectfitness.ipfitness.models.dtos.ProgramPurchaseItem;
import dev.projectfitness.ipfitness.models.requests.ProgramPurchaseRequest;

import java.util.List;

public interface ProgramPurchaseService {

    void purchaseFitnessProgram(Integer programId, ProgramPurchaseRequest request);

    List<ProgramPurchaseItem> getProgramPurchaseItemsForUser(Integer userId);
}
