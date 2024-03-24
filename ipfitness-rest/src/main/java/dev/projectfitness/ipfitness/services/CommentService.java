package dev.projectfitness.ipfitness.services;

import dev.projectfitness.ipfitness.models.dtos.ProgramComment;
import dev.projectfitness.ipfitness.models.requests.CommentRequest;

import java.util.List;

public interface CommentService {
    ProgramComment postCommentOnProgram(Integer programId, CommentRequest request);

    List<ProgramComment> getAllCommentsOnProgram(Integer programId);
}
