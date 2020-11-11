package com.capstone.alarmengine;

import com.capstone.alarmengine.model.Device;
import com.capstone.alarmengine.property.FileStorageProperties;
import com.capstone.alarmengine.repository.DeviceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SpringBootApplication
@EnableNeo4jRepositories
@EnableTransactionManagement
@EnableScheduling
@EnableConfigurationProperties({FileStorageProperties.class})
public class AlarmEngineApplication {
	private final static Logger log = LoggerFactory.getLogger(AlarmEngineApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AlarmEngineApplication.class, args);
		// System.exit(0);
	}

}
