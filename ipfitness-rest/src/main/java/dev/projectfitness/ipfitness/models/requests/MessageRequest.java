package dev.projectfitness.ipfitness.models.requests;

import lombok.Data;

@Data
public class MessageRequest {
    private String content;
    private Integer userFromId;
    private Integer userToId;
}
