package dev.projectfitness.ipfitness.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "program_picture")
public class ProgramPictureEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "PictureId", nullable = false)
    private Integer pictureId;
    @Basic
    @Column(name = "PictureLocation", nullable = false, length = 256)
    private String pictureLocation;
    @Basic
    @Column(name = "OrderNumber", nullable = false)
    private Integer orderNumber;
    @Basic
    @Column(name = "ProgramId", nullable = false)
    private Integer programId;

}
