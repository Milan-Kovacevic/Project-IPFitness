package dev.projectfitness.ipfitness.models.entities;

import dev.projectfitness.ipfitness.models.enums.ProgramDifficulty;
import dev.projectfitness.ipfitness.models.enums.TrainingLocation;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "fitness_program")
public class FitnessProgramEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ProgramId", nullable = false)
    private Integer programId;
    @Basic
    @Column(name = "Name", nullable = false, length = 128)
    private String name;
    @Basic
    @Column(name = "Description", nullable = false, length = 512)
    private String description;
    @Basic
    @Column(name = "Price", nullable = false, precision = 2)
    private Double price;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "Difficulty", nullable = false)
    private ProgramDifficulty difficulty;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "StartDate", nullable = false)
    private Date startDate;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EndDate", nullable = false)
    private Date endDate;
    @Basic
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "Location", nullable = false)
    private TrainingLocation location;
    @Basic
    @Column(name = "Deleted", nullable = false)
    private Boolean deleted;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedAt", nullable = false)
    private Date createdAt;
    @Basic
    @Column(name = "CategoryId", nullable = true)
    private Integer categoryId;
    @OneToMany(mappedBy = "programId")
    private List<CommentEntity> comments;
    @ManyToOne
    @JoinColumn(name = "UserId", referencedColumnName = "UserId", nullable = false)
    private FitnessUserEntity fitnessUser;
    @OneToMany(mappedBy = "programId")
    private List<ProgramDemonstrationEntity> onlineDemonstrations;
    @OneToMany(mappedBy = "programId")
    private List<ProgramPictureEntity> pictures;

}
