package ch.heigvd.repositories;

import ch.heigvd.entities.Country;
import ch.heigvd.entities.Recipe;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ConflictResponse;
import io.javalin.http.NotFoundResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CountryRepository {
  private final ConcurrentHashMap<String, Country> countries = new ConcurrentHashMap<>();

  private final RecipeRepository recipeRepository;

  public CountryRepository(RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }

  public Country newCountry(Country country) {
    for (String code : countries.keySet()) {
      if (code.equals(country.code()) || countries.get(code).name().equals(country.name())) {
        throw new ConflictResponse();
      }
    }
    return countries.putIfAbsent(country.code(), country);
  }

  public List<Country> getAllCountries() {
    return countries.values().stream().toList();
  }

  public Country getCountryByCode(String countryCode) {
    if (countries.containsKey(countryCode)) return countries.get(countryCode);
    throw new NotFoundResponse();
  }

  public void updateCountry(String countryCode, Country newValues) {
    if (!countries.containsKey(countryCode)) throw new NotFoundResponse();
    countries.computeIfPresent(
        countryCode,
        (k, oldCountry) -> {
          for (String code : countries.keySet()) {
            if (!countries.get(code).equals(oldCountry)
                && (code.equals(newValues.code())
                    || countries.get(code).name().equals(newValues.name()))) {
              throw new ConflictResponse();
            }
          }
          String nameEntry = newValues.name();
          Set<Integer> recipesEntry = newValues.recipes();
          for (Integer recipeId : recipesEntry) {
            if (!recipeRepository.existsById(recipeId)) {
              throw new BadRequestResponse();
            }
          }
          return new Country(
              countryCode,
              nameEntry != null && !nameEntry.isEmpty() ? nameEntry : oldCountry.name(),
              !recipesEntry.isEmpty() ? recipesEntry : oldCountry.recipes());
        });
  }

  public void deleteCountry(String countryCode) {
    if (!countries.containsKey(countryCode)) throw new NotFoundResponse();
    countries.remove(countryCode);
  }

  public List<Recipe> getRecipesFromCountry(String countryCode) {
    Country country = getCountryByCode(countryCode);

    List<Recipe> recipes = new ArrayList<>();

    for (int recipeId : country.recipes()) {
      recipes.add(recipeRepository.getOneById(recipeId));
    }
    return recipes;
  }

  public void linkRecipesToCountry(String countryCode, List<Integer> recipeIds) {
    if (!countries.containsKey(countryCode)) throw new NotFoundResponse();

    countries.computeIfPresent(
        countryCode,
        (k, oldCountry) -> {
          Set<Integer> newRecipesSet = new HashSet<>(oldCountry.recipes());

          for (Integer recipeId : recipeIds) {
            if (!recipeRepository.existsById(recipeId)) {
              throw new NotFoundResponse();
            }
            newRecipesSet.add(recipeId);
          }
          return new Country(countryCode, oldCountry.name(), newRecipesSet);
        });
  }

  public void dissociateRecipesFromCountry(String countryCode) {
    if (!countries.containsKey(countryCode)) throw new NotFoundResponse();
    countries.computeIfPresent(
        countryCode,
        (k, oldCountry) -> new Country(countryCode, oldCountry.name(), new HashSet<>()));
  }

  public void dissociateRecipeFromCountries(Integer recipeId) {
    if (!recipeRepository.existsById(recipeId)) throw new NotFoundResponse();
    countries.forEach(
        (countryCode, oldCountry) -> {
          HashSet<Integer> newRecipesSet = new HashSet<>(oldCountry.recipes());
          newRecipesSet.remove(recipeId);
          countries.put(countryCode, new Country(countryCode, oldCountry.name(), newRecipesSet));
        });
  }
}
