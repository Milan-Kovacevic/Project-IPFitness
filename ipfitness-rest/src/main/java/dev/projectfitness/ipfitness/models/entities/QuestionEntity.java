package dev.projectfitness.ipfitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "question")
public class QuestionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "QuestionId", nullable = false)
    private Integer questionId;
    @Basic
    @Column(name = "UserId", nullable = false)
    private Integer userId;
    @Basic
    @Column(name = "IsRead", nullable = false)
    private Boolean isRead;
    @Basic
    @Column(name = "Content", nullable = false, length = 512)
    private String content;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SendDate", nullable = false)
    private Date sendDate;

}
