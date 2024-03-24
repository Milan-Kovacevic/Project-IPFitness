package dev.projectfitness.ipfitness.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "program_demonstration")
public class ProgramDemonstrationEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "DemonstrationId", nullable = false)
    private Integer demonstrationId;
    @Basic
    @Column(name = "VideoUrl", nullable = false, length = 256)
    private String videoUrl;
    @Basic
    @Column(name = "OrderNumber", nullable = false)
    private Integer orderNumber;
    @Basic
    @Column(name = "ProgramId", nullable = false)
    private Integer programId;

}
