package ch.heigvd.controllers;

import ch.heigvd.entities.Recipe;
import ch.heigvd.repositories.CountryRepository;
import ch.heigvd.repositories.RecipeRepository;
import io.javalin.http.*;
import java.util.*;

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
    Recipe recipe =
        ctx.bodyValidator(Recipe.class)
            .check(r -> r.name() != null, "Recipe name is not set")
            .check(r -> r.time() != null, "Recipe time is not set")
            .check(r -> r.description() != null, "Recipe description is not set")
            .get();
    ctx.json(recipeRepository.newRecipe(recipe));
    ctx.status(201);
  }

  public void getById(Context ctx) {
    int id = ctx.pathParamAsClass("id", Integer.class).get();
    String serverEtag = recipeRepository.getCache(id);
    String clientEtag = ctx.header("If-None-Match");
    if (clientEtag != null && Objects.equals(serverEtag, clientEtag))
      throw new NotModifiedResponse();
    Recipe recipe = recipeRepository.getOneById(id);
    ctx.header("ETag", serverEtag);
    ctx.json(recipe);
  }

  public void patchRecipe(Context ctx) {
    int id = ctx.pathParamAsClass("id", Integer.class).get();
    String serverEtag = recipeRepository.getCache(id);
    String clientEtag = ctx.header("If-Match");
    if (clientEtag != null && !Objects.equals(serverEtag, clientEtag))
      throw new PreconditionFailedResponse();
    Recipe newRecipe = ctx.bodyAsClass(Recipe.class);
    ctx.json(recipeRepository.modifyRecipe(id, newRecipe));
    ctx.header("ETag", recipeRepository.getCache(id));
  }

  public void deleteRecipe(Context ctx) {
    Integer id = ctx.pathParamAsClass("id", Integer.class).get();
    countryRepository.dissociateRecipeFromCountries(id);
    if (!recipeRepository.deleteById(id)) throw new NotFoundResponse();
    ctx.status(204);
  }
}
