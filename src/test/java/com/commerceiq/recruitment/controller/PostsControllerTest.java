package com.commerceiq.recruitment.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.web.client.HttpClientErrorException;

import com.commerceiq.recruitment.RecruitmentApplication;
import com.commerceiq.recruitment.entities.Posts;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecruitmentApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  private String getRootUrl() {
    return "http://localhost:" + port + "/api/v1";
  }

  @Test
  public void contextLoads() {

  }

  @Test
  public void testGetAllPosts() {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/posts",
      HttpMethod.GET, entity, String.class);
    assertNotNull(response.getBody());
  }

  @Test
  public void testGetPostById() {
    int id = 0;
    Posts post = restTemplate.getForObject(getRootUrl() + "/posts/" + id, Posts.class);
    System.out.println(post.getTitle());
    assertNotNull(post);
  }

  @Test
  public void testCreatePost() {
    Posts post = new Posts();
    post.setReviews(20);
    post.setTitle("Intro to Spring boot");
    post.setViews(10);
    post.setAuthor("Shreya Awasthi");

    Posts post2 = new Posts();
    post2.setReviews(24);
    post2.setViews(12);
    post2.setTitle("Advanced Spring boot");
    post2.setAuthor("Vikram Ahuja");

    ResponseEntity<Posts> postResponse = restTemplate.postForEntity(getRootUrl() + "/posts", post, Posts.class);
    ResponseEntity<Posts> postResponse2 = restTemplate.postForEntity(getRootUrl() + "/posts", post2, Posts.class);

    assertNotNull(postResponse);
    assertNotNull(postResponse.getBody());
    assertNotNull(postResponse2);
    assertNotNull(postResponse2.getBody());
  }


  @Test
  public void testSortAndOrder() {
    String sortBy = "views";
    String orderBy = "desc";
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/posts?_sort=" + sortBy +
        "&_order=" + orderBy,
      HttpMethod.GET, entity, String.class);
    assertNotNull(response.getBody());
  }

  @Test
  public void testFilters() {
    String author = "Vikram Ahuja";
    String title = "Advanced Spring boot";
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/posts?author=" + author +
        "&title=" + title, HttpMethod.GET, entity, String.class);
    assertNotNull(response.getBody());
  }

  @Test
  public void testUpdatePost() {
    int id = 2;
    Posts post = restTemplate.getForObject(getRootUrl() + "/posts/" + id, Posts.class);
    post.setTitle("Intro to Java");
    post.setAuthor("Vaishakh Arora");
    post.setViews(4);
    post.setReviews(8);
    restTemplate.put(getRootUrl() + "/posts/" + id, post);
    Posts updatedPost = restTemplate.getForObject(getRootUrl() + "/posts/" + id, Posts.class);
    assertNotNull(updatedPost);
  }

  @Test
  public void testPatchPost() {
    int id = 2;
    Posts post = restTemplate.getForObject(getRootUrl() + "/posts/" + id, Posts.class);
    assertNotNull(post);
    Map<String, String> map = new HashMap<>();
    map.put("title", "Advanced Java");
    map.put("author", "Rupesh Yadav");
    Posts patchedPost = restTemplate.patchForObject(getRootUrl() + "/posts/" + id, map, Posts.class);
    assertNotNull(patchedPost);
  }

  @Test
  public void testDeletePost() {
    int id = 0;
    Posts post = restTemplate.getForObject(getRootUrl() + "/posts/" + id, Posts.class);
    assertNotNull(post);
    restTemplate.delete(getRootUrl() + "/posts/" + id);
    try {
      post = restTemplate.getForObject(getRootUrl() + "/posts/" + id, Posts.class);
    } catch (final HttpClientErrorException e) {
      assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
    }
  }


  @Test
  public void testSearchedPost() {
    String query = "Shreya";
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/posts/search?_q=" + query,
      HttpMethod.GET, entity, String.class);
    assertNotNull(response.getBody());
  }

}
