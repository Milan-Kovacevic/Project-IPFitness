package dev.projectfitness.ipfitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "comment")
public class CommentEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CommentId", nullable = false)
    private Integer commentId;
    @Basic
    @Column(name = "Content", nullable = false, length = 512)
    private String content;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DatePosted", nullable = false)
    private Date datePosted;
    @Basic
    @Column(name = "ProgramId", nullable = false)
    private Integer programId;
    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "UserId", nullable = false)
    private FitnessUserEntity fitnessUser;

}
