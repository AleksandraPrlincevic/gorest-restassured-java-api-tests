package com.aleksandraprlincevic.gorest.tests;

import com.aleksandraprlincevic.gorest.pojo.User;
import com.aleksandraprlincevic.gorest.utils.ApiHelper;
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

    @BeforeClass
    public void setUp() {
    RestAssured.baseURI = ConfigReader.getBaseUrl();
}
    @Test
    public void canGetAllUsers(){
        List<User>users = ApiHelper.getAllUsers();
        User user = ApiHelper.getRandomUser(users);

     Assert.assertFalse(users.isEmpty());
     Assert.assertNotNull(user.getEmail());
     Assert.assertFalse(user.getName().isEmpty());
     Assert.assertTrue(user.getId()>0);
    }

    @Test
    public void canGetUserById(){
        List<User>users = ApiHelper.getAllUsers();
        User user = ApiHelper.getRandomUser(users);
        int userId = user.getId();
        User fetchedUser = ApiHelper.getFetchedUser(userId);

        Assert.assertEquals(fetchedUser.getId(), userId);
        Assert.assertEquals(fetchedUser.getName(), user.getName());
        Assert.assertEquals(fetchedUser.getEmail(), user.getEmail());
        Assert.assertEquals(fetchedUser.getEmail(), user.getEmail());
        Assert.assertEquals(fetchedUser.getStatus(), user.getStatus());
    }

    @Test
    public void canCreateUser(){
    User user = ApiHelper.createNewUser();
    User createdUser = ApiHelper.getCreatedUser(user);

    Assert.assertTrue(createdUser.getId()>0);
    Assert.assertEquals(createdUser.getName(), user.getName());
    Assert.assertEquals(createdUser.getEmail(), user.getEmail());
    Assert.assertTrue(createdUser.getGender().equalsIgnoreCase(user.getGender()));
    Assert.assertEquals(createdUser.getStatus(), user.getStatus());
    }

    @Test
    public void canReplaceUser(){
        User substituteUser = ApiHelper.createNewUser();
        List<User>users = ApiHelper.getAllUsers();
        User user = ApiHelper.getRandomUser(users);
        int userId = user.getId();
        User replacementUser = ApiHelper.getReplacementUser(substituteUser, userId);

        Assert.assertEquals(replacementUser.getId(), user.getId());
        Assert.assertEquals(replacementUser.getEmail(), substituteUser.getEmail());
        Assert.assertEquals(replacementUser.getStatus(), substituteUser.getStatus());
        Assert.assertTrue(replacementUser.getGender().equalsIgnoreCase(substituteUser.getGender()));
        Assert.assertEquals(replacementUser.getName(), substituteUser.getName());
    }
    @Test
    public void canUpdateUser() {
        List<User>users = ApiHelper.getAllUsers();
        User randomUser = ApiHelper.getRandomUser(users);
        int userId = randomUser.getId();

        String newName = ApiHelper.faker.name().fullName();
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", newName);
        updates.put("gender", "male");

        User updatedUser = ApiHelper.updatedUser(updates, userId);
        Assert.assertEquals(updatedUser.getName(), newName);
        Assert.assertEquals(updatedUser.getGender(), "male");
        Assert.assertEquals(updatedUser.getEmail(), randomUser.getEmail());
    }
    @Test
    public void canDeleteUser(){
         List<User> users = ApiHelper.getAllUsers();
         User randomUser = ApiHelper.getRandomUser(users);
         int userId = randomUser.getId();

        ApiHelper.deleteUser(userId);
        List<User> usersAfterDelete = ApiHelper.getAllUsers();

        boolean userStillExist = usersAfterDelete.stream().anyMatch(user->user.getId() == userId);
        Assert.assertFalse(userStillExist);
    }
}

