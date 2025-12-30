package ch.heigvd;

import ch.heigvd.controllers.RecipeController;
import ch.heigvd.entities.Recipe;
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

    recipeRepository.newRecipe(new Recipe("Test", 30, "AAAAAAAAAAAAAA", List.of("a", "b", "c")));
    recipeRepository.newRecipe(new Recipe("Test2", 40, "AAAAAAAAAAAAA", List.of("a", "b", "c")));
    recipeRepository.newRecipe(new Recipe("test3", 50, "RHDHDFHDFHFDFH", List.of("b", "c", "d")));

    RecipeController recipeController = new RecipeController(recipeRepository);

    app.get("/test", ctx -> {ctx.result("Test");});

    app.get("/recipes", recipeController::getRecipes);
    app.post("/recipes", recipeController::addRecipe);
    app.get("/recipes/{id}", recipeController::getById);
    app.patch("/recipes/{id}", recipeController::patchRecipe);
    app.delete("/recipes/{id}", recipeController::deleteRecipe);

    app.start(PORT);
  }
}
