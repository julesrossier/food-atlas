package ch.heigvd.repositories;

import ch.heigvd.entities.Country;
import ch.heigvd.entities.Recipe;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ConflictResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.NotModifiedResponse;
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
    countries.put(country.code(), country);
    return country;
  }

  public List<Country> getAllCountries() {
    return countries.values().stream().toList();
  }

  public Country getCountryByCode(String countryCode) {
    Country country = countries.get(countryCode);
    if (country == null) throw new NotFoundResponse();
    return country;
  }

  public void updateCountry(String countryCode, Country newValues) {
    Country oldCountry = getCountryByCode(countryCode);
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
    Country countryToAdd =
        new Country(
            countryCode,
            nameEntry != null && !nameEntry.isEmpty() ? nameEntry : oldCountry.name(),
            !recipesEntry.isEmpty() ? recipesEntry : oldCountry.recipes());

    countries.put(countryCode, countryToAdd);
  }

  public void deleteCountry(String countryCode) {
    Country country = getCountryByCode(countryCode);
    if (country.recipes() != null && !country.recipes().isEmpty()) {
      throw new NotModifiedResponse("The country queried is linked to at least one recipe");
    }
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
    Country oldCountry = getCountryByCode(countryCode);

    Set<Integer> newRecipesSet = new HashSet<>(oldCountry.recipes());

    for (Integer recipeId : recipeIds) {
      if (!recipeRepository.existsById(recipeId)) {
        throw new NotFoundResponse("Recipe with id " + recipeId + " not found");
      }
      newRecipesSet.add(recipeId);
    }
    Country countryToAdd = new Country(countryCode, oldCountry.name(), newRecipesSet);
    countries.put(countryCode, countryToAdd);
  }

  public void dissociateRecipesFromCountry(String countryCode) {
    Country oldCountry = getCountryByCode(countryCode),
        newCountry = new Country(countryCode, oldCountry.name(), new HashSet<>());
    countries.put(countryCode, newCountry);
  }

  public boolean isRecipeLinkedToCountry(Integer recipeId) {
    for (String code : countries.keySet()) {
      for (Integer storedId : countries.get(code).recipes()) {
        if (storedId.equals(recipeId)) return true;
      }
    }
    return false;
  }
}
