package com.aleksandraprlincevic.gorest.utils;

import com.aleksandraprlincevic.gorest.pojo.Post;
import com.aleksandraprlincevic.gorest.pojo.User;
import io.restassured.http.ContentType;
import net.datafaker.Faker;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class ApiHelper {

    public static Faker faker = new Faker();

    public static User getRandomUser(List<User> users){
        return users.get(new Random().nextInt(users.size()));
    }
    public static List<User> getAllUsers(){
        return given()
                .when().get(ConfigReader.getUserParameter())
                .then().log().all()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract().response().jsonPath().getList(".", User.class);
    }
    public static User getFetchedUser(int userId){
        return  given()
                .when().get(ConfigReader.getUserParameter()+"/"+userId)
                .then().log().all()
                .statusCode(200)
                .extract().response().as(User.class);
    }

    public static User createNewUser(){
        User user = new User();
        user.setName(faker.name().fullName());
        user.setEmail(faker.internet().emailAddress());
        user.setGender(faker.gender().binaryTypes());
        user.setStatus(faker.options().option("active", "inactive"));
        return user;
    }
    public static User getCreatedUser(User user){
        return given().log().all()
                .header("Authorization", "Bearer " + ConfigReader.getToken())
                .contentType(ContentType.JSON)
                .body(user)
                .when().post(ConfigReader.getUserParameter())
                .then().log().all()
                .statusCode(201)
                .extract().response().as(User.class);
    }
    public static User getReplacementUser(User substituteUser, int userId) {
        return given().log().all()
                .header("Authorization", "Bearer " + ConfigReader.getToken())
                .contentType(ContentType.JSON)
                .body(substituteUser)
                .when().put(ConfigReader.getUserParameter() + "/" + userId)
                .then().log().all()
                .statusCode(200)
                .extract().response().as(User.class);
    }
    public static User updatedUser(Object updates, int userId) {
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

    public static void deleteUser(int userId){
        given().log().all()
                .header("Authorization", "Bearer " + ConfigReader.getToken())
                .contentType(ContentType.JSON)
                .when().delete(ConfigReader.getUserParameter() + "/" + userId)
                .then()
                .statusCode(204);
    }

    public static Post getRandomPost(List<Post>posts){
        return posts.get(new Random().nextInt(posts.size()));
    }

    public static List<Post> getAllPosts(){
        return given()
                .when().get( ConfigReader.getPostParameter())
                .then().log().all()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract().response().jsonPath().getList(".", Post.class);
    }
    public static Post getPostById(int postIdParameter){
        return given()
                .when().get(ConfigReader.getPostParameter() + "/" + postIdParameter)
                .then().log().all()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract().response().as(Post.class);
    }
    public static Post createPost(int id){
        Post newPost = new Post();
        newPost.setId(faker.number().numberBetween(1, 10000));
        newPost.setTitle(faker.lorem().sentence());
        newPost.setBody(faker.lorem().paragraph());
        newPost.setUserId(id);
        return newPost;
    }
    public static Post getCreatedPost(Post post){
              return  given().log().all()
                        .header("Authorization", "Bearer " + ConfigReader.getToken())
                        .contentType(ContentType.JSON)
                        .body(post)
                        .when().post(ConfigReader.getPostParameter())
                        .then()
                        .statusCode(201)
                .extract().response().as(Post.class);
    }
}
