package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.models.dtos.ProgramComment;
import dev.projectfitness.ipfitness.models.requests.CommentRequest;
import dev.projectfitness.ipfitness.services.CommentService;
import dev.projectfitness.ipfitness.util.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${ipfitness.base-url}")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/programs/{programId}/comments")
    public ProgramComment sendCommentOnProgram(@PathVariable("programId") Integer programId,
                                               @RequestBody CommentRequest request, Authentication auth) {
        Utility.authorizeFitnessUser(auth, request.getUserId());
        return commentService.postCommentOnProgram(programId, request);
    }
    
    @GetMapping("/programs/{programId}/comments")
    public List<ProgramComment> getCommentsOnProgram(@PathVariable("programId") Integer programId){
        return commentService.getAllCommentsOnProgram(programId);
    }
}
