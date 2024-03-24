package dev.projectfitness.ipfitness.models.entities;

import dev.projectfitness.ipfitness.models.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "program_purchase")
public class ProgramPurchaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "PurchaseId", nullable = false)
    private Integer purchaseId;
    @Basic
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "PaymentType", nullable = false)
    private PaymentType paymentType;
    @Basic
    @Column(name = "Price", nullable = false, precision = 2)
    private Double price;
    @Basic
    @Column(name = "UserId", nullable = false)
    private Integer userId;
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PurchaseDate", nullable = false)
    private Date purchaseDate;
    @ManyToOne
    @JoinColumn(name = "ProgramId", referencedColumnName = "ProgramId", nullable = false)
    private FitnessProgramEntity fitnessProgram;

}
