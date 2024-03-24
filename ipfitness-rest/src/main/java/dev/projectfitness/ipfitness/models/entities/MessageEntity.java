package dev.projectfitness.ipfitness.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "message")
public class MessageEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "MessageId", nullable = false)
    private Integer messageId;
    @Basic
    @Column(name = "Content", nullable = false, length = 512)
    private String content;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TimeSent", nullable = false)
    private Date timeSent;
    @Basic
    @Column(name = "UserFromId", nullable = false)
    private Integer userFromId;
    @Basic
    @Column(name = "UserToId", nullable = false)
    private Integer userToId;

}
