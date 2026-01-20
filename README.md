to launch locally

create `.env` with

```
FQDN=
```
and `traefik/.env`

mandatory to at least set the variables:
```
FQDN=
TRAEFIK_DASHBOARD_PASSWD=
```

then

```
# launch traefik
docker compose -f traefik/compose.yaml up -d
# launch api
docker compose up -d
```
