package com.example.openai.chatgpt.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.openai.chatgpt.dto.CreateOperationRequest;
import com.example.openai.chatgpt.dto.OperationResponse;
import com.example.openai.chatgpt.dto.UpdateOperationRequest;
import com.example.openai.chatgpt.entity.Category;
import com.example.openai.chatgpt.entity.Operation;
import com.example.openai.chatgpt.exception.BadRequestException;
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
                .orElseThrow(() -> new BadRequestException("Category not found"));

        Operation operation = Operation.builder()
                .publicId(UUID.randomUUID().toString())
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

    public OperationResponse updateOperation(String publicId, UpdateOperationRequest request) {
        Operation operation = operationRepository.findOperationByPublicId(publicId)
                .orElseThrow(() -> new BadRequestException("Operation not found with publicId: " + publicId));

        if (request.getOperationName() != null) {
            operation.setName(request.getOperationName());
        }

        if (request.getOperationAmount() != null) {
            operation.setAmount(request.getOperationAmount());
        }

        if (request.getOperationType() != null) {
            operation.setType(request.getOperationType());
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BadRequestException("Category not found: " + request.getCategoryId()));
            operation.setCategory(category);
        } else if (request.getCategoryId() == null) {
            throw new BadRequestException("Category ID is required when updating category");
        }

        Operation saved = operationRepository.save(operation);

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
