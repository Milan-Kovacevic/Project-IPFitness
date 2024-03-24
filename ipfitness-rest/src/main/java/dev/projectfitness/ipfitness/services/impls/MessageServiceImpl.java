package dev.projectfitness.ipfitness.services.impls;

import dev.projectfitness.ipfitness.exceptions.ForbiddenException;
import dev.projectfitness.ipfitness.exceptions.NotFoundException;
import dev.projectfitness.ipfitness.models.dtos.ChatMessage;
import dev.projectfitness.ipfitness.models.dtos.MessageSender;
import dev.projectfitness.ipfitness.models.entities.MessageEntity;
import dev.projectfitness.ipfitness.models.requests.MessageRequest;
import dev.projectfitness.ipfitness.repositories.FitnessUserEntityRepository;
import dev.projectfitness.ipfitness.repositories.MessageEntityRepository;
import dev.projectfitness.ipfitness.services.ActionLoggingService;
import dev.projectfitness.ipfitness.services.MessageService;
import dev.projectfitness.ipfitness.util.ActionMessageResolver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageEntityRepository messageEntityRepository;
    private final FitnessUserEntityRepository fitnessUserEntityRepository;
    private final ModelMapper modelMapper;
    private final ActionLoggingService actionLoggingService;
    private static final String INITIAL_MESSAGE = "Hi there!";

    @Override
    public ChatMessage sendMessage(MessageRequest request) {
        if (!fitnessUserEntityRepository.existsById(request.getUserToId()))
            throw new NotFoundException();
        MessageEntity entity = modelMapper.map(request, MessageEntity.class);
        entity.setTimeSent(new Date());
        MessageEntity newMessage = messageEntityRepository.saveAndFlush(entity);
        actionLoggingService.logMessage(ActionMessageResolver.resolveDirectMessageSend(newMessage, false));
        return modelMapper.map(newMessage, ChatMessage.class);
    }

    @Override
    public List<ChatMessage> getChatMessages(Integer userId, Integer otherUserId) {
        return messageEntityRepository.getAllUserChatMessages(userId, otherUserId).stream().map(m ->
                modelMapper.map(m, ChatMessage.class)).toList();
    }

    @Override
    public List<MessageSender> getAllMessageSenders(Integer userId) {
        return messageEntityRepository
                .getAllSendersForUser(userId)
                .stream()
                .map(e -> modelMapper.map(e, MessageSender.class))
                .collect(Collectors.toList());
    }

    @Override
    public MessageSender getSenderByUsername(Integer senderId, String username) {
        MessageSender msgSender = fitnessUserEntityRepository
                .findByUsername(username)
                .map(e -> modelMapper.map(e, MessageSender.class))
                .orElseThrow(NotFoundException::new);
        if(msgSender.getUserId().equals(senderId))
            throw new ForbiddenException();

        // Sending initial Hi message...
        MessageEntity entity = new MessageEntity();
        entity.setTimeSent(new Date());
        entity.setContent(INITIAL_MESSAGE);
        entity.setUserFromId(senderId);
        entity.setUserToId(msgSender.getUserId());
        messageEntityRepository.saveAndFlush(entity);
        actionLoggingService.logSensitiveAction(ActionMessageResolver.resolveDirectMessageSend(entity, true));
        return msgSender;
    }
}
