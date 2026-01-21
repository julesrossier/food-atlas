# Food Atlas

Food Atlas is a HTTP API that is intended to list various recipes around the world.

The API definition can be found in [this document](doc/api-specs.md). Also, a Swagger UI is displayed as a welcome page when the application is deployed and linked to Traefik.

## Table of contents

* [Food Atlas](#food-atlas)
* [Table of contents](#table-of-contents)
* [Installing and launching the application](#installing-and-launching-the-application)
  * [From source](#from-source-api-only)
  * [With Docker and Docker Compose](#with-docker-and-docker-compose)
* [Usage example of API](#usage-examples-of-api)
  * [Create a new recipe](#create-a-new-recipe)
  * [Get all the recipes](#get-all-the-recipes)
  * [Get recipes filtered by time](#get-recipes-filtered-by-time)
  * [Get recipes filtered by labels](#get-recipes-filtered-by-labels)
  * [Get a specific recipe](#get-a-specific-recipe)
  * [Modifying a recipe](#modifying-a-recipe)
  * [Get all the recipes from a country](#get-all-the-recipes-from-a-country)
  * [Link recipes to a country](#link-recipes-to-a-country)
* [Contributing](#contributing)
* [Authors](#authors)

## Installing and launching the application

### From source (API only)

**IMPORTANT**: This project has been developed and tested with the Java Temurin 21 JDK. We do not guarantee our application works with another one.

First clone the project :

```sh
git clone git@github.com:julesrossier/food-atlas.git
```

Then move into the main folder and compile the package.

```sh
cd food-atlas
./mvnw dependency:go-offline clean compile package
```

You can now lauch it:

```sh
java -jar target/food-atlas*.jar [-p <port>]
```
Where `-p` is the flag to define the port where the application listens (optional, defaults to 8080).

## With Docker and Docker compose

In the project root directory, create a `.env` file with this content:

```
FQDN=<fully qualified domain name>
```
If you launch in local, put `localhost`.

Create also a `.env` file in `traefik/` directory, with this content :

```
FQDN=<fully qualified domain name>
TRAEFIK_DASHBOARD_PASSWD=<set password for traefik dashboardY
```

Then :

```sh
# launch traefik
docker compose -f traefik/compose.yaml up -d
# launch api
docker compose up -d
```

### Deployment

#### VPS

To setup your server see [VPS](./doc/SETUP.md#vps)

#### DNS records

To setup your DNS see [DNS](./doc/SETUP.md#dns)

## Usage examples of API

### Create a new recipe

```sh
curl -X POST -d '{"name": "Escargots au beurre persillé", "labels": ["bourgogne"], "time": 30, "description": "Very atypical, but very delicious !"}' https://food-atlas.cc/recipes
```

Output (in the implementation, forgot to actually return something):
```
{
    "id": 7,
    "name": "Escargots au beurre persillé",
    "labels": ["bourgogne"],
    "time": 30,
    "description": "Very atypical, but very delicious !"
}
```

### Get all the recipes

```shell
curl https://food-atlas.cc/recipes
```

Output:

```
[
    {"id":1,"name":"Spaghetti carbonara","time":30,"description":"Sauce is composed with eggs, pancetta and parmesan cheese","labels":["maindish"]},
    {"id":2,"name":"Fondue moitié-moitié","time":25,"description":"Composed of Vacherin Fribourgeois and Gruyère AOP","labels":["vegetarian","alcohol"]},
    {"id":3,"name":"Taboulé","time":25,"description":"The traditional delicious recipe","labels":["vegan","vegetarian","salad"]},
    {"id":4,"name":"Rösti","time":50,"description":"Can be served with fried egg on top","labels":["vegetarian","glutenfree"]},
    {"id":5,"name":"Ratatouille","time":80,"description":"Mix of delicious vegetables","labels":["vegan","vegetarian","glutenfree"]},
    {"id":6,"name":"Omelette","time":10,"description":"Beaten eggs cooked in a pan","labels":["vegetarian"]},
    {"id":7,"name":"Escargots au beurre persillé","time":30,"description":"Very atypical, but very delicious !","labels":["bourgogne"]}
]
```

### Get recipes filtered by time

```sh
curl https://food-atlas.cc/recipes?max_time=30
```

Output:

```
[
    {"id":1,"name":"Spaghetti carbonara","time":30,"description":"Sauce is composed with eggs, pancetta and parmesan cheese","labels":["maindish"]},
    {"id":2,"name":"Fondue moitié-moitié","time":25,"description":"Composed of Vacherin Fribourgeois and Gruyère AOP","labels":["vegetarian","alcohol"]},
    {"id":3,"name":"Taboulé","time":25,"description":"The traditional delicious recipe","labels":["vegan","vegetarian","salad"]},
    {"id":6,"name":"Omelette","time":10,"description":"Beaten eggs cooked in a pan","labels":["vegetarian"]},
    {"id":7,"name":"Escargots au beurre persillé","time":30,"description":"Very atypical, but very delicious !","labels":["bourgogne"]}
]
```

### Get recipes filtered by labels

```shell
curl https://food-atlas.cc/recipes?labels=vegetarian,glutenfree
```

Output:

```
[
    {"id":4,"name":"Rösti","time":50,"description":"Can be served with fried egg on top","labels":["vegetarian","glutenfree"]},
    {"id":5,"name":"Ratatouille","time":80,"description":"Mix of delicious vegetables","labels":["vegan","vegetarian","glutenfree"]}
]
```

### Get a specific recipe

```shell
curl https://food-atlas.cc/recipes/3
```

Output:

```
{
    "id":3,
    "name":"Taboulé",
    "time":25,
    "description":"The traditional delicious recipe",
    "labels":["vegan","vegetarian","salad"]
}
```

### Modifying a recipe

```shell
curl -X PATCH -d '{"description": "New desciption for Taboulé"}' https://food-atlas.cc/recipes/3
```

Output:

```
{
    "id":3,
    "name":"Taboulé",
    "time":25,
    "description":"New description for Taboulé",
    "labels":["vegan","vegetarian","salad"]
}
```

### Get all the recipes from a country

```shell
curl https://food-atlas.cc/LBN/recipes
```

### Link recipes to a country

```shell
curl -X POST -d '[5,6,7]' https://food-atlas.cc/FRA/recipes
```

## Contributing

Contributions via issues and pull requests are welcome.

### Build Docker image and push it to Github Container Registry

To build the Docker image, when in project folder :

```sh
$ docker build -t food-atlas .
```

To push the Image (you need to be logged in to Github Container Registry) :


```sh
$ docker tag dai-go ghcr.io/yanniskawronski/food-atlas:<tag>
$ docker push ghcr.io/yanniskawronski/food-atlas:<tag>
```

`<tag>` is the tag you want to put to your image.

## Authors

- [Yanni Skawronski](https://github.com/yanniSkawronski)
- [Tadeusz Kondracki](https://github.com/GlysVenture)
- [Jules Rossier](https://github.com/julesrossier)
