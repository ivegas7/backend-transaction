package com.example.controller;

import com.example.errors.BadRequestException;
import com.example.errors.ResourceNotFoundException;
import com.example.model.Transaction;
import com.example.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService service;

    // Crea una nueva transacción
    @PostMapping("/create")
    @Operation(summary = "Create Transaction", description = "Used to create a transaction")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        try {
            Transaction createdTransaction = service.createTransaction(transaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
        } catch (BadRequestException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid input data: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Internal error occurred during transaction creation.");
        }
    }

    // Actualiza una transacción
    @PutMapping("/update/{id}")
    @Operation(summary = "Update Transaction", description = "Used to update a transaction")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable int id,
                                                          @Valid @RequestBody Transaction transaction) {
        try {
            Transaction updatedTransaction = service.updateTransaction(id, transaction);
            return ResponseEntity.status(HttpStatus.OK).body(updatedTransaction);
        } catch (ResourceNotFoundException e) {
            throw e; // 
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid input data: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Internal error occurred during transaction update.");
        }
    }

    // Elimina una transacción
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Transaction", description = "Used to delete a transaction")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int id) {
        try {
            service.deleteTransaction(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResourceNotFoundException e) {
            throw e; 
        } catch (Exception e) {
            throw new RuntimeException("Internal error occurred during transaction deletion.");
        }
    }

    // Retorna todas las transacciones
    @GetMapping("/allTransactions")
    @Operation(summary = "Get All Transactions", description = "Retrieve a list of all transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        try {
            List<Transaction> transactions = service.getAllTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            throw new RuntimeException("Internal error occurred while retrieving transactions.");
        }
    }
}
