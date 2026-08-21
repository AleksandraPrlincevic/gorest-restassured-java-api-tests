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

    private static Faker faker;

    private User getRandomUser(List<User>users){
        return users.get(new Random().nextInt(users.size()));
    }
    private List<User> getAllUsers(){
        return given()
                .when().get(ConfigReader.getUserParameter())
                .then().log().all()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract().response().jsonPath().getList(".", User.class);
    }
    private User getFetchedUser(int userId){
              return  given()
                        .when().get(ConfigReader.getUserParameter()+"/"+userId)
                        .then().log().all()
                        .statusCode(200)
                        .extract().response().as(User.class);
    }

    private User createNewUser(){
        User user = new User();
        user.setName(faker.name().fullName());
        user.setEmail(faker.internet().emailAddress());
        user.setGender(faker.gender().binaryTypes());
        user.setStatus(faker.options().option("active", "inactive"));
        return user;
    }
    private User getCreatedUser(User user){
          return given().log().all()
                        .header("Authorization", "Bearer " + ConfigReader.getToken())
                        .contentType(ContentType.JSON)
                        .body(user)
                        .when().post(ConfigReader.getUserParameter())
                        .then().log().all()
                        .statusCode(201)
                        .extract().response().as(User.class);
    }
   private User getReplacementUser(User substituteUser, int userId) {
       return given().log().all()
               .header("Authorization", "Bearer " + ConfigReader.getToken())
               .contentType(ContentType.JSON)
               .body(substituteUser)
               .when().put(ConfigReader.getUserParameter() + "/" + userId)
               .then().log().all()
               .statusCode(200)
               .extract().response().as(User.class);
   }
   private User updatedUser(Object updates, int userId) {
      return given()
               .header("Authorization", "Bearer " + ConfigReader.getToken())
               .contentType(ContentType.JSON)
               .body(updates)
               .when().patch(ConfigReader.getUserParameter() + "/" + userId)
               .then().log().all()
               .statusCode(200)
               .extract().response().as(User.class);
   }
    /*public Map<String, Object> makeUpdateObject(Object... keyvalue) {
        if (keyvalue.length % 2 != 0) {
            throw new IllegalArgumentException("Number of arguments has to be even");
        }
        Map<String, Object> update = new HashMap<>();
        for (int i = 0; i < keyvalue.length; i += 2) {
            String key = (String) keyvalue[i];  // kastujemo na String jer svaki je sting object
            Object value = keyvalue[i + 1];   // ostaje object jer moze biti i int i boolean i string i object kao value
            update.put(key, value);
        }
        return update;
    }
          // Gave up on this method because it is not suit for current testing
     */

    public void deleteUser(int userId){
        given().log().all()
                .header("Authorization", "Bearer " + ConfigReader.getToken())
                .contentType(ContentType.JSON)
                .when().delete(ConfigReader.getUserParameter() + "/" + userId)
                .then()
                .statusCode(204);
    }

    @BeforeClass
    public void setUp() {
    RestAssured.baseURI = ConfigReader.getBaseUrl();
    faker = new Faker();
}
    @Test
    public void canGetAllUsers(){
        List<User>users = getAllUsers();
        User user = getRandomUser(users);

     Assert.assertFalse(users.isEmpty());
     Assert.assertNotNull(user.getEmail());
     Assert.assertFalse(user.getName().isEmpty());
     Assert.assertTrue(user.getId()>0);
    }

    @Test
    public void canGetUserById(){
        List<User>users = getAllUsers();
        User user = getRandomUser(users);
        int userId = user.getId();
        User fetchedUser = getFetchedUser(userId);

        Assert.assertEquals(fetchedUser.getId(), userId);
        Assert.assertEquals(fetchedUser.getName(), user.getName());
        Assert.assertEquals(fetchedUser.getEmail(), user.getEmail());
        Assert.assertEquals(fetchedUser.getEmail(), user.getEmail());
        Assert.assertEquals(fetchedUser.getStatus(), user.getStatus());
    }

    @Test
    public void canCreateUser(){
    User user =createNewUser();
    User createdUser = getCreatedUser(user);

    Assert.assertTrue(createdUser.getId()>0);
    Assert.assertEquals(createdUser.getName(), user.getName());
    Assert.assertEquals(createdUser.getEmail(), user.getEmail());
    Assert.assertTrue(createdUser.getGender().equalsIgnoreCase(user.getGender()));
    Assert.assertEquals(createdUser.getStatus(), user.getStatus());
    }

    @Test
    public void canReplaceUser(){
        User substituteUser = createNewUser();
        List<User>users = getAllUsers();
        User user = getRandomUser(users);
        int userId = user.getId();
        User replacementUser = getReplacementUser(substituteUser, userId);

        Assert.assertEquals(replacementUser.getId(), user.getId());
        Assert.assertEquals(replacementUser.getEmail(), substituteUser.getEmail());
        Assert.assertEquals(replacementUser.getStatus(), substituteUser.getStatus());
        Assert.assertTrue(replacementUser.getGender().equalsIgnoreCase(substituteUser.getGender()));
        Assert.assertEquals(replacementUser.getName(), substituteUser.getName());
    }
    @Test
    public void canUpdateUser() {
        List<User>users = getAllUsers();
        User randomUser = getRandomUser(users);
        int userId = randomUser.getId();

        String newName = faker.name().fullName();
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", newName);
        updates.put("gender", "male");

        User updatedUser = updatedUser(updates, userId);
        Assert.assertEquals(updatedUser.getName(), newName);
        Assert.assertEquals(updatedUser.getGender(), "male");
        Assert.assertEquals(updatedUser.getEmail(), randomUser.getEmail());
    }
    @Test
    public void canDeleteUser(){
         List<User> users = getAllUsers();
         User randomUser = getRandomUser(users);
         int userId = randomUser.getId();

        deleteUser(userId);
        List<User> usersAfterDelete = getAllUsers();

        boolean userStillExist = usersAfterDelete.stream().anyMatch(user->user.getId() == userId);
        Assert.assertFalse(userStillExist);
    }
}

