package ch.heigvd;

import ch.heigvd.controllers.CountryController;
import ch.heigvd.controllers.RecipeController;
import ch.heigvd.entities.Country;
import ch.heigvd.entities.Recipe;
import ch.heigvd.repositories.CountryRepository;
import ch.heigvd.repositories.RecipeRepository;
import io.javalin.Javalin;
import java.util.Set;

public class Main {
  public static final int PORT = 80;

  public static void main(String[] args) {
    Javalin app = Javalin.create();

    RecipeRepository recipeRepository = new RecipeRepository();
    CountryRepository countryRepository = new CountryRepository(recipeRepository);
    RecipeController recipeController = new RecipeController(recipeRepository, countryRepository);
    CountryController countryController = new CountryController(countryRepository);

    recipeRepository.newRecipe(
        new Recipe(
            null,
            "Spaghetti carbonara",
            30,
            "Sauce is composed with eggs, pancetta and parmesan cheese",
            Set.of("maindish")));
    recipeRepository.newRecipe(
        new Recipe(
            null,
            "Fondue moitié-moitié",
            25,
            "Composed of Vacherin Fribourgeois and Gruyère AOP",
            Set.of("vegetarian", "alcohol")));
    recipeRepository.newRecipe(
        new Recipe(
            null,
            "Taboulé",
            25,
            "The traditional delicious recipe",
            Set.of("vegetarian", "vegan", "salad")));
    recipeRepository.newRecipe(
        new Recipe(
            null,
            "Rösti",
            50,
            "Can be served with fried egg on top",
            Set.of("vegetarian", "glutenfree")));
    recipeRepository.newRecipe(
        new Recipe(
            null,
            "Ratatouille",
            80,
            "Mix of delicious vegetables",
            Set.of("vegetarian", "vegan", "glutenfree")));
    recipeRepository.newRecipe(
        new Recipe(null, "Omelette", 10, "Beaten eggs cooked in a pan", Set.of("vegetarian")));

    countryRepository.newCountry(new Country("CHE", "Switzerland", Set.of(2, 4)));
    countryRepository.newCountry(new Country("ITA", "Italy", Set.of(1)));
    countryRepository.newCountry(new Country("LBN", "Lebanon", Set.of(3)));
    countryRepository.newCountry(new Country("FRA", "France", Set.of()));

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
