package com.example.openai.chatgpt.dto;

import java.math.BigDecimal;

import com.example.openai.chatgpt.entity.enums.OperationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOperationRequest {
    private String operationPublicId;
    private String operationName;
    private BigDecimal operationAmount;
    private OperationType operationType;
    private Long categoryId;
}
