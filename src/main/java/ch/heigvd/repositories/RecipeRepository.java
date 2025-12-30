package ch.heigvd.repositories;

import ch.heigvd.entities.Recipe;
import lombok.Locked;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RecipeRepository {
    private static int counter = 0;
    List<Recipe> recipes = new ArrayList<>();

    /**
     *
     * @param max_time : if not null, filter the recipes by time preparation under max_time
     * @param labels : if defined, filter the recipes that have all the labels given
     * @return A list of recipes, optionally filtered
     */
    @Locked
    public List<Recipe> getRecipes(Integer max_time, List<String> labels) {
        Predicate<Recipe> predicate = x -> true;
        if (max_time != null) {
            predicate = predicate.and(recipe -> recipe.getTime() <=  max_time);
        }
        if (!labels.isEmpty()) {
            for (String label : labels) {
                System.out.println(label);
                predicate = predicate.and(recipe -> recipe.getLabels().contains(label));
            }
        }

        return recipes.stream().filter(predicate).collect(Collectors.toList());
    }

    public Optional<Recipe> getById(Integer recipeId) {
        for(Recipe recipe: recipes) {
            if(recipe.getId() == recipeId) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

     public void newRecipe(Recipe recipe) {
        recipe.setId(++counter);
        System.out.println(recipe);
        recipes.add(recipe);
    }

    @Locked
    public boolean modifyRecipe(Integer recipeId, Recipe newRecipe) {
        Optional<Recipe> recipeOptional = getById(recipeId);

        if(recipeOptional.isEmpty()) return false;

        Recipe oldRecipe = recipeOptional.get();

        if(newRecipe.getName() != null) oldRecipe.setName(newRecipe.getName());
        if(newRecipe.getTime() != null) oldRecipe.setTime(newRecipe.getTime());
        if(newRecipe.getDescription() != null) oldRecipe.setDescription(newRecipe.getDescription());
        if(newRecipe.getLabels() != null && !newRecipe.getLabels().isEmpty())
            oldRecipe.setLabels(new ArrayList<>(newRecipe.getLabels()));

        return true;
    }

    @Locked
    public boolean deleteById(Integer recipeId) {
        for(Recipe recipe: recipes) {
            if(recipe.getId() == recipeId) {
                recipes.remove(recipe);
                return true;
            }
        }
        return false;
    }

}
