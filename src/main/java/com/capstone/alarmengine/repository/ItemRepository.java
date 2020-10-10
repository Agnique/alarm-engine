package com.capstone.alarmengine.repository;

import com.capstone.alarmengine.model.Item;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ItemRepository extends Neo4jRepository<Item, Long> {
    Item getItemByName(String name);
}
