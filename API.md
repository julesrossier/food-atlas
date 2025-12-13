# Food Atlas

## Entities

Recipes and countries must be linked.

### Recipe
- id : integer
- name : string
- ingredients : array of string
- algoritm : string
- preparation time : integer

### Country
- id : string (following one of the ISO country code type)
- name : string
- continent : string
- recipesIds : array of integers


## Endpoints

### Create a recipe

```
POST /recipes
```

Create a new recipe.

#### Request

Request body will consist of a JSON containing the following fields :
- `name` : the name of the recipe
- `algorithm` : the steps to accomplish the recipe
- `ingredients` : the list of ingredients needed
- `preparation_time` : time needed to prepare the dish

#### Response

Response is a JSON containing the following fields :
- `id` : identifier of the recipe
- `name` : the name of the recipe
- `algorithm` : the steps to accomplish the recipe
- `ingredients` : the list of ingredients needed
- `preparation_time` : time needed to prepare the dish

#### Status codes

- `201` (Created) - The recipe has been successfully created
- `400` (Bad Request) - The request body is invalid
- `409` (Conflict) - There is already a recipe with the name given

### Get all recipe names

```
GET /recipes
```

Get the list of names of all recipes.

#### Request

The request can contain the following query parameters:

- `continent` : filter the recipes by continent
- `country` : filter the recipes by country
- `max_time_prep` : filter the recipe

#### Response

A JSON array with the following fields :

- `id` : identifier of the recipe
- `name` : name of the recipe

#### Status codes

- `200` (OK) - Request successful
- `400` (Bad request) - Request parameters are invalid (if continent and country are given, returns 400 if the country is not in the continent)

### Get details of a recipe

```
GET /recipes/{id}/details
``` 

Get all the details of the recipe with the ID entered.

#### Request

The request path must contain the ID of the recipe needed.

#### Response

The response contains a JSON object with the following fields :

Response is a JSON containing the following fields :
- `id` : identifier of the recipe
- `name` : the name of the recipe
- `algorithm` : the steps to accomplish the recipe
- `ingredients` : the list of ingredients needed
- `preparation_time` : time needed to prepare the recipe

#### Status codes

- `200` (OK) - Request successful
- `404` (Not Found) - Recipe not found


### Update a recipe

```
PUT /recipes/{id}
```

Update the requested recipe with the values given in body request.

#### Request

- The request path must contain the ID of the recipe we want to modify
- The request body must be a JSON with **at least** one of these fields :
  - `name` : a string with the new name of the recipe
  - `algorithm` : a string with the new steps to accomplish the recipe
  - `ingreditents` : a JSON array containing the new ingredients of the recipe (ingredients in string format)
  - `preparation_time` : a number with the new preparation time of the recipe

#### Response

The response contains a JSON containing the following fields  :

- `id` : identifier of the updated recipe
- `name` : the name of the updated recipe
- `algorithm` : the steps to accomplish the updated recipe
- `ingredients` : the list of ingredients needed for the updated recipe
- `preparation_time` : preparation time of the updated recipe

### Delete a recipe

```
DELETE /recipes/{id}
```

Delete the recipe with id given in the path request.

#### Request

The request path must contain the id of the recipe we want to delete.

#### Response

The response body contains a JSON with all the informations about the deleted recipe :

- `id` : identifier of the recipe
- `name` : the name of the recipe
- `algorithm` : the steps to accomplish the recipe
- `ingredients` : the list of ingredients needed
- `preparation_time` : time needed to prepare the dish

#### Status codes

- `200` (OK) : The request is successful
- `404` (Not found) : Country not found

### Create a country

```
POST /countries
```

Create a new country.

#### Request

The request body must contain a JSON with the following fields :

- `name` : the name of the country
- `continent` : the continent of the country
- `recipes_ids` : a JSON array containing the ids of the recipes we want to associate to the country

#### Response

The response contains a JSON with the following fields :

- `id` : the identifier of the new country
- `name` : the name of the new country
- `continent` : the continent of the new country
- `recipes_ids` : the ids of the recipes associated to the new country

#### Status codes

- `201` (Created) : The request is successful
- `400` (Bad Request) : The request body is invalid

### List all countries

```
GET /countries
```

Get a list of countries, optionally filtered by continent.

#### Request

The request can contain the following query parameter :

- `continent` : filter the countries by continent

#### Response

The response contains a JSON array with the following fields :

- `id` : the identifier of the country
- `name` : the name of the country
- `continent` : the continent of the country

#### Status codes

- `200` (OK) Request is successful

### Get one country by its ID

```
GET /countries/{id}/details
```

Get detailed informations about the country queried.

#### Request

The request path must contain the id of the country queried.

#### Response

The response is a JSON containing the following fields :

- `id` : the identifier of the country
- `name` : the name of the country
- `continent` : the continent of the country

#### Status codes

- `200` (OK) : The request was successful
- `404` (Not found) : The country queried does not exist

### Update a country

```
PUT /countries/{id}
```

#### Request

The request path must contain the id of the country we want to update

#### Response

Blablabla

#### Status codes

- `200` (OK) : The request was successful
- `404` (Not found) : The country queried does not exist

### Delete a country

```
DELETE /countries/{id}
```

The country can be deleted only if it does not have any recipe linked.

#### Request

The request path must contain the id of the country we want to update

#### Response

The response contains a JSON object containing the following fields :

- `id` : the identifier of the country
- `name` : the name of the country
- `continent` : the continent of the country

#### Status codes

- `200` (OK) : The request was successful
- `304` (Not modified) : The country queried is linked to at least one recipe
- `404` (Not found) : The country queried does not exist

### Link a recipe to a country

```
PUT /countries/{countryId}/addRecipes
```

Link some recipes to a country.

#### Request

The request path must contain the id of the country we want to add recipes. The path parameter must contain the following field :

- `recipesIds` : The list of ids associated to the recipe we want to link to the country.

#### Response

None.

#### Status codes

- `200` (OK) : Request successful
- `400` (Bad Request) : The path parameter is invalid
- `404` (Not Found) : Either the country or one of the recipes are not found.
