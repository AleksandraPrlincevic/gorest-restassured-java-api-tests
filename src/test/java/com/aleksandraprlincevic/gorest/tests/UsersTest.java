package com.aleksandraprlincevic.gorest.tests;

import com.aleksandraprlincevic.gorest.pojo.User;
import com.aleksandraprlincevic.gorest.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

public class UsersTest {

    //private String usersParameter;
    private int userIdParameter;
    private static Faker faker;

    @BeforeClass
    public void setUp() {
    RestAssured.baseURI = ConfigReader.getBaseUrl();
    faker = new Faker();
}
    @Test
    public void listUsers(){
     List<User> users =
             given().log().all()
             .when().get(ConfigReader.getUserParameter())
             .then().log().all()
             .assertThat().statusCode(200)
     .extract().response().jsonPath().getList(".",User.class);

        User randomUser = users.get(new Random().nextInt(users.size()));
     Assert.assertFalse(users.isEmpty());
     Assert.assertNotNull(randomUser.getEmail());
     Assert.assertFalse(randomUser.getName().isEmpty());
     Assert.assertTrue(randomUser.getId()>0);
    }
    @Test
    public void getUser(){
        List<User> users =
                  given()
                  .when().get(ConfigReader.getUserParameter())
                  .then()
        .extract().response().jsonPath().getList(".", User.class);

        User randomUser = users.get(new Random().nextInt(users.size()));
        userIdParameter = randomUser.getId();
        User fetchedUser =
        given()
                .when().get(ConfigReader.getUserParameter()+"/"+userIdParameter)
                .then().log().all()
                .assertThat().statusCode(200)
        .extract().response().as(User.class);
        Assert.assertEquals(fetchedUser.getId(), userIdParameter);
        Assert.assertEquals(fetchedUser.getName(), randomUser.getName());
    }
    @Test
    public void createUser(){
        User newUser = new User();
        newUser.setName(faker.name().fullName());
        newUser.setEmail(faker.internet().emailAddress());
        newUser.setGender(faker.gender().binaryTypes());
        newUser.setStatus(faker.options().option("active", "inactive"));

    User createdUser =
        given().log().all()
                .header("Authorization", "Bearer " + ConfigReader.getToken())
                .contentType(ContentType.JSON)
                .body(newUser)
                .when().post(ConfigReader.getUserParameter())
                .then().log().all()
                .statusCode(201)
    .extract().response().as(User.class);

    Assert.assertTrue(createdUser.getId()>0);
    Assert.assertEquals(createdUser.getName(), newUser.getName());
    Assert.assertEquals(createdUser.getEmail(), newUser.getEmail());
    Assert.assertTrue(createdUser.getGender().equalsIgnoreCase(newUser.getGender()));
    Assert.assertEquals(createdUser.getStatus(), newUser.getStatus());
    }
    @Test
    public void replaceUser(){
        User substituteUser = new User();
        substituteUser.setName(faker.name().fullName());
        substituteUser.setEmail(faker.internet().emailAddress());
        substituteUser.setGender(faker.gender().binaryTypes());
        substituteUser.setStatus(faker.options().option("active", "inactive"));

        List<User> users =
                given()
                        .when().get(ConfigReader.getUserParameter())
                        .then().statusCode(200)
        .extract().response().jsonPath().getList(".", User.class);
        User randomUser = users.get(new Random().nextInt(users.size()));
         userIdParameter = randomUser.getId();
        User replacedUser =
        given().log().all()
                .header("Authorization", "Bearer " + ConfigReader.getToken())
                .contentType(ContentType.JSON)
                .body(substituteUser)
                .when().put(ConfigReader.getUserParameter() + "/" + userIdParameter)
                .then().log().all()
                .statusCode(200)
        .extract().response().as(User.class);

        Assert.assertEquals(replacedUser.getId(), randomUser.getId());
        Assert.assertEquals(replacedUser.getEmail(), substituteUser.getEmail());
        Assert.assertEquals(replacedUser.getStatus(), substituteUser.getStatus());
        Assert.assertTrue(replacedUser.getGender().equalsIgnoreCase(substituteUser.getGender()));
        Assert.assertNotEquals(replacedUser.getName(), randomUser.getName());
    }
    @Test
    public void updateUser() {
        List<User> users =
                given()
                        .when().get(ConfigReader.getUserParameter())
                        .then().statusCode(200)
                        .extract().response().jsonPath().getList(".", User.class);
        User randomUser = users.get(new Random().nextInt(users.size()));
        userIdParameter = randomUser.getId();

        String newName = faker.name().fullName();
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", newName);
        updates.put("gender", "male");
        User updatedUser =
                given()
                        .header("Authorization", "Bearer " + ConfigReader.getToken())
                        .contentType(ContentType.JSON)
                        .body(updates)
                        .when().patch(ConfigReader.getUserParameter() + "/" + userIdParameter)
                        .then().log().all()
                        .statusCode(200)
                        .extract().response().as(User.class);
        Assert.assertEquals(updatedUser.getName(), newName);
        Assert.assertEquals(updatedUser.getGender(), "male");
        Assert.assertEquals(updatedUser.getEmail(), randomUser.getEmail());
    }
    @Test
    public void deleteUser(){
         List<User> users =
                 given()
                         .when().get(ConfigReader.getUserParameter())
                         .then()
                         .statusCode(200)
                 .extract().response().jsonPath().getList(".", User.class);
         User randomUser = users.get(new Random().nextInt(users.size()));
         userIdParameter = randomUser.getId();

         given().log().all()
                 .header("Authorization", "Bearer " + ConfigReader.getToken())
                 .contentType(ContentType.JSON)
                 .when().delete(ConfigReader.getUserParameter() + "/" + userIdParameter)
                 .then()
                 .statusCode(204);
        List<User> usersAfterDelete =
                given()
                        .when().get(ConfigReader.getUserParameter())
                        .then()
                        .statusCode(200)
                .extract().response().jsonPath().getList(".", User.class);

        boolean userStillExist =usersAfterDelete.stream().anyMatch(user->user.getId() == userIdParameter);
        Assert.assertFalse(userStillExist);
    }
}

