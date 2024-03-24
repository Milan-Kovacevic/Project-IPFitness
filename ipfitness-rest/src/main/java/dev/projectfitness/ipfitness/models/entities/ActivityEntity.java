package dev.projectfitness.ipfitness.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "activity")
public class ActivityEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ActivityId", nullable = false)
    private Integer activityId;
    @Basic
    @Column(name = "UserId", nullable = false)
    private Integer userId;
    @Basic
    @Column(name = "ActivityType", nullable = false, length = 128)
    private String activityType;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DatePosted", nullable = false)
    private Date datePosted;
    @Basic
    @Column(name = "TrainingDuration", nullable = false)
    private Integer trainingDuration;
    @Basic
    @Column(name = "PercentageCompleted", nullable = false)
    private Integer percentageCompleted;
    @Basic
    @Column(name = "Result", nullable = false, precision = 3)
    private Double result;
    @Basic
    @Column(name="Summary", nullable = true, length = 512)
    private String summary;
}
