package dev.projectfitness.ipfitness.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

import static dev.projectfitness.ipfitness.util.Constants.DATE_TIME_FORMAT;

@Data
public class ProgramComment {
    private Integer commentId;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private Date datePosted;
    private String firstName;
    private String lastName;
    private String avatar;
}
