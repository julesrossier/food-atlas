package ch.heigvd;

import ch.heigvd.controllers.CountryController;
import ch.heigvd.controllers.RecipeController;
import ch.heigvd.entities.Country;
import ch.heigvd.entities.Recipe;
import ch.heigvd.repositories.CountryRepository;
import ch.heigvd.repositories.RecipeRepository;
import io.javalin.Javalin;
import java.util.List;

public class Main {
  public static final int PORT = 8080;

  public static void main(String[] args) {
    Javalin app = Javalin.create();

    RecipeRepository recipeRepository = new RecipeRepository();
    CountryRepository countryRepository = new CountryRepository(recipeRepository);
    RecipeController recipeController = new RecipeController(recipeRepository, countryRepository);
    CountryController countryController = new CountryController(countryRepository);

    recipeRepository.newRecipe(
        new Recipe(
            "Spaghetti carbonara",
            30,
            "Sauce is composed with eggs, pancetta and parmesan cheese",
            List.of("main dish")));
    recipeRepository.newRecipe(
        new Recipe(
            "Fondue moitié-moitié",
            25,
            "Composed of Vacherin Fribourgeois and Gruyère AOP",
            List.of("vegetarian", "alcohol")));
    recipeRepository.newRecipe(
        new Recipe(
            "Taboulé",
            25,
            "The traditional delicious recipe",
            List.of("vegetarian", "vegan", "salad")));
    recipeRepository.newRecipe(
        new Recipe(
            "Rösti",
            50,
            "Can be served with fried egg on top",
            List.of("vegetarian", "gluten free")));
    recipeRepository.newRecipe(
        new Recipe(
            "Ratatouille",
            80,
            "Mix of delicious vegetables",
            List.of("vegetarian", "vegan", "gluten free")));
    recipeRepository.newRecipe(
        new Recipe("Omelette", 10, "Beaten eggs cooked in a pan", List.of("vegetarian")));

    countryRepository.newCountry(new Country("CHE", "Switzerland", List.of(2, 4)));
    countryRepository.newCountry(new Country("ITA", "Italy", List.of(1)));
    countryRepository.newCountry(new Country("LBN", "Lebanon", List.of(3)));
    countryRepository.newCountry(new Country("FRA", "France"));

    app.get("/recipes", recipeController::getRecipes);
    app.post("/recipes", recipeController::addRecipe);
    app.get("/recipes/{id}", recipeController::getById);
    app.patch("/recipes/{id}", recipeController::patchRecipe);
    app.delete("/recipes/{id}", recipeController::deleteRecipe);

    app.post("/countries", countryController::newCountry);
    app.get("/countries", countryController::getAllCountries);
    app.get("/countries/{code}", countryController::getOneCountry);
    app.patch("/countries/{code}", countryController::patchCountry);
    app.delete("/countries/{code}", countryController::deleteCountry);

    app.get("/countries/{code}/recipes", countryController::getRecipesFromCountry);
    app.post("/countries/{code}/recipes", countryController::linkRecipesToCountry);
    app.delete("/countries/{code}/recipes", countryController::dissociateRecipesFromCountry);

    app.start(PORT);
  }
}
