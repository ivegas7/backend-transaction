package com.example.service;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Transaction;
import com.example.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import com.example.errors.BadRequestException;
import com.example.errors.ResourceNotFoundException;

@Service
public class TransactionServiceImpl implements TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Override
    // Devuelve la lista de todas las transacciones
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
    
    @Override
    // Devuelve la transacción solicitada por ID
    public Transaction getTransactionById(int id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }
    
    @Transactional
    // Crea una nueva transacción
    public Transaction createTransaction(Transaction transaction) {
        validateTransaction(transaction); //Valida condiciones antes de crear
        return transactionRepository.save(transaction);
    }
    
    @Transactional
    // Actualiza los nuevos valores de la transacción
    public Transaction updateTransaction(int id, Transaction updatedTransaction) {
        Transaction existingTransaction = getTransactionById(id);
        validateTransaction(updatedTransaction);
        existingTransaction.setAmount(updatedTransaction.getAmount());
        existingTransaction.setMerchant(updatedTransaction.getMerchant());
        existingTransaction.setCustomer(updatedTransaction.getCustomer());
        existingTransaction.setDate(updatedTransaction.getDate());
        return transactionRepository.save(existingTransaction);
    }
    
    @Transactional
    // Elimina una transacción
    public void deleteTransaction(int id) {
        getTransactionById(id); // Esto lanza ResourceNotFoundException si no existe
        transactionRepository.deleteById(id);
    }
    
    // Condiciones de validación para generar transacción.
    private void validateTransaction(Transaction transaction) {
        // Validación de fecha
    	if (transaction.getDate().isAfter(OffsetDateTime.now())) {
            throw new BadRequestException("The date cannot be greater than the current date");
        }
        
        // Validación de monto
        if (transaction.getAmount() < 0) {
            throw new BadRequestException("The amount cannot be negative");
        }
        
        // Validación monto igual a 0
        if (transaction.getAmount() == 0) {
            throw new BadRequestException("The amount cannot be 0");
        }
        
        long count = transactionRepository.countByCustomer(transaction.getCustomer());
        
        // Validación máximo de solicitudes
        if (count >= 100) {
            throw new BadRequestException("No more than 100 transactions can be recorded");
        }
    }
}
