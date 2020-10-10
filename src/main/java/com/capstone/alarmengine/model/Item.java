package com.capstone.alarmengine.model;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@NodeEntity
public class Item {
    @Id @GeneratedValue private Long id;

    private String name;

    public Item() {

    }
    public Item(String name) {
        this.name = name;
    }

    @Relationship(type = "CONNECT", direction = Relationship.OUTGOING)
    public Set<Item> connectedItems;

    public void connect(Item item) {
        if (connectedItems == null) {
            connectedItems = new HashSet<>();
        }
        connectedItems.add(item);
    }

    public String toString() {
        return this.name + "'s connected items => "
                + Optional.ofNullable(this.connectedItems).orElse(
                        Collections.emptySet()).stream().map(Item::getName).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
