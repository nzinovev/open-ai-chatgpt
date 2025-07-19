package com.example.openai.chatgpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import com.example.openai.chatgpt.entity.Operation;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    Optional<Operation> findOperationByPublicId(String operationPublicId);
}
