package com.example.dtsec;

import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.util.io.pem.PemObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


@Service
public class Services {

    private final Logger logger;

    public Services() {
        this.logger = LoggerFactory.getLogger(Services.class);
    }


    public Map<String, String> parseCSR(String csrContent) throws IOException {
        // Create a StringReader to read the CSR string
        StringReader stringReader = new StringReader(csrContent);

        // Create a PEMParser with the StringReader
        PEMParser pemParser = new PEMParser(stringReader);

        try {
            // Read the PEM object
            PemObject pemObject = pemParser.readPemObject();

            // Check if the PEM object is a CSR
            if (pemObject.getType().equals("CERTIFICATE REQUEST")) {
                // Get the content (bytes) of the PEM object
                byte[] csrBytes = pemObject.getContent();

                // Parse the CSR
                CertificationRequest csr = CertificationRequest.getInstance(csrBytes);

                // Get the subject
                X500Name subject = csr.getCertificationRequestInfo().getSubject();

                // Retrieve subject components
                String commonName = subject.getRDNs(org.bouncycastle.asn1.x500.style.BCStyle.CN)[0].getFirst().getValue().toString();
                String organization = subject.getRDNs(org.bouncycastle.asn1.x500.style.BCStyle.O)[0].getFirst().getValue().toString();
                // Get the PKI algorithm
                String pkiAlgorithm = csr.getSignatureAlgorithm().getAlgorithm().toString();

                String country = subject.getRDNs(BCStyle.C).length > 0
                        ? subject.getRDNs(BCStyle.C)[0].getFirst().getValue().toString()
                        : null;
                String state = subject.getRDNs(BCStyle.ST).length > 0
                        ? subject.getRDNs(BCStyle.ST)[0].getFirst().getValue().toString()
                        : null;
                String ou = subject.getRDNs(BCStyle.OU).length > 0
                        ? subject.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString()
                        : null;
                String email = subject.getRDNs(BCStyle.EmailAddress).length > 0
                        ? subject.getRDNs(BCStyle.EmailAddress)[0].getFirst().getValue().toString()
                        : null;

                Map<String, String> response = new HashMap<>();
                response.put("csr", csrContent);
                response.put("name",commonName);
                response.put("orga",organization);
                response.put("pki",pkiAlgorithm);
                response.put("country",country);
                response.put("state",state);
                response.put("ou",ou);
                response.put("email",email);

                logger.info("Response gefunden für: " + response.get("csr"));
                return response;

            } else {
                logger.warn("Das Object ist kein CSR Objekt");

            }
        } catch (Exception e) {
            // Handle any exceptions that occur during parsing
            e.printStackTrace();
            logger.error(e.getMessage());

        } finally {
            // Close the PEMParser and StringReader
            pemParser.close();
            stringReader.close();
        }
        return new HashMap<>();
    }

}
