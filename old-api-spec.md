# Food Atlas

## Entities

Recipes and countries must be linked.

### Recipe
- id : integer
- name : string
- labels : array of string
- time : integer
- description : string

### Country
- id : string (following one of the ISO country code type)
- name : string
- recipes : array of integers


## Endpoints

### Create a recipe

```
POST /recipes
```

Create a new recipe.

#### Request

Request body will consist of a JSON containing the following fields :
- `name` : the name of the recipe
- `labels` : the labels we want to give to the recipe
- `time` : time needed to prepare the dish
- `description` : the description of the recipe

#### Response

Response is a JSON containing the following fields :
- `id` : identifier of the recipe
- `name` : the name of the recipe
- `labels` : the list of ingredients needed
- `time` : time needed to prepare the dish
- `description` : description of the recipe

#### Status codes

- `201` (Created) - The recipe has been successfully created
- `400` (Bad Request) - The request body is invalid
- `409` (Conflict) - There is already a recipe with the name given

### Get recipes

```
GET /recipes
```

Get a list of recipes.

#### Request

The request can contain the following query parameters:

- `max_time` : get only the recipes ready in under a certain time
- `labels` : filter the recipes by labels

#### Response

A JSON array with the following fields :

- `id` : identifier of the recipe
- `name` : name of the recipe
- `labels` : labels of the recipe
- `time` : preparation time of recipe
- `description` : description of the recipe

#### Status codes

- `200` (OK) - Request successful
- `400` (Bad request) - Request parameters are invalid

### Get a specific recipe

```
GET /recipes/{id}
``` 

Get all the details of the recipe with the ID entered.

#### Request

The request path must contain the ID of the recipe needed.

#### Response

The response contains a JSON object with the following fields :

Response is a JSON containing the following fields :
- `id` : identifier of the recipe
- `name` : the name of the recipe
- `labels` : the labels of the recipe
- `time` : time needed to prepare the recipe
- `description` : description of the recipe

#### Status codes

- `200` (OK) - Request successful
- `404` (Not Found) - Recipe not found


### Update a recipe

```
PATCH /recipes/{id}
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

No response body

#### Status codes

- `204` (No content) - The request is successful
- `400` (Bad request) - Request body is invalid
- `404` (Not found) - Recipe not found

### Delete a recipe

```
DELETE /recipes/{id}
```

Delete the recipe with id given in the path request.

#### Request

The request path must contain the id of the recipe we want to delete.

#### Response

No response body

#### Status codes

- `204` (No content) : The request is successful
- `404` (Not found) : Recipe not found

### Create a country

```
POST /countries
```

Create a new country.

#### Request

The request body must contain a JSON with the following fields :

- `code` : the [ISO 3166-1 alpha-3 code](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) of the country
- `name` : the name of the country
- `recipes_ids` (optional) : a JSON array containing the ids of the recipes we want to associate to the country

#### Response

The response contains a JSON with the following fields :

- `code` : the identifier of the new country
- `name` : the name of the new country
- `recipes_ids` : the ids of the recipes associated to the new country

#### Status codes

- `201` (Created) : The request is successful
- `400` (Bad Request) : The request body is invalid

### List all countries

```
GET /countries
```

Get a list of countries.

#### Response

The response contains a JSON array with the following fields :

- `code` : the identifier of the country
- `name` : the name of the country

#### Status codes

- `200` (OK) Request is successful

### Get one country by its ID

```
GET /countries/{code}
```

Get detailed informations about the country queried.

#### Request

The request path must contain the id of the country queried.

#### Response

The response is a JSON containing the following fields :

- `code` : the identifier of the country
- `name` : the name of the country
- `recipes_ids` : the ids of the recipes linked to the country

#### Status codes

- `200` (OK) : The request was successful
- `404` (Not found) : The country queried does not exist

### Update a country

```
PATCH /countries/{code}
```

#### Request

The request path must contain the id of the country we want to update. The request body must contain a JSON with the following fields :
- `name` : the name of the country
- `recipesIds` : the ids of the recipes

#### Response

No response body.

#### Status codes

- `204` (No content) : The request was successful
- `404` (Not found) : The country queried does not exist

### Delete a country

```
DELETE /countries/{code}
```

The country can be deleted only if it does not have any recipe linked.

#### Request

The request path must contain the id of the country we want to update

#### Response

No response body

#### Status codes

- `204` (No content) : The request was successful
- `304` (Not modified) : The country queried is linked to at least one recipe
- `404` (Not found) : The country queried does not exist

### Get all the recipes from a country

```
GET /countries/{code}/recipes
```

#### Request

The request path must contain the id of the country we want the recipes from.

#### Response

The response body contains a JSON array with the following fields :
- `id` : identifier of the recipe
- `name` : the name of the recipe
- `labels` : the list of ingredients needed
- `time` : time needed to prepare the dish

#### Status codes

- `200` (OK) : Request successful
- `400` (Bad request) : The path parameter is invalid
- `404` (Not found) : Country not found

### Link a recipe to a country

```
POST /countries/{code}/recipes
```

Link some recipes to a country.

#### Request

The request path must contain the id of the country we want to add recipes. The query parameter must contain the following field :

- `recipesIds` : The list of ids associated to the recipe we want to link to the country.

#### Response

None.

#### Status codes

- `200` (OK) : Request successful
- `400` (Bad Request) : The path parameter is invalid
- `404` (Not Found) : Either the country or one of the recipes are not found.


### Delete the link between a recipe and a country

```
DELETE /countries/{code}/recipes
```

#### Request

The request path must contain the id of the country we want to delete recipes.

#### Response

None.

#### Status codes

- `204` (No content) : The request was successful
- `400` (Bad request) : The request path is invalid
- `404` (Not found) : Country not found
