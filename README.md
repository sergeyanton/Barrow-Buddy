# SENG302 Team G - Gardens Grove Users
Basic project using ```gradle```, ```Spring Boot```, ```Thymeleaf```, and ```GitLab CI```.

> Gardens Grove user side application. This project is a part of the SENG302 course at the University of Canterbury. 
> It currently focuses on the development of a web application that allows users to create and manage their profile.


## How to run
### 1 - Running the project
From the root directory

On Linux:
```
./gradlew bootRun
```

On Windows:
```
gradlew bootRun
```

By default, the application will run on local port 8080

### 2 - Using the application
> Using a preferred browser connect to the application at [http://localhost:8080](http://localhost:8080). 
> Click on the register button to create a new user profile. After this you are able to interact with your profile and make changes.
> You may sign out and create different users and sign in to different accounts.  
> Note: The database in currently not persistent so all data will be lost when the server is stopped.

## How to run tests
From the root directory: 

On linux:
```
./gradlew test
```
On windows:
```
gradlew test
```

## Contributors

- SENG302 teaching team
- Sergey Antonets (san177)
- Jake van Keulen (jva83)
- Elena Joeng (cjo108)
- Zipporah Price (zpr12)

## References

- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring JPA docs](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Thymeleaf Docs](https://www.thymeleaf.org/documentation.html)
- [Learn resources](https://learn.canterbury.ac.nz/course/view.php?id=17797&section=8)
