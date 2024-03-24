package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.models.dtos.ProgramComment;
import dev.projectfitness.ipfitness.models.entities.CommentEntity;
import dev.projectfitness.ipfitness.models.entities.FitnessUserEntity;
import dev.projectfitness.ipfitness.models.requests.CommentRequest;
import dev.projectfitness.ipfitness.repositories.CommentEntityRepository;
import dev.projectfitness.ipfitness.repositories.FitnessUserEntityRepository;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import dev.projectfitness.ipfitness.services.CommentService;
import dev.projectfitness.ipfitness.util.ActionMessageResolver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentEntityRepository commentEntityRepository;
    private final FitnessUserEntityRepository fitnessUserEntityRepository;
    private final ModelMapper modelMapper;
    private final ActionLoggingService actionLoggingService;

    @Override
    public ProgramComment postCommentOnProgram(Integer programId, CommentRequest request) {
        FitnessUserEntity user = fitnessUserEntityRepository.findById(request.getUserId()).orElseThrow(NotFoundException::new);
        CommentEntity entity = modelMapper.map(request, CommentEntity.class);
        entity.setCommentId(null);
        entity.setDatePosted(new Date());
        entity.setProgramId(programId);
        entity.setFitnessUser(user);
        CommentEntity newEntity = commentEntityRepository.saveAndFlush(entity);
        actionLoggingService.logMessage(ActionMessageResolver.resolveProgramCommentPost(user, newEntity));
        ProgramComment comment = modelMapper.map(newEntity, ProgramComment.class);
        modelMapper.map(user, comment);
        return comment;
    }

    @Override
    public List<ProgramComment> getAllCommentsOnProgram(Integer programId) {
        return commentEntityRepository.getAllByProgramId(programId).stream().map(e -> {
            ProgramComment comment = modelMapper.map(e, ProgramComment.class);
            comment.setFirstName(e.getFitnessUser().getFirstName());
            comment.setLastName(e.getFitnessUser().getLastName());
            comment.setAvatar(e.getFitnessUser().getAvatar());
            return comment;
        }).collect(Collectors.toList());
    }
}
