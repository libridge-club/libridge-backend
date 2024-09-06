# libridge - a free/libre, open-source bridge server

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=rulojuka_libridge-backend&metric=bugs)](https://sonarcloud.io/summary/new_code?id=rulojuka_libridge-backend)

## About the project

libridge is a free/libre, open-source bridge server and it is (soon) available in [libridge.club](https://libridge.club/).

libridge uses [ben](https://github.com/lorserker/ben) for bidding suggestions (and more in the future). Don't forget to check out this awesome bridge engine.

## Java version

libridge-backend uses Java 21 (LTS) in development and in runtime. It will be updated to Java 25 LTS after its launch at September 2025.

## Compiling

libridge uses Makefile and [Maven](https://maven.apache.org/) to build. The following line should be enough:

```
make
```

This should clean your directory, compile, run all tests, package the final .jar at the `target/` directory and copy it into `./libridge-server.jar`.

### Setting the environment and running
And then `java -Dspring.profiles.active=development -jar ./libridge-server.jar` to run the development server.

## Only unit tests

If you'd like to run only the unit tests:

```
mvn test
```

## Code quality

You can also run checks to verify the package is valid and meets quality criteria.

### JaCoCo

```
mvn verify
```

This will include [JaCoCo](https://www.jacoco.org/jacoco/)'s code coverage report at `target/site/jacoco/index.html`

### SpotBugs
You can see potential bugs with [SpotBugs](https://spotbugs.github.io/) running:

```
mvn spotbugs:gui
```

### pitest
To execute mutation tests using [pitest](https://pitest.org/) run:  

```
mvn test-compile org.pitest:pitest-maven:mutationCoverage
```

and see the coverage report at `target/pit-reports/`

## Using Docker

If you prefer to user Docker, you can
```
make build
```
to build the server image and
```
make run
```
to run the server on the default port.

You can also pull the image directly from the [Docker Hub](https://hub.docker.com/r/rulojuka/libridge-backend).

If you want to just start the server on the default port:
```
docker run -p 8080:8080 rulojuka/libridge-backend
```

### On DigitalOcean
Create a [docker based droplet](https://marketplace.digitalocean.com/apps/docker) and
```
docker run --network host -e DATABASE_URL='url' -e DATABASE_USERNAME='username' -e DATABASE_PASSWORD='password' -d -p 8080:8080 rulojuka/libridge-backend
```

### Pushing to DockerHub
```
make deploy
```

## Authors and copyright

### Authors:
See file AUTHORS

### Copyright information:
See file COPYRIGHT

### Full license text:
See file COPYING
