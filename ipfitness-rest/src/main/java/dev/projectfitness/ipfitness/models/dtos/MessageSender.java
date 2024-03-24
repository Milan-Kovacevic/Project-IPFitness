package dev.projectfitness.ipfitness.models.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class MessageSender {
    private Integer userId;
    private String username;
    private String firstName;
    private String lastName;
    private String avatar;
}
