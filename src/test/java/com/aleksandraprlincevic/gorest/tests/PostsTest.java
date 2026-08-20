package com.aleksandraprlincevic.gorest.tests;

import com.aleksandraprlincevic.gorest.pojo.Post;
import com.aleksandraprlincevic.gorest.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class PostsTest {
   private int postIdParameter;
   private static Faker faker;

   @BeforeMethod
    public void testSetUp(){
       RestAssured.baseURI = ConfigReader.getBaseUrl();
       faker = new Faker();
   }
   @Test
    public void listPosts(){
       List<Post> posts =
               given().log().all()
                       .when().get( ConfigReader.getPostParameter())
                       .then().log().all()
                       .statusCode(200)
                       .contentType(ContentType.JSON)
               .extract().response().jsonPath().getList(".", Post.class);
       Assert.assertTrue(posts.size()>0);
       posts.forEach((post)->{
           Assert.assertTrue(post.getId()>0);
           Assert.assertTrue(post.getUser_id()>0);
           Assert.assertNotNull(post.getBody());
       });

   }
}
