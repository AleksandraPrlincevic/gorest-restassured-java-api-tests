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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Test
    public void canUpdatePost(){
        User newUser =ApiHelper.createNewUser();
        User createdUser = ApiHelper.getCreatedUser(newUser);
        int userId = createdUser.getId();
        Post post = ApiHelper.createPost(userId);
        Post createdPost =ApiHelper.getCreatedPost(post);
        int createdPostId = createdPost.getId();

        String title = ApiHelper.faker.lorem().sentence();
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", title);

        Post updatedPost = ApiHelper.getUpdatedPost(updates, createdPostId);

        Assert.assertEquals(title,updatedPost.getTitle());
        Assert.assertEquals(createdPost.getId(), updatedPost.getId());
        Assert.assertEquals(createdPost.getUser_id(), updatedPost.getUser_id());
    }
    @Test
    public void canReplacePost(){
        User newUser =ApiHelper.createNewUser();
        User createdUser = ApiHelper.getCreatedUser(newUser);
        int userId = createdUser.getId();

        Post post = ApiHelper.createPost(userId);
        Post createdPost = ApiHelper.getCreatedPost(post);
        int createdPostId = createdPost.getId();

        Post substitutePost = ApiHelper.createPost(userId);
        Post replacementPost = ApiHelper.getSubstitutePost(substitutePost, createdPostId);
        int replacementPostId = replacementPost.getId();


        Assert.assertEquals(createdPostId, replacementPostId);
        Assert.assertEquals(createdPost.getUser_id(), replacementPost.getUser_id());
        Assert.assertEquals(substitutePost.getTitle(), replacementPost.getTitle());
        Assert.assertEquals(substitutePost.getBody(), replacementPost.getBody());
        Assert.assertNotEquals(createdPost.getBody(), replacementPost.getBody());
    }
    @Test
    public void canDeletePost() {
        User newUser = ApiHelper.createNewUser();
        User createdUser = ApiHelper.getCreatedUser(newUser);
        int userId = createdUser.getId();

        Post post = ApiHelper.createPost(userId);
        Post createdPost = ApiHelper.getCreatedPost(post);
        int createdPostId = createdPost.getId();

        ApiHelper.deletePost(createdPostId);

        List<Post> postsAfterDelete = ApiHelper.getAllPosts();
        boolean deletedPostStillExist = postsAfterDelete.stream().anyMatch(p -> p.getId() == createdPostId);
        Assert.assertFalse(deletedPostStillExist);
        Assert.assertNotNull(postsAfterDelete);
    }
 }

