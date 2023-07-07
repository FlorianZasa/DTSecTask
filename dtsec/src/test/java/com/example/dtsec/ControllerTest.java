package com.example.dtsec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ControllerTest {

    @Mock
    private Services services;
    @Mock
    private Logger logger;

    @Autowired
    private Controller controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new Controller(services);
    }

    @Test
    void testApi() {
        String expected = "API up running...";
        String result = controller.api();
        assertEquals(expected, result);
    }

    @Test
    void testProcessCSR() throws IOException {
        // Prepare test data
        String csr = "testCSR";
        Map<String, String> csrRequest = new HashMap<>();
        csrRequest.put("csr", csr);

        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("result", "success");

        // Mock the services.parseCSR() method
        when(services.parseCSR(csr)).thenReturn(expectedResponse);
        // Mock the logger

        Logger logger = mock(Logger.class);
        doNothing().when(logger).error(anyString());
        when(logger.isErrorEnabled()).thenReturn(true);


        // Call the method under test
        Map<String, String> actualResponse = controller.processCSR(csrRequest);

        // Verify the services.parseCSR() method was called with the correct argument
        verify(services).parseCSR(csr);

        // Verify the expected response is returned
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testProcessCSRError() throws IOException {
        // Prepare test data
        String csr = "testCSR";
        Map<String, String> csrRequest = new HashMap<>();
        csrRequest.put("csr", csr);

        IOException exception = new IOException("Error parsing CSR");

        // Mock the services.parseCSR() method to throw an exception
        when(services.parseCSR(csr)).thenThrow(exception);

        // Call the method under test
        Map<String, String> actualResponse = controller.processCSR(csrRequest);

        // Verify the services.parseCSR() method was called with the correct argument
        verify(services).parseCSR(csr);

        // Verify an empty response is returned
        assertEquals(new HashMap<>(), actualResponse);
    }
}
