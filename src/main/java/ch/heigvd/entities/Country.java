package ch.heigvd.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Country {
  @Getter @Setter private String code;
  @Getter @Setter private String name;
  @Getter @Setter private Set<Integer> recipes;

  public Country(String code, String name, List<Integer> recipes) {
    this.code = code;
    this.name = name;
    this.recipes = new HashSet<>(recipes);
  }

  public Country(String code, String name) {
    this(code, name, List.of());
  }

  public void addRecipe(Integer recipeId) {
    recipes.add(recipeId);
  }
}
