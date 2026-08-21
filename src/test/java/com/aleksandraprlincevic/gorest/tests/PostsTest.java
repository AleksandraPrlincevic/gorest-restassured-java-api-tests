package com.aleksandraprlincevic.gorest.tests;

import com.aleksandraprlincevic.gorest.pojo.Post;
import com.aleksandraprlincevic.gorest.pojo.User;
import com.aleksandraprlincevic.gorest.utils.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.datafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.aleksandraprlincevic.gorest.utils.ApiHelper;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class PostsTest {

   @BeforeMethod
    public void testSetUp(){
       RestAssured.baseURI = ConfigReader.getBaseUrl();
   }
   @Test
    public void canGetAllPosts(){
       List<Post> posts = ApiHelper.getAllPosts();
       Assert.assertTrue(posts.size()>0);
       posts.forEach((post)->{
           Assert.assertTrue(post.getId()>0);
           Assert.assertTrue(post.getUser_id()>0);
           Assert.assertNotNull(post.getBody());
       });
   }
   @Test
    public void canGetPostById(){
       List<Post> posts = ApiHelper.getAllPosts();
       Post randomPost = ApiHelper.getRandomPost(posts);
       int postIdParameter = randomPost.getId();
       Post fetchedPost = ApiHelper.getPostById(postIdParameter);

       Assert.assertEquals(fetchedPost.getId(), postIdParameter);
       Assert.assertEquals(fetchedPost.getUser_id(), randomPost.getUser_id());
       Assert.assertEquals(fetchedPost.getTitle(), (randomPost.getTitle()));
       Assert.assertTrue(fetchedPost.getBody().equals(randomPost.getBody()));
   }
   @Test
    public void canCreatePost(){

       User newUser =ApiHelper.createNewUser();
       User createdUser = ApiHelper.getCreatedUser(newUser);
       int userId = createdUser.getId();
       Post post = ApiHelper.createPost(userId);
       Post createdPost =ApiHelper.getCreatedPost(post);

       Assert.assertEquals(post.getUser_id(), createdPost.getUser_id());
       Assert.assertEquals(post.getBody(), createdPost.getBody());
       Assert.assertEquals(post.getTitle(), createdPost.getTitle());
       }
   }

