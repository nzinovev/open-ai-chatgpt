package com.example.openai.chatgpt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.openai.chatgpt.dto.CreateOperationRequest;
import com.example.openai.chatgpt.dto.OperationResponse;
import com.example.openai.chatgpt.dto.UpdateOperationRequest;
import com.example.openai.chatgpt.service.OperationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
public class OperationController {

    private final OperationService operationService;

    @PostMapping
    public ResponseEntity<OperationResponse> createOperation(@RequestBody CreateOperationRequest request) {
        var created = operationService.createOperation(request);
        return ResponseEntity.ok(created);
    }

    @PatchMapping("/{operationPublicId}")
    public ResponseEntity<OperationResponse> updateOperation(
            @PathVariable String operationPublicId,
            @RequestBody UpdateOperationRequest request) {
        OperationResponse response = operationService.updateOperation(operationPublicId, request);
        return ResponseEntity.ok(response);
    }
}
