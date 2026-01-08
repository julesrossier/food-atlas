package ch.heigvd.repositories;

import ch.heigvd.entities.Recipe;
import io.javalin.http.ConflictResponse;
import io.javalin.http.NotFoundResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Locked;

public class RecipeRepository {
  private static int counter = 0;
  List<Recipe> recipes = new ArrayList<>();

  /**
   * @param max_time : if not null, filter the recipes by time preparation under max_time
   * @param labels : if defined, filter the recipes that have all the labels given
   * @return A list of recipes, optionally filtered
   */
  @Locked
  public List<Recipe> getRecipes(Integer max_time, List<String> labels) {
    Predicate<Recipe> predicate = x -> true;
    if (max_time != null) {
      predicate = predicate.and(recipe -> recipe.getTime() <= max_time);
    }
    if (!labels.isEmpty()) {
      for (String label : labels) {
        System.out.println(label);
        predicate = predicate.and(recipe -> recipe.getLabels().contains(label));
      }
    }

    return recipes.stream().filter(predicate).collect(Collectors.toList());
  }

  @Locked
  public Recipe getOneById(int recipeId) {
    for (Recipe recipe : recipes) {
      if (recipe.getId() == recipeId) {
        return recipe;
      }
    }
    throw new NotFoundResponse("Recipe with id " + recipeId + " not found");
  }

  @Locked
  public void newRecipe(Recipe recipe) {
    for (Recipe r : recipes) {
      if (recipe.getName().equals(r.getName())) {
        throw new ConflictResponse();
      }
    }

    recipe.setId(++counter);
    recipes.add(recipe);
  }

  @Locked
  public void modifyRecipe(int recipeId, Recipe newRecipe) {
    for (Recipe r : recipes) {
      if (newRecipe.getName().equals(r.getName())) {
        throw new ConflictResponse();
      }
    }

    Recipe oldRecipe = getOneById(recipeId);
    if (newRecipe.getName() != null) oldRecipe.setName(newRecipe.getName());
    if (newRecipe.getTime() != null) oldRecipe.setTime(newRecipe.getTime());
    if (newRecipe.getDescription() != null) oldRecipe.setDescription(newRecipe.getDescription());
    if (newRecipe.getLabels() != null && !newRecipe.getLabels().isEmpty())
      oldRecipe.setLabels(new ArrayList<>(newRecipe.getLabels()));
  }

  @Locked
  public boolean deleteById(int recipeId) {
    for (Recipe recipe : recipes) {
      if (recipe.getId() == recipeId) {
        recipes.remove(recipe);
        return true;
      }
    }
    return false;
  }

  @Locked
  public boolean existsById(int recipeId) {
    for (Recipe recipe : recipes) {
      if (recipe.getId() == recipeId) return true;
    }
    return false;
  }
}
