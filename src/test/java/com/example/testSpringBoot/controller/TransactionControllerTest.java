package com.example.testSpringBoot.controller;


import com.example.controller.TransactionController;
import com.example.model.Transaction;
import com.example.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

class TransactionControllerTest {

    private MockMvc mockMvc;
    // Se crea mock del servicio
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
    	// Inicializa los mocks
        MockitoAnnotations.openMocks(this);
       // Configura MockMvc para usar el controlador transaccional
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
       // Crea una transacción de ejemplo 
        OffsetDateTime specificDate = OffsetDateTime.of(2025, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC);
        transaction = new Transaction();
        transaction.setAmount(500);
        transaction.setMerchant("Supermercado");
        transaction.setCustomer("Juan");
        transaction.setDate(specificDate);
    }

    @Test
    void testCreateTransaction() throws Exception {
    	// Configura  mock para que cuando se llame al método createTransaction, devuelva la transacción creada
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);
        
        // Realiza una solicitud POST a "/transaction", enviando un JSON con los datos de la transacción
        mockMvc.perform(post("/transaction")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":500,\"business\":\"Supermercado\",\"tenpistaName\":\"Juan\",\"transactionDate\":\"2025-01-01T10:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(500))
                .andExpect(jsonPath("$.business").value("Supermercado"))
                .andExpect(jsonPath("$.tenpistaName").value("Juan"));
    }

    @Test
    void testUpdateTransaction() throws Exception {
    	// Configura mock para que cuando se llame al método updateTransaction, devuelva la transacción actualizada
        when(transactionService.updateTransaction(anyInt(), any(Transaction.class))).thenReturn(transaction);
        
        // Realiza una solicitud PUT a "/transaction/1", enviando un JSON con los datos de la transacción a actualizar
        mockMvc.perform(put("/transaction/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":700,\"business\":\"Tienda\",\"tenpistaName\":\"Carlos\",\"transactionDate\":\"2025-01-01T10:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(500)) 
                .andExpect(jsonPath("$.business").value("Supermercado"));
    }

    @Test
    void testDeleteTransaction() throws Exception {
    	// Configura mock para que cuando se llame al método deleteTransaction, no realice ninguna acción
        doNothing().when(transactionService).deleteTransaction(anyInt());
        // Realiza una solicitud DELETE a "/transaction/1" para eliminar la transacción con ID 1
        mockMvc.perform(delete("/transaction/1"))
        // Verifica que el código de respuesta sea 204 No Content (indica que la transacción se eliminó correctamente)
                .andExpect(status().isNoContent());
        // Verifica que el método deleteTransaction haya sido llamado una vez con el ID 1.
        verify(transactionService, times(1)).deleteTransaction(1);
    }
}
