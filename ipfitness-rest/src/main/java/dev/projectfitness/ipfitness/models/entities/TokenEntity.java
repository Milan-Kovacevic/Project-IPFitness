package dev.projectfitness.ipfitness.models.entities;

import dev.projectfitness.ipfitness.models.enums.TokenType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "token")
public class TokenEntity {
    @Id
    @Column(name = "TokenId", nullable = false)
    private String tokenId;
    @Basic
    @Column(name = "Value", nullable = false, length = 512)
    private String value;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "TokenType", nullable = false)
    private TokenType tokenType;
    @Basic
    @Column(name = "UserId", nullable = false)
    private Integer userId;
}
