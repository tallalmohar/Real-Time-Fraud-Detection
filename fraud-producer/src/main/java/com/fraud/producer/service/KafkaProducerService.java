package com.fraud.producer.service;

import com.fraud.common.model.Transaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransaction(Transaction transaction){
        // the topic name transaction matches the one kafka has on the docker instance
        kafkaTemplate.send("transactions",transaction);

    }


}
