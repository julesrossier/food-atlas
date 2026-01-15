package ch.heigvd.entities;

import java.util.Set;

public record Recipe(
    Integer id, String name, Integer time, String description, Set<String> labels) {}
