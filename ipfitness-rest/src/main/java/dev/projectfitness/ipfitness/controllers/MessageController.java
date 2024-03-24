package dev.projectfitness.ipfitness.controllers;

import dev.projectfitness.ipfitness.models.dtos.ChatMessage;
import dev.projectfitness.ipfitness.models.dtos.MessageSender;
import dev.projectfitness.ipfitness.models.requests.MessageRequest;
import dev.projectfitness.ipfitness.services.MessageService;
import dev.projectfitness.ipfitness.util.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${ipfitness.base-url}")
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/messages")
    public ChatMessage sendDirectMessage(@RequestBody MessageRequest request, Authentication auth) {
        Utility.authorizeFitnessUser(auth, request.getUserFromId());
        return messageService.sendMessage(request);
    }

    @GetMapping("/users/{userId}/chats")
    public List<MessageSender> getAllMessageSendersForUser(@PathVariable("userId") Integer userId, Authentication auth) {
        Utility.authorizeFitnessUser(auth, userId);
        return messageService.getAllMessageSenders(userId);
    }

    @GetMapping("/users/{userId}/chats/{otherUserId}")
    public List<ChatMessage> getDirectMessagesForUser(@PathVariable("userId") Integer userId,
                                                      @PathVariable("otherUserId") Integer otherUserId, Authentication auth) {
        Utility.authorizeFitnessUser(auth, userId);
        return messageService.getChatMessages(userId, otherUserId);
    }

    @GetMapping("/users/{userId}/chats/discover")
    public MessageSender getSenderForUser(@PathVariable("userId") Integer userId,
                                          @RequestParam(value = "username", required = true) String username, Authentication auth) {
        Utility.authorizeFitnessUser(auth, userId);
        return messageService.getSenderByUsername(userId, username);
    }
}
