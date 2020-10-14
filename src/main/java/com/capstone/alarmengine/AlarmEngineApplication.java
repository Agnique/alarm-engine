package com.capstone.alarmengine;

import com.capstone.alarmengine.model.Device;
import com.capstone.alarmengine.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@SpringBootApplication
@EnableNeo4jRepositories
public class AlarmEngineApplication {
	private final static Logger log = LoggerFactory.getLogger(AlarmEngineApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AlarmEngineApplication.class, args);
		System.exit(0);
	}

	@Bean
	CommandLineRunner demo(DeviceRepository itemRepository) {
		return args -> {

			log.info("starting");
			itemRepository.deleteAll();

			Device GeneratorsWest1 = new Device("GeneratorsWest1");
			Device GeneratorsWest2 = new Device("GeneratorsWest2");
			Device GeneratorsGEN1 = new Device("GeneratorsGEN1");
			Device GeneratorsGEN3 = new Device("GeneratorsGEN3");
			Device BusTiesCPL_XA = new Device("BusTiesCPL_XA");
			Device TransfersTRF_To_A = new Device("TransfersTRF_To_A");
			Device IncomersINC_A = new Device("IncomersINC_A");
			Device BusTiesCPL_AB = new Device("BusTiesCPL_AB");
			Device IncomersINC_B = new Device("IncomersINC_B");
			Device TransfersTRF_To_B = new Device("TransfersTRF_To_B");
			Device BusTiesCPL_BY = new Device("BusTiesCPL_BY");
			Device GeneratorsGEN2 = new Device("GeneratorsGEN2");
			Device GeneratorsEast1 = new Device("GeneratorsEast1");
			Device GeneratorsEast2 = new Device("GeneratorsEast2");
			Device GeneratorsGEN4 = new Device("GeneratorsGEN4");

			log.info("connecting");

			GeneratorsWest1.connect(GeneratorsGEN1);
			GeneratorsGEN1.connect(BusTiesCPL_XA);
			GeneratorsGEN3.connect(BusTiesCPL_XA);
			GeneratorsWest2.connect(GeneratorsGEN3);
			BusTiesCPL_XA.connect(IncomersINC_A);
			BusTiesCPL_XA.connect(TransfersTRF_To_A);
			IncomersINC_A.connect(TransfersTRF_To_A);
			IncomersINC_A.connect(BusTiesCPL_AB);
			TransfersTRF_To_A.connect(BusTiesCPL_AB);
			BusTiesCPL_AB.connect(IncomersINC_B);
			BusTiesCPL_AB.connect(TransfersTRF_To_B);
			IncomersINC_B.connect(TransfersTRF_To_B);
			IncomersINC_B.connect(BusTiesCPL_BY);
			TransfersTRF_To_B.connect(BusTiesCPL_BY);
			BusTiesCPL_BY.connect(GeneratorsGEN2);
			GeneratorsGEN2.connect(GeneratorsEast1);
			GeneratorsGEN2.connect(GeneratorsGEN4);
			GeneratorsGEN4.connect(GeneratorsEast2);

			itemRepository.save(GeneratorsGEN1);
			itemRepository.save(GeneratorsGEN3);
			itemRepository.save(BusTiesCPL_XA);
			itemRepository.save(TransfersTRF_To_A);
			itemRepository.save(IncomersINC_A);
			itemRepository.save(BusTiesCPL_AB);
			itemRepository.save(IncomersINC_B);
			itemRepository.save(TransfersTRF_To_B);
			itemRepository.save(BusTiesCPL_BY);
			itemRepository.save(GeneratorsGEN2);
			itemRepository.save(GeneratorsEast1);
			itemRepository.save(GeneratorsEast2);
		    itemRepository.save(GeneratorsWest1);

			log.info("Saved to neo4j");
			long cnt = itemRepository.count();
			log.info(String.valueOf(cnt));

			//log.info(GeneratorsWest1.toString());

		};
	}
}
