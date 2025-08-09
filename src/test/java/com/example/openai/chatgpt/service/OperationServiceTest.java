package com.example.openai.chatgpt.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.openai.chatgpt.dto.CreateOperationRequest;
import com.example.openai.chatgpt.dto.UpdateOperationRequest;
import com.example.openai.chatgpt.entity.Category;
import com.example.openai.chatgpt.entity.Operation;
import com.example.openai.chatgpt.entity.enums.OperationType;
import com.example.openai.chatgpt.exception.BadRequestException;
import com.example.openai.chatgpt.exception.NotFoundException;
import com.example.openai.chatgpt.repository.CategoryRepository;
import com.example.openai.chatgpt.repository.OperationRepository;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

    @Mock private OperationRepository operationRepository;
    @Mock private CategoryRepository categoryRepository;

    @InjectMocks private OperationService operationService;

    private Category category;
    private Operation operation;

    @BeforeEach
    void setUp() {
        category = Category.builder().id(1L).name("Test Category").build();
        operation = Operation.builder()
                .id(1L)
                .publicId("uuid-123")
                .name("Test Operation")
                .amount(BigDecimal.valueOf(100))
                .type(OperationType.DEPOSIT)
                .category(category)
                .build();
    }

    // --- CREATE ---

    @Test
    void createOperation_success() {
        var request = CreateOperationRequest.builder()
                .operationName("Create")
                .operationAmount(BigDecimal.TEN)
                .operationType(OperationType.DEPOSIT)
                .categoryId(1L)
                .build();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(operationRepository.save(any())).thenReturn(operation);

        var response = operationService.createOperation(request);

        assertThat(response).isNotNull();
        assertThat(response.getOperationName()).isEqualTo("Test Operation");
        verify(operationRepository).save(any());
    }

    @Test
    void createOperation_categoryNotFound_throwsBadRequest() {
        var request = CreateOperationRequest.builder()
                .categoryId(999L)
                .operationAmount(BigDecimal.TEN)
                .operationName("Invalid")
                .operationType(OperationType.DEPOSIT)
                .build();

        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> operationService.createOperation(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Category not found");
    }

    // --- UPDATE ---

    @Test
    void updateOperation_success() {
        var request = UpdateOperationRequest.builder()
                .operationName("Updated")
                .operationAmount(BigDecimal.valueOf(999))
                .operationType(OperationType.WITHDRAW)
                .categoryId(1L)
                .build();
        var updatedOperation = operation.toBuilder()
                .name(request.getOperationName())
                .amount(request.getOperationAmount())
                .type(request.getOperationType())
                .category(category)
                .build();

        when(operationRepository.findOperationByPublicId("uuid-123")).thenReturn(Optional.of(operation));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(operationRepository.save(any())).thenReturn(updatedOperation);

        var result = operationService.updateOperation("uuid-123", request);

        assertThat(result.getOperationAmount()).isEqualTo(BigDecimal.valueOf(999));
        verify(operationRepository).save(any());
    }

    @Test
    void updateOperation_operationNotFound_throwsBadRequest() {
        when(operationRepository.findOperationByPublicId("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> operationService.updateOperation("missing", new UpdateOperationRequest()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Operation not found");
    }

    @Test
    void updateOperation_categoryIdMissing_throwsBadRequest() {
        var request = UpdateOperationRequest.builder().categoryId(null).build();
        when(operationRepository.findOperationByPublicId("uuid-123")).thenReturn(Optional.of(operation));

        assertThatThrownBy(() -> operationService.updateOperation("uuid-123", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Category ID is required");
    }

    @Test
    void updateOperation_categoryNotFound_throwsBadRequest() {
        var request = UpdateOperationRequest.builder().categoryId(99L).build();

        when(operationRepository.findOperationByPublicId("uuid-123")).thenReturn(Optional.of(operation));
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> operationService.updateOperation("uuid-123", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Category not found");
    }

    // --- FIND ONE ---

    @Test
    void findByPublicId_success() {
        when(operationRepository.findOperationByPublicId("uuid-123")).thenReturn(Optional.of(operation));

        var result = operationService.findByPublicId("uuid-123");

        assertThat(result.getOperationName()).isEqualTo("Test Operation");
    }

    @Test
    void findByPublicId_notFound_throwsNotFound() {
        when(operationRepository.findOperationByPublicId("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> operationService.findByPublicId("missing"))
                .isInstanceOf(NotFoundException.class);
    }

    // --- FIND ALL ---

    @Test
    void findAllOperations_success() {
        var page = new PageImpl<>(List.of(operation));
        when(operationRepository.findAll(any(Pageable.class))).thenReturn(page);

        var result = operationService.findAllOperations(0, 10);

        assertThat(result.getContent()).hasSize(1);
    }

    // --- DELETE ---

    @Test
    void deleteByPublicId_success() {
        when(operationRepository.findOperationByPublicId("uuid-123")).thenReturn(Optional.of(operation));

        operationService.deleteByPublicId("uuid-123");

        verify(operationRepository).delete(operation);
    }

    @Test
    void deleteByPublicId_notFound_throwsNotFound() {
        when(operationRepository.findOperationByPublicId("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> operationService.deleteByPublicId("missing"))
                .isInstanceOf(NotFoundException.class);
    }
}
