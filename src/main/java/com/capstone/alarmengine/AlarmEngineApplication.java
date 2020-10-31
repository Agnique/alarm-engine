package com.capstone.alarmengine;

import com.capstone.alarmengine.model.Device;
import com.capstone.alarmengine.repository.DeviceRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
public class AlarmEngineApplication {
	private final static Logger log = LoggerFactory.getLogger(AlarmEngineApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AlarmEngineApplication.class, args);
		// System.exit(0);
	}

	@Bean
	CommandLineRunner demo(DeviceRepository deviceRepository) {
		return args -> {

			log.info("starting");
			deviceRepository.deleteAll();

			log.info("connecting");

			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Device>> typeReference = new TypeReference<List<Device>>() {};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/json/model.json");
			try {
				List<Device> devices = mapper.readValue(inputStream, typeReference);
				for (Device device : devices) {
					log.info(device.getName());
					Device d1 = deviceRepository.getDeviceByName(device.getName());
					if (d1 == null) d1 = new Device(device.getName());
					Set<Device> neighbors = device.getConnectedDevices();
					for (Device nei : neighbors) {
						Device d2 = deviceRepository.getDeviceByName(nei.getName());
						if (d2 == null) d2 = nei;
						d1.connect(d2);
					}
					deviceRepository.save(d1);
				}
				log.info("Model saved to database.");
			} catch (IOException e){
				log.info("Unable to save model: " + e.getMessage());
			}

		};
	}
}
