package dev.projectfitness.ipfitness.models.requests;

import dev.projectfitness.ipfitness.models.enums.PaymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProgramPurchaseRequest {
    @NotNull
    private Integer userId;
    @NotNull
    private PaymentType paymentType;
    @NotBlank
    private String cardNumber;
}
