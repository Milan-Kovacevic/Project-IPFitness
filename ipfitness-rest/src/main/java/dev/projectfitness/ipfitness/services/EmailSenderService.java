package dev.projectfitness.ipfitness.services;

import dev.projectfitness.ipfitness.models.dtos.AttachmentEmail;
import dev.projectfitness.ipfitness.models.dtos.ProgramMailItem;

import java.util.List;

public interface EmailSenderService {

    void sendEmailWithAttachment(AttachmentEmail email);

    void sendVerificationEmail(String sendTo, String tokenId);

    void sendFitnessProgramsSuggestions(List<ProgramMailItem> programs, List<String> sendTo, String categoryName);
}
