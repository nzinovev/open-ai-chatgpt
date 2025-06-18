package com.example.openai.chatgpt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.openai.chatgpt.dto.CreateOperationRequest;
import com.example.openai.chatgpt.entity.Operation;
import com.example.openai.chatgpt.service.OperationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;

    @PostMapping
    public ResponseEntity<Operation> createOperation(@RequestBody CreateOperationRequest request) {
        Operation created = operationService.createOperation(request);
        return ResponseEntity.ok(created);
    }
}
