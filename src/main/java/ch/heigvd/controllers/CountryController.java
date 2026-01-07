package ch.heigvd.controllers;

import ch.heigvd.entities.Country;
import ch.heigvd.repositories.CountryRepository;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.CreatedResponse;
import io.javalin.http.NoContentResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountryController {
    private final CountryRepository countryRepository;

    public CountryController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public void newCountry(Context ctx) {
        Country entry = ctx.bodyValidator(Country.class)
                .check(country -> country.getCode() != null, "No country code given")
                .check(country -> country.getName() != null, "No country name given")
                .get();
        ctx.json(countryRepository.newCountry(entry));
        throw new CreatedResponse();
    }

    public void getAllCountries(Context ctx) {
        ctx.json(countryRepository.getAllCountries());
    }

    public void getOneCountry(Context ctx) {
        String countryCode = ctx.pathParam("code");
        ctx.json(countryRepository.getCountryByCode(countryCode));
    }

    public void patchCountry(Context ctx) {
        String countryCode = ctx.pathParam("code");
        Country newEntry = ctx.bodyAsClass(Country.class);
        countryRepository.updateCountry(countryCode, newEntry);
        throw new NoContentResponse();
    }

    public void deleteCountry(Context ctx) {
        String countryCode = ctx.pathParam("code");
        countryRepository.deleteCountry(countryCode);
        throw new NoContentResponse();
    }

    public void getRecipesFromCountry(Context ctx) {
        ctx.json(countryRepository.getRecipesFromCountry(ctx.pathParam("code")));
    }

    public void linkRecipesToCountry(Context ctx) {
        String countryCode = ctx.pathParam("code");

        List<Integer> recipesIds = new ArrayList<>();
        String recipesIdsEntry = ctx.queryParam("recipesIds");
        if(!recipesIdsEntry.isEmpty()) {
            try {
                recipesIds = Arrays.stream(recipesIdsEntry.split(","))
                        .toList().stream()
                        .map(Integer::parseInt)
                        .toList();
            }
            catch(NumberFormatException e) {
                throw new BadRequestResponse("Invalid request path parameter");
            }
        }

        countryRepository.linkRecipesToCountry(countryCode, recipesIds);
        throw new NoContentResponse();
    }

    public void dissociateRecipesFromCountry(Context ctx) {
        countryRepository.dissociateRecipesFromCountry(ctx.pathParam("code"));
        throw new NoContentResponse();
    }
}
