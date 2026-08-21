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
   private static Faker faker;
   private Post getRandomPost(List<Post>posts){
      return posts.get(new Random().nextInt(posts.size()));
   }
   private List<Post> getAllPosts(){
          return given()
                       .when().get( ConfigReader.getPostParameter())
                       .then().log().all()
                       .contentType(ContentType.JSON)
                       .statusCode(200)
                       .extract().response().jsonPath().getList(".", Post.class);
   }
    private Post getPostById(int postIdParameter){
       return given()
                        .when().get(ConfigReader.getPostParameter() + "/" + postIdParameter)
                        .then().log().all()
                        .contentType(ContentType.JSON)
                        .statusCode(200)
                        .extract().response().as(Post.class);
    }
    private Post createPost(int id){
       Post newPost = new Post();
       newPost.setId(faker.number().numberBetween(1, 10000));
       newPost.setTitle(faker.lorem().sentence());
       newPost.setBody(faker.lorem().paragraph());
       return newPost;
    }
   @BeforeMethod
    public void testSetUp(){
       RestAssured.baseURI = ConfigReader.getBaseUrl();
       faker = new Faker();
   }
   @Test
    public void canGetAllPosts(){
       List<Post> posts = getAllPosts();
       Assert.assertTrue(posts.size()>0);
       posts.forEach((post)->{
           Assert.assertTrue(post.getId()>0);
           Assert.assertTrue(post.getUser_id()>0);
           Assert.assertNotNull(post.getBody());
       });
   }
   @Test
    public void canGetPostById(){
       List<Post> posts = getAllPosts();
       Post randomPost = getRandomPost(posts);
       int postIdParameter = randomPost.getId();
       Post fetchedPost = getPostById(postIdParameter);

       Assert.assertEquals(fetchedPost.getId(), postIdParameter);
       Assert.assertEquals(fetchedPost.getUser_id(), randomPost.getUser_id());
       Assert.assertEquals(fetchedPost.getTitle(), (randomPost.getTitle()));
       Assert.assertTrue(fetchedPost.getBody().equals(randomPost.getBody()));
   }
   @Test
    public void canCreatePost(){

   }
}
