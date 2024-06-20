# Tic Tac toe


## Backend

- Springboot version 3.3.0
- JVM version 21  [Amazon Corretto JDK 21](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html)
- Kotlin
- IDE: Intellij IDEA
- PostgreSQL
- latest Gradle

To run backend project do the following:

### Initialize database

- Create database with name `tec-tac-toe` then make sure it works on port 5432
- Set the PostgreSQL user name in `spring.r2dbc.username` and the password in `spring.r2dbc.password` in application.properties in the resource folder

### Run project


```bash
./gradlew bootRun
```

### Build
```bash
./gradlew build
```
---

## Frontend

- React js version 18.2.66
- Vite version 5.2.0

#### Install dependencies
```bash
yarn install
```

#### Run project

```bash
yarn dev --host
```

#### Build

```bash
yarn build
```
