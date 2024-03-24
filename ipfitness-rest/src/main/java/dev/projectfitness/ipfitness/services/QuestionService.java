package dev.projectfitness.ipfitness.services;

import dev.projectfitness.ipfitness.models.requests.QuestionRequest;

public interface QuestionService {

    void sendQuestion(QuestionRequest request);
}
