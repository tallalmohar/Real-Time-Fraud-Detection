package com.fraud.consumer.service;

import com.fraud.common.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionConsumer {

    private final FraudDetectionService fraudDetectionService;

    public TransactionConsumer(FraudDetectionService fraudDetectionService){
        this.fraudDetectionService = fraudDetectionService;
    }


    @KafkaListener(topics = "transactions", groupId = "fraud-detection-group")
    public void consumeTransaction(Transaction transaction) {
        log.info("📨 Received transaction: ID={}, Amount={}, Merchant={}, Location={}, PaymentMethod={}", 
                transaction.getTransactionID(), 
                transaction.getAmount(), 
                transaction.getMerchantId(),
                transaction.getLocation(),
                transaction.getPaymentMethod());

        try {
            boolean isFraud = fraudDetectionService.isFraudulent(transaction);
            if(isFraud){
                log.warn("Fraud DETECTED: Transaction {} | Amount: ${} | Merchant: {} | Payment: {}",
                        transaction.getTransactionID(),
                        transaction.getAmount(),
                        transaction.getMerchantId(),
                        transaction.getPaymentMethod());

                // TODO: still need to save to db
                // TODO: and send alert to the kafka topic
            }else{
                log.info("Transaction OK: {} | Amount: ${}",
                        transaction.getTransactionID(),
                        transaction.getAmount());
            }
        }catch(Exception e){
            log.error("Error processing transaction {}: {}",
                    transaction.getTransactionID(),
                    e.getMessage(),
                    e);
        }
    }
}
