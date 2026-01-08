package ch.heigvd.controllers;

import ch.heigvd.entities.Recipe;
import ch.heigvd.repositories.CountryRepository;
import ch.heigvd.repositories.RecipeRepository;
import io.javalin.http.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeController {
  private final RecipeRepository recipeRepository;
  private final CountryRepository countryRepository;

  public RecipeController(RecipeRepository recipeRepository, CountryRepository countryRepository) {
    this.recipeRepository = recipeRepository;
    this.countryRepository = countryRepository;
  }

  public void getRecipes(Context ctx) {
    Integer max_time = ctx.queryParamAsClass("max_time", Integer.class).allowNullable().get();
    List<String> labels = new ArrayList<>();
    String labelsString = ctx.queryParam("labels");
    if (labelsString != null && !labelsString.isEmpty()) {
      labels = Arrays.asList(ctx.queryParam("labels").split(","));
    }
    ctx.json(recipeRepository.getRecipes(max_time, labels));
  }

  public void addRecipe(Context ctx) {
    Recipe recipe = ctx.bodyAsClass(Recipe.class);
    recipeRepository.newRecipe(recipe);
    throw new CreatedResponse();
  }

  public void getById(Context ctx) {
    int id = ctx.pathParamAsClass("id", Integer.class).get();
    Recipe recipe = recipeRepository.getOneById(id);
    ctx.json(recipe);
  }

  public void patchRecipe(Context ctx) {
    int id = ctx.pathParamAsClass("id", Integer.class).get();
    Recipe newRecipe = ctx.bodyAsClass(Recipe.class);
    recipeRepository.modifyRecipe(id, newRecipe);
    throw new NoContentResponse();
  }

  public void deleteRecipe(Context ctx) {
    int id = ctx.pathParamAsClass("id", Integer.class).get();
    if (countryRepository.isRecipeLinkedToCountry(id)) {
      throw new NotModifiedResponse("Recipe is linked to at least one country");
    }
    if (!recipeRepository.deleteById(id)) throw new NotFoundResponse();
    throw new NoContentResponse();
  }
}
