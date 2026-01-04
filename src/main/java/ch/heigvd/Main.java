package ch.heigvd;

import ch.heigvd.controllers.CountryController;
import ch.heigvd.controllers.RecipeController;
import ch.heigvd.entities.Country;
import ch.heigvd.entities.Recipe;
import ch.heigvd.repositories.CountryRepository;
import ch.heigvd.repositories.RecipeRepository;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.Validator;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
  public static final int PORT = 8080;

  public static void main(String[] args) {
    Javalin app = Javalin.create();

    RecipeRepository recipeRepository = new RecipeRepository();
    CountryRepository countryRepository = new CountryRepository(recipeRepository);
    RecipeController recipeController = new RecipeController(recipeRepository);
    CountryController countryController = new CountryController(countryRepository);

      recipeRepository.newRecipe(new Recipe("Test", 30, "AAAAAAAAAAAAAA", List.of("a", "b", "c")));
      recipeRepository.newRecipe(new Recipe("Test2", 40, "AAAAAAAAAAAAA", List.of("a", "b", "c")));
      recipeRepository.newRecipe(new Recipe("test3", 50, "RHDHDFHDFHFDFH", List.of("b", "c", "d")));

      countryRepository.newCountry(new Country("CHE", "Switzerland", List.of(1)));

    app.get("/test", ctx -> {ctx.result("Test");});

    app.get("/recipes", recipeController::getRecipes);
    // TODO : verify if recipe is linked to a country before deleting
    app.post("/recipes", recipeController::addRecipe);
    app.get("/recipes/{id}", recipeController::getById);
    app.patch("/recipes/{id}", recipeController::patchRecipe);
    app.delete("/recipes/{id}", recipeController::deleteRecipe);

    app.post("/countries", countryController::newCountry);
    app.get("/countries", countryController::getAllCountries);
    app.get("/countries/{code}", countryController::getOneCountry);
    app.patch("/countries/{code}", countryController::updateCountry);
    app.delete("/countries/{code}", countryController::deleteCountry);

    app.get("/countries/{code}/recipes", countryController::getRecipesFromCountry);
    app.post("/countries/{code}/recipes", countryController::linkRecipesToCountry);
    app.delete("/countries/{code}/recipes", countryController::dissociateRecipesFromCountry);

    app.start(PORT);
  }
}
