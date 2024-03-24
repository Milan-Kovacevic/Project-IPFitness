package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.models.dtos.ProgramPurchaseItem;
import dev.projectfitness.ipfitness.models.requests.ProgramPurchaseRequest;
import dev.projectfitness.ipfitness.services.ProgramPurchaseService;
import dev.projectfitness.ipfitness.util.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${ipfitness.base-url}")
@RequiredArgsConstructor
public class ProgramPurchaseController {
    private final ProgramPurchaseService programPurchaseService;

    @PostMapping("/programs/{programId}/purchase")
    public void purchaseFitnessProgram(@PathVariable("programId") Integer programId,
                                       @RequestBody ProgramPurchaseRequest request, Authentication auth) {
        Utility.authorizeFitnessUser(auth, request.getUserId());
        programPurchaseService.purchaseFitnessProgram(programId, request);
    }

    @GetMapping("/users/{userId}/purchases")
    public List<ProgramPurchaseItem> getPurchaseHistoryForUser(@PathVariable("userId") Integer userId, Authentication auth) {
        Utility.authorizeFitnessUser(auth, userId);
        return programPurchaseService.getProgramPurchaseItemsForUser(userId);
    }
}
