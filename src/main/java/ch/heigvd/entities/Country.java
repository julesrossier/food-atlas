package ch.heigvd.entities;

import java.util.Set;

public record Country(String code, String name, Set<Integer> recipes) {}
