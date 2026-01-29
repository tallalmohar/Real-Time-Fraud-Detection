package com.fraud.consumer.config;

import ai.onnxruntime.OrtEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Configuration class for loading the ONNX machine learning model.
 * 
 * SPRINT 2: This bean is OPTIONAL and disabled by default.
 * We're using rule-based detection for now.
 * 
 * SPRINT 5: We'll enable this when we train a real ML model.
 * Set spring.ml.enabled=true in application.properties to activate.
 */
@Slf4j
@Configuration
public class OnnxModelConfig {

	/**
	 * Creates ONNX Runtime session - ONLY if ML model is enabled.
	 * 
	 * To enable: Set spring.ml.enabled=true in application.properties
	 */
	@Bean
	@ConditionalOnProperty(name = "spring.ml.enabled", havingValue = "true", matchIfMissing = false)
	public OrtSession fraudDetectionModel() throws OrtException, IOException {
		log.info("🔧 Loading ONNX fraud detection model...");

		// Get the global ONNX Runtime environment
		OrtEnvironment env = OrtEnvironment.getEnvironment();

		// Load model file from classpath
		ClassPathResource modelResource = new ClassPathResource("fraud_model.onnx");
		byte[] modelBytes = modelResource.getInputStream().readAllBytes();

		// Validate model is not empty
		if (modelBytes.length == 0) {
			throw new IllegalStateException("fraud_model.onnx is empty! Train a model in Sprint 5 first.");
		}

		// Create session from model bytes
		OrtSession session = env.createSession(modelBytes);

		log.info("ONNX model loaded successfully!");
		log.info("Model has {} inputs and {} outputs",
				session.getNumInputs(),
				session.getNumOutputs());

		return session;
	}
}
