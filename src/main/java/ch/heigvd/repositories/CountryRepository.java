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

public class CountryRepository {
    private final List<Country> countries = new ArrayList<>();

    private final RecipeRepository recipeRepository;

    public CountryRepository(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Country newCountry(Country country) {
        for(Country c : countries){
            if(c.getCode().equals(country.getCode()) || c.getName().equals(country.getName())){
                throw new ConflictResponse();
            }
        }
        countries.add(country);
        return country;
    }

    public List<Country> getAllCountries() {
        return List.copyOf(countries);
    }

    public Country getCountryByCode(String countryCode) {
        for (Country country : countries) {
            if(country.getCode().equals(countryCode)) return country;
        }
        throw new NotFoundResponse("Country with code " + countryCode + " not found");
    }

    public void updateCountry(String countryCode, Country newValues) {
        Country country = getCountryByCode(countryCode);
        for(Country c : countries){
            if(!c.equals(country) && (c.getCode().equals(country.getCode()) || c.getName().equals(country.getName()))){
                throw new ConflictResponse();
            }
        }
        if(country.getName() != null && !country.getName().isEmpty()) {
            country.setName(newValues.getName());
        }
        if(country.getRecipes() != null && !country.getRecipes().isEmpty()) {
            for(int recipeId : country.getRecipes()) {
                if(!recipeRepository.existsById(recipeId)) throw new BadRequestResponse("Recipe with id " + recipeId + " not found");
            }
            country.setRecipes(Set.copyOf(country.getRecipes()));
        }
    }

    public void deleteCountry(String countryCode) {
        Country country = getCountryByCode(countryCode);
        if(country.getRecipes() != null && !country.getRecipes().isEmpty()) {
            throw new NotModifiedResponse("The country queried is linked to at least one recipe");
        }
        countries.remove(country);
    }

    public List<Recipe> getRecipesFromCountry(String countryCode) {
        Country country = getCountryByCode(countryCode);

        List<Recipe> recipes = new ArrayList<>();

        for(int recipeId : country.getRecipes()) {
            recipes.add(recipeRepository.getOneById(recipeId));
        }
        return recipes;
    }

    public void linkRecipesToCountry(String countryCode, List<Integer> recipeIds) {
        Country country = getCountryByCode(countryCode);

        for(Integer recipeId : recipeIds) {
            if(!recipeRepository.existsById(recipeId)) {
                throw new NotFoundResponse("Recipe with id " + recipeId + " not found");
            }
            country.addRecipe(recipeId);
        }
    }

    public void dissociateRecipesFromCountry(String countryCode) {
        Country country = getCountryByCode(countryCode);

        country.setRecipes(new HashSet<>());
    }

    public boolean isRecipeLinkedToCountry(Integer recipeId) {
        for(Country country : countries) {
            for(Integer storedId : country.getRecipes()) {
                if(storedId.equals(recipeId)) return true;
            }
        }
        return false;
    }
}
