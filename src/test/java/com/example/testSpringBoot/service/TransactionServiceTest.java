package com.example.testSpringBoot.service;

import com.example.model.Transaction;
import com.example.repository.TransactionRepository;
import com.example.service.TransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
    	OffsetDateTime specificDate = OffsetDateTime.of(2025, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);
        // Inicializa los mocks de Mockito
        MockitoAnnotations.openMocks(this);
        // Se crea una transacción de ejemplo
        transaction = new Transaction();
        transaction.setAmount(500);
        transaction.setMerchant("Supermercado");
        transaction.setCustomer("Juan");
        transaction.setDate(specificDate);
    }

    @Test
    void testCreateTransaction() {
    	// Configurar mock para devolver la transacción creada cuando se llame a save()
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction createdTransaction = transactionService.createTransaction(transaction);
       // Verifica que la transacción no sea nula
        assertNotNull(createdTransaction);
        // Verifica que el monto de la transacción creada sea 500
        assertEquals(500, createdTransaction.getAmount());
       // Verifica que el método save() haya sido llamado una vez con el objeto transaction
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testUpdateTransaction() {
    	OffsetDateTime specificDate = OffsetDateTime.of(2025, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setAmount(700);
        updatedTransaction.setMerchant("Tienda");
        updatedTransaction.setCustomer("Carlos");
        updatedTransaction.setDate(specificDate);

        // Configura el mock para que encuentre la transacción con id 1 y devuelva la transacción original
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
        // Configura el mock para que guarde la transacción actualizada
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updatedTransaction);
        // Llama al método de servicio para actualizar la transacción
        Transaction result = transactionService.updateTransaction(1, updatedTransaction);
        // Verifica que el monto de la transacción actualizada sea 700
        assertEquals(700, result.getAmount());
        // Verifica que el comercio de la transacción actualizada sea "Tienda"
        assertEquals("Tienda", result.getMerchant());
       // Verifica que el método save() haya sido llamado una vez con el objeto updatedTransaction
        verify(transactionRepository, times(1)).save(updatedTransaction);
    }

    @Test
    void testDeleteTransaction() {
    	// Configura el mock para que encuentre la transacción con id 1
        when(transactionRepository.findById(1)).thenReturn(Optional.of(transaction));
       // Configura el mock para indicar que la transacción existe
        when(transactionRepository.existsById(1)).thenReturn(true);
       // Llama al método de servicio para eliminar la transacción
        transactionService.deleteTransaction(1);
       // Verifica que el método deleteById() haya sido llamado una vez con el id 1
        verify(transactionRepository, times(1)).deleteById(1);
    }


    @Test
    void testDeleteTransactionNotFound() {
    	// Configura el mock para indicar que la transacción con id 1 no existe
        when(transactionRepository.existsById(1)).thenReturn(false);
        // Se espera que se lance una excepción de tipo IllegalArgumentException cuando no se encuentre la transacción
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.deleteTransaction(1);
        });
       // Verifica que el mensaje de la excepción sea "Transacción no encontrada"
        assertEquals("Transacción no encontrada", exception.getMessage());
    }
}
