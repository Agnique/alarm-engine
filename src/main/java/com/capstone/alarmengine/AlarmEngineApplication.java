package com.capstone.alarmengine;

import com.capstone.alarmengine.model.Item;
import com.capstone.alarmengine.repository.ItemRepository;
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
	CommandLineRunner demo(ItemRepository itemRepository) {
		return args -> {

			log.info("starting");
			itemRepository.deleteAll();

			Item GeneratorsWest1 = new Item("GeneratorsWest1");
			Item GeneratorsWest2 = new Item("GeneratorsWest2");
			Item GeneratorsGEN1 = new Item("GeneratorsGEN1");
			Item GeneratorsGEN3 = new Item("GeneratorsGEN3");
			Item BusTiesCPL_XA = new Item("BusTiesCPL_XA");
			Item TransfersTRF_To_A = new Item("TransfersTRF_To_A");
			Item IncomersINC_A = new Item("IncomersINC_A");
			Item BusTiesCPL_AB = new Item("BusTiesCPL_AB");
			Item IncomersINC_B = new Item("IncomersINC_B");
			Item TransfersTRF_To_B = new Item("TransfersTRF_To_B");
			Item BusTiesCPL_BY = new Item("BusTiesCPL_BY");
			Item GeneratorsGEN2 = new Item("GeneratorsGEN2");
			Item GeneratorsEast1 = new Item("GeneratorsEast1");
			Item GeneratorsEast2 = new Item("GeneratorsEast2");
			Item GeneratorsGEN4 = new Item("GeneratorsGEN4");

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
