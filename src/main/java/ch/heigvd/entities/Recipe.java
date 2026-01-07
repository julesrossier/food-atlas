package ch.heigvd.entities;

import lombok.Getter;
import lombok.Locked;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
public class Recipe {
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private Integer time;
    @Getter
    private String description;
    // TODO : Check if Lombok does a copy for the setter
    @Getter
    private Set<String> labels;

    public Recipe(){}

    public Recipe(String name, Integer time, String description, List<String> labels) {
        this.name = name;
        this.time = time;
        this.description = description;
        this.labels = new HashSet<>(labels);
    }

    @Locked
    public void setName(String name) {
        this.name = name;
    }

    @Locked
    public void setId(int id) {
        this.id = id;
    }

    @Locked
    public void setTime(Integer time) {
        this.time = time;
    }

    @Locked
    public void setDescription(String description) {
        this.description = description;
    }

    @Locked
    public void setLabels(List<String> labels) {
        this.labels = new HashSet<>(labels);
    }

    @Locked
    public void addLabel(String label) {
        this.labels.add(label);
    }

}
