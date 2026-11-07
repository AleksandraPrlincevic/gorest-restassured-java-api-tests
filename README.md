# goRest API Automation project

## Description:
This is a practice project built after learning API testing in Postman,
applying those concepts using REST Assured in IntelliJ IDEA.


## Tech Stack:
- Java (OpenJDK 26)
- IntelliJ IDEA 2026.1
- TestNG 7.12.0
- Maven
- REST Assured 6.0.1
- jackson-databind 2.22.1
- datafaker 2.7.0

## Prerequisites:
- IntelliJ IDEA
- Project SDK: OpenJDK 26

## Installation

Clone the repository in IDE terminal:

```bash
git clone https://github.com/AleksandraPrlincevic/gorest-restassured-java-api-tests.git
```
## How to run:
Create your own config.properties file with base.url, userParameter, postsParameter and
token inside src/test/resources.
Open the project in IntelliJ IDEA and run tests directly from the IDE.

## Framework Walkthrough

Packages:
* `pojo` - POJO classes: `Post`and `User` 
* `tests` - Test classes: `PostsTest`and `UsersTest` 
* `utils` - `ApiHelper`with reusable helper API REST Assured methods and `ConfigReader` with config.properties reading logic

## What's Covered

* Full CRUD test coverage for both Users and Posts endpoints (GET, POST, PUT, PATCH, DELETE)
