package com.example.openai.chatgpt.service;

import org.springframework.stereotype.Service;

import com.example.openai.chatgpt.dto.CreateOperationRequest;
import com.example.openai.chatgpt.dto.OperationResponse;
import com.example.openai.chatgpt.entity.Category;
import com.example.openai.chatgpt.entity.Operation;
import com.example.openai.chatgpt.repository.CategoryRepository;
import com.example.openai.chatgpt.repository.OperationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;
    private final CategoryRepository categoryRepository;

    public OperationResponse createOperation(CreateOperationRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Operation operation = Operation.builder()
                .publicId(request.getOperationPublicId())
                .name(request.getOperationName())
                .amount(request.getOperationAmount())
                .type(request.getOperationType())
                .category(category)
                .build();

        var saved = operationRepository.save(operation);
        return OperationResponse.builder()
                .operationId(saved.getId())
                .operationPublicId(saved.getPublicId())
                .operationName(saved.getName())
                .operationAmount(saved.getAmount())
                .operationType(saved.getType())
                .categoryId(saved.getCategory().getId())
                .build();
    }
}
