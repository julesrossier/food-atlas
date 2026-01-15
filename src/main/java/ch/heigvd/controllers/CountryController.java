package ch.heigvd.controllers;

import ch.heigvd.entities.Country;
import ch.heigvd.repositories.CountryRepository;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountryController {
  private final CountryRepository countryRepository;

  public CountryController(CountryRepository countryRepository) {
    this.countryRepository = countryRepository;
  }

  public void newCountry(Context ctx) {
    Country entry =
        ctx.bodyValidator(Country.class)
            .check(country -> country.code() != null, "No country code given")
            .check(country -> country.name() != null, "No country name given")
            .get();
    ctx.json(countryRepository.newCountry(entry));
    ctx.status(201);
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
    ctx.status(204);
  }

  public void deleteCountry(Context ctx) {
    String countryCode = ctx.pathParam("code");
    countryRepository.deleteCountry(countryCode);
    ctx.status(204);
  }

  public void getRecipesFromCountry(Context ctx) {
    ctx.json(countryRepository.getRecipesFromCountry(ctx.pathParam("code")));
  }

  public void linkRecipesToCountry(Context ctx) {
    String countryCode = ctx.pathParam("code");

    List<Integer> recipesIds = new ArrayList<>();
    String recipesIdsEntry = ctx.queryParam("recipesIds");
    if (!recipesIdsEntry.isEmpty()) {
      try {
        recipesIds =
            Arrays.stream(recipesIdsEntry.split(",")).toList().stream()
                .map(Integer::parseInt)
                .toList();
      } catch (NumberFormatException e) {
        throw new BadRequestResponse("Invalid request path parameter");
      }
    }

    countryRepository.linkRecipesToCountry(countryCode, recipesIds);
    ctx.status(204);
  }

  public void dissociateRecipesFromCountry(Context ctx) {
    countryRepository.dissociateRecipesFromCountry(ctx.pathParam("code"));
    ctx.status(204);
  }
}
