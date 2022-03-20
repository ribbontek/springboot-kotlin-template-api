# Template API

### High Level Overview
A Kotlin SpringBoot App
- JPA, Liquibase, Postgres
- CQRS, Event Sourcing
- Docs - Swagger, Redoc, OpenAPI

### API Design
![Alt text](doc-api-design.png?raw=true "API Design")

### ERD Design
![Alt text](doc-erd.png?raw=true "ERD Design")

### Build Prerequisites:

Ensure to have the following installed on your machine
* docker, docker-compose

Any issues with docker commands on linux:  
- `sudo chmod 666 /var/run/docker.sock`

### Build

Run a build without any tests initially
```bash
./gradlew clean build -x integrationTest -x test -i
```

### Test

Run the Unit Tests
```bash
./gradlew test
```

Run the Integration Tests
```bash
./gradlew -x test integrationTest
```

### Running Locally

First run a build without tests & start up the docker-compose dependencies
```bash
./gradlew composeUp
```

Now run through Intellij OR
Run the SpringBoot App through Gradle OR
```bash
./gradlew bootRun
```

Add these properties to the gradle-wrapper.properties file if you wish to debug that way without IntelliJ doing the lifting here
```
org.gradle.daemon=true
org.gradle.jvmargs=-XX:MaxPermSize=4g -XX:+HeapDumpOnOutOfMemoryError -Xmx4g -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5006
```

Run the Jar
```bash
java -jar ./build/libs/*-final.jar 
```

### API Documentation

Redoc
- Found at: `http://localhost:8080/redoc`
- Enabled through the properties file: `redoc.enabled: true`

Open API
- Found at: `http://localhost:8080/api-docs`

Swagger UI
- Found at: `http://localhost:8080/swagger-ui.html`
- Enabled through the properties file: `springdoc.swagger-ui.enabled: true`

Maintain the draw.io design diagrams
* Install draw.io on your machine; &/or
* Viewing/Editing the diagram via IntelliJ `https://plugins.jetbrains.com/plugin/15635-diagrams-net-integration`


### Performing a Release

Run the versioning command
```bash
./gradlew -x test -x integrationTest final cacheNebulaVersion
```

Build the Dockerfile with the appropriate tags
```bash
docker build -t $(basename "$PWD"):latest -t $(basename "$PWD"):$(git describe --tags) -f docker/Dockerfile .
```

Use either of the following commands to login (alias ecrlogin)
```bash 
aws ecr get-login-password | docker login --username AWS --password-stdin <your-id>.dkr.ecr.<your-region>.amazonaws.com
```

Create the docker tags & push to ECR
```bash
docker tag $(basename "$PWD"):latest <your-id>.dkr.ecr.<your-region>.amazonaws.com/$(basename "$PWD"):latest
docker tag $(basename "$PWD"):$(git describe --tags) <your-id>.dkr.ecr.<your-region>.amazonaws.com/$(basename "$PWD"):$(git describe --tags)

docker push <your-id>.dkr.ecr.<your-region>.amazonaws.com/$(basename "$PWD"):latest
docker push <your-id>.dkr.ecr.<your-region>.amazonaws.com/$(basename "$PWD"):$(git describe --tags)
```

##### Once off ECR set up
Create the ecr repository for the project:
```bash
aws ecr create-repository \
--repository-name $(basename "$PWD") \
--image-scanning-configuration scanOnPush=true
```

#### Related notes

Doing the soft reset replaces the commit id with the previous tag, so you have to delete & set up your tags all over again in the repo.

- Add a tag to the initial commit
`git tag v1.0.0 <repo-commit-id>`
- Push tags to repo
`git push --tags`
- Delete tags locally 
`git tag -d v0.1.0`
- Delete tags remotely
`git push --delete origin v0.2.0`
- Use the 'useLastTag' if resetting from a new git version
`gw -x test -x integrationTest final -Prelease.useLastTag=true cacheNebulaVersion`
