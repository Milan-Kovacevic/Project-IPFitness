package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.models.entities.QuestionEntity;
import dev.projectfitness.ipfitness.models.requests.QuestionRequest;
import dev.projectfitness.ipfitness.repositories.QuestionEntityRepository;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import dev.projectfitness.ipfitness.services.QuestionService;
import dev.projectfitness.ipfitness.util.ActionMessageResolver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionEntityRepository questionEntityRepository;
    private final ModelMapper modelMapper;
    private final ActionLoggingService actionLoggingService;

    @Override
    public void sendQuestion(QuestionRequest request) {
        QuestionEntity entity = modelMapper.map(request, QuestionEntity.class);
        entity.setSendDate(new Date());
        entity.setIsRead(false);
        entity.setQuestionId(null);
        questionEntityRepository.saveAndFlush(entity);
        actionLoggingService.logMessage(ActionMessageResolver.resolveQuestionSend(entity));
    }
}
