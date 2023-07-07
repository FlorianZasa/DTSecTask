package com.example.dtsec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class Controller {

    private final Services services;
    private final Logger logger;
    
    public Controller(Services services) {
        this.services = services;
        this.logger = LoggerFactory.getLogger(Controller.class);
    }

    @CrossOrigin
    @GetMapping("/api")
    public String api() {
        return "API up running...";
    }

    @CrossOrigin
    @PostMapping("/api/csr")
    public Map<String, String> processCSR(@RequestBody Map<String, String> csrRequest) {
        
        
        Map<String, String> response = new HashMap<>();
        String csr = csrRequest.get("csr");

        try {
            response = this.services.parseCSR(csr);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("##############"+e.getMessage());
        }
        // Return a response to the frontend
        return response;
    }

}
