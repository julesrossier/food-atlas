# Food Atlas

Food Atlas is a HTTP API that is intended to list various recipes around the world.

It is based on the protocol defined in [this document](doc/api-specs.md).


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

## Contributing

Contributions via issues and pull requests are welcome.

### Build Docker image and push it to Github Container Registry

To build the Docker image, when in project folder :

```sh
$ docker build -t food-atlas .
```

To push the Image (you need to be logged in to Github Container Registry) :

**TODO**: modify paths when we have package in right place.

```sh
$ docker tag dai-go ghcr.io/yanniskawronski/food-atlas:<tag>
$ docker push ghcr.io/yanniskawronski/food-atlas:<tag>
```

`<tag>` is the tag you want to put to your image.

## Authors

- [Yanni Skawronski](https://github.com/yanniSkawronski)
- [Tadeusz Kondracki](https://github.com/GlysVenture)
- [Jules Rossier](https://github.com/julesrossier)
