package com.aleksandraprlincevic.gorest.tests;

import com.aleksandraprlincevic.gorest.pojo.User;
import com.aleksandraprlincevic.gorest.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

public class UsersTest {

    //private String usersParameter;
    private int userIdParameter;

    @BeforeClass
    public void setUp() {
    RestAssured.baseURI = ConfigReader.getBaseUrl();
    //usersParameter = "users";
}
    @Test
    public void listUser(){
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
        Faker faker = new Faker();
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
}
