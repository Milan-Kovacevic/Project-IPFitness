package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.models.requests.QuestionRequest;
import dev.projectfitness.ipfitness.services.QuestionService;
import dev.projectfitness.ipfitness.util.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${ipfitness.base-url}/questions")
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping
    public void sendQuestionToAdvisor(@RequestBody QuestionRequest request, Authentication auth) {
        Utility.authorizeFitnessUser(auth, request.getUserId());
        questionService.sendQuestion(request);
    }
}
