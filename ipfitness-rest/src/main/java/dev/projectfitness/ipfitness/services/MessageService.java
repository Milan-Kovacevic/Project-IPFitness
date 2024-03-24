package dev.projectfitness.ipfitness.services;

import dev.projectfitness.ipfitness.models.dtos.ChatMessage;
import dev.projectfitness.ipfitness.models.dtos.MessageSender;
import dev.projectfitness.ipfitness.models.requests.MessageRequest;

import java.util.List;

public interface MessageService {
    ChatMessage sendMessage(MessageRequest request);

    List<ChatMessage> getChatMessages(Integer userId, Integer otherUserId);

    List<MessageSender> getAllMessageSenders(Integer userId);

    MessageSender getSenderByUsername(Integer senderId, String username);
}
