# SENG302 Gardener's Grove - team 1000

Basic project using ```gradle```, ```Spring Boot```, ```Thymeleaf```, and ```GitLab CI```.

> Gardener's Grove is a web application that allows gardeners to keep track of their plants.
> Users can create and manage their personal gardens as well as view other user's gardens.
> This project is a part of the SENG302 course at the University of Canterbury.

## Dependencies
This project requires Java version >= 21

## Deployment
Current version of the product uses h2 database.
Works on production and test, but does not use the persistent database.

### Configuration variables for email:
> EMAIL_PASSWORD=ngwj lqqa fngw gjsl
> 
> EMAIL_PORT=587
> 
> EMAIL_USERNAME=gardensgroveteam1000@gmail.com

## Test User Information:
| USERNAME | PASSWORD |
| ------ | ------ |
|    test@example.com  |   Password1234!     |

## How to run

### 1 - Running the project

From the root directory

On Linux:

```sh
./gradlew bootRun
```

On Windows:

```
gradlew bootRun
```

By default, the application will run on local port 8080 [http://localhost:8080](http://localhost:8080)

### 2 - Using the application

> Click on the register button to create a new user profile. After this you are able to interact with your profile and make changes.
> 
> You may sign out and create different users and sign in to different accounts.  
Note: The database in currently not persistent so all data will be lost when the server is stopped.

## How to run tests

From the root directory:

On Linux:

```sh
./gradlew test
```

On Windows:

```sh
gradlew test
```

## Todo (Sprint 2)

- Update team name into `build.gradle`
- Set up Gitlab CI server (refer to the student guide on Scrumboard)
- Decide on a LICENSE

## Contributors

- SENG302 Teaching Team
- Sergey Antonets (san177)
- Jake van Keulen (jva83)
- Elena Joeng (cjo108)
- Zipporah Price (zpr12)
- Hanan Fokkens (hfo22)
- Stephen Hockey (sho151)
- Ahmick Montana (amo255)

## References

- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring JPA docs](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Thymeleaf Docs](https://www.thymeleaf.org/documentation.html)
- [Learn resources](https://learn.canterbury.ac.nz/course/view.php?id=17797&section=8)
