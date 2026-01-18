package ch.heigvd.repositories;

import ch.heigvd.entities.Recipe;
import io.javalin.http.ConflictResponse;
import io.javalin.http.NotFoundResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RecipeRepository {

  private final ConcurrentHashMap<Integer, Recipe> recipes = new ConcurrentHashMap<>();
  private final AtomicInteger idCounter = new AtomicInteger(0);

  /**
   * @param max_time : if not null, filter the recipes by time preparation under max_time
   * @param labels : if defined, filter the recipes that have all the labels given
   * @return A list of recipes, optionally filtered
   */
  public List<Recipe> getRecipes(Integer max_time, List<String> labels) {
    Predicate<Recipe> predicate = x -> true;
    if (max_time != null) {
      predicate = predicate.and(recipe -> recipe.time() <= max_time);
    }
    if (!labels.isEmpty()) {
      for (String label : labels) {
        predicate = predicate.and(recipe -> recipe.labels().contains(label));
      }
    }

    return recipes.values().stream().filter(predicate).collect(Collectors.toList());
  }

  public Recipe getOneById(int recipeId) {
    for (Integer id : recipes.keySet()) {
      if (id == recipeId) {
        return recipes.get(id);
      }
    }
    throw new NotFoundResponse();
  }

  public void newRecipe(Recipe entry) {
    for (Integer id : recipes.keySet()) {
      if (entry.name().equals(recipes.get(id).name())) {
        throw new ConflictResponse();
      }
    }

    Integer id = idCounter.incrementAndGet();
    Recipe recipeToAdd =
        new Recipe(id, entry.name(), entry.time(), entry.description(), Set.copyOf(entry.labels()));
    recipes.putIfAbsent(id, recipeToAdd);
  }

  public void modifyRecipe(int recipeId, Recipe entry) {

    if(!recipes.containsKey(recipeId)) throw new NotFoundResponse();

    recipes.computeIfPresent(recipeId, (i, oldRecipe) -> {
        for (Integer id : recipes.keySet()) {
            if (entry.name() != null && entry.name().equals(recipes.get(id).name())) {
                throw new ConflictResponse();
            }
        }

        return new Recipe(
                oldRecipe.id(),
                entry.name() != null ? entry.name() : oldRecipe.name(),
                entry.time() != null ? entry.time() : oldRecipe.time(),
                entry.description() != null ? entry.description() : oldRecipe.description(),
                (entry.labels() != null && !entry.labels().isEmpty())
                        ? new HashSet<>(entry.labels())
                        : oldRecipe.labels());
    });
  }

  public boolean deleteById(int recipeId) {
    for (Integer id : recipes.keySet()) {
      if (id == recipeId) {
        recipes.remove(id);
        return true;
      }
    }
    return false;
  }

  public boolean existsById(int recipeId) {
    for (Integer id : recipes.keySet()) {
      if (id == recipeId) return true;
    }
    return false;
  }
}
