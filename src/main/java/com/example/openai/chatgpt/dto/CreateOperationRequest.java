package com.example.openai.chatgpt.dto;

import java.math.BigDecimal;

import com.example.openai.chatgpt.entity.enums.OperationType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOperationRequest {
    @NotBlank(message = "Operation name is required")
    @Size(max = 255, message = "Operation name must not exceed 255 characters")
    private String operationName;
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
    private BigDecimal operationAmount;
    @NotNull(message = "Operation type is required")
    private OperationType operationType;
    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
