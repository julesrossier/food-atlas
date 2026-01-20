package ch.heigvd;

import ch.heigvd.controllers.CountryController;
import ch.heigvd.controllers.RecipeController;
import ch.heigvd.entities.Country;
import ch.heigvd.entities.Recipe;
import ch.heigvd.repositories.CountryRepository;
import ch.heigvd.repositories.RecipeRepository;
import io.javalin.Javalin;
import java.util.Set;
import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class FoodAtlasApp implements Callable<Integer> {
  private final RecipeRepository recipeRepository = new RecipeRepository();
  private final CountryRepository countryRepository = new CountryRepository(recipeRepository);
  private final RecipeController recipeController =
      new RecipeController(recipeRepository, countryRepository);
  private final CountryController countryController = new CountryController(countryRepository);
  private final Javalin app = Javalin.create();

  @Option(
      names = {"-p", "--port"},
      defaultValue = "8080",
      description = "The port used by the Food Atlas application.")
  private int port;

  @Option(
      names = "--populate-with-data",
      description = "If this flag is inserted, application will be populated with some datas.")
  private boolean populateWithData;

  private void dataPopulation() {
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
  }

  public Integer call() throws Exception {
    if (populateWithData) {
      dataPopulation();
    }

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
    app.start(port);
    return 0;
  }

  public static void main(String[] args) {
    FoodAtlasApp foodAtlasApp = new FoodAtlasApp();
    new CommandLine(foodAtlasApp).execute(args);
  }
}
