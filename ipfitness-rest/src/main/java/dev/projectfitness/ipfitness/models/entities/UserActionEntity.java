package dev.projectfitness.ipfitness.models.entities;

import dev.projectfitness.ipfitness.models.enums.ActionSeverity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "user_action")
public class UserActionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ActionId", nullable = false)
    private Integer actionId;
    @Basic
    @Column(name = "IpAddress", nullable = false)
    private String ipAddress;
    @Basic
    @Column(name = "Endpoint", nullable = false)
    private String endpoint;
    @Basic
    @Column(name = "Message", nullable = false)
    private String message;
    @Basic
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "Severity", nullable = false)
    private ActionSeverity severity;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ActionTime", nullable = false)
    private Date actionTime;
}
