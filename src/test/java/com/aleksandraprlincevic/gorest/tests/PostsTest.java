package com.aleksandraprlincevic.gorest.tests;

import com.aleksandraprlincevic.gorest.utils.ConfigReader;
import io.restassured.RestAssured;
import net.datafaker.Faker;
import org.testng.annotations.BeforeMethod;

public class PostsTest {
   private int postIdParameter;
   private static Faker faker;

   @BeforeMethod
    public void testSetUp(){
       RestAssured.baseURI = ConfigReader.getBaseUrl();
       faker = new Faker();
   }
}
