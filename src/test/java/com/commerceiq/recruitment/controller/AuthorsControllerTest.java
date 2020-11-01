package com.commerceiq.recruitment.controller;

  import static org.junit.Assert.assertEquals;
  import static org.junit.Assert.assertNotNull;

  import java.util.HashMap;
  import java.util.Map;

  import org.apache.http.impl.client.CloseableHttpClient;
  import org.apache.http.impl.client.HttpClients;
  import org.junit.Before;
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
  import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
  import org.springframework.test.context.junit4.SpringRunner;

  import org.springframework.web.client.HttpClientErrorException;

  import com.commerceiq.recruitment.RecruitmentApplication;
  import com.commerceiq.recruitment.entities.Authors;

  import sun.net.www.http.HttpClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecruitmentApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorsControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @LocalServerPort
  private int port;

  @Before
  public void setup() {
    CloseableHttpClient client = HttpClients.createDefault();
    restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory(client));
  }

  private String getRootUrl() {
    return "http://localhost:" + port + "/api/v1";
  }

  @Test
  public void contextLoads() {

  }

  @Test
  public void testGetAllAuthors() {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/authors",
      HttpMethod.GET, entity, String.class);
    assertNotNull(response.getBody());
  }

  @Test
  public void testGetAuthorById() {
    int id = 0;
    Authors author = restTemplate.getForObject(getRootUrl() + "/authors/" + id, Authors.class);
    System.out.println(author.getFirstName());
    assertNotNull(author);
  }

  @Test
  public void testCreateAuthor() {
    Authors author = new Authors();
    author.setPosts(20);
    author.setFirstName("Sohit");
    author.setLastName("Patel");

    Authors author2 = new Authors();
    author2.setPosts(24);
    author2.setFirstName("Ram");
    author2.setLastName("Arora");

    ResponseEntity<Authors> authorResponse = restTemplate.postForEntity(getRootUrl() + "/authors", author,
      Authors.class);
    ResponseEntity<Authors> authorResponse2 = restTemplate.postForEntity(getRootUrl() + "/authors", author2,
      Authors.class);
    assertNotNull(authorResponse);
    assertNotNull(authorResponse.getBody());
    assertNotNull(authorResponse2);
    assertNotNull(authorResponse2.getBody());
  }


  @Test
  public void testSortAndOrder() {
    String sortBy = "posts";
    String orderBy = "desc";
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/authors?_sort=" + sortBy +
        "&_order=" + orderBy,
      HttpMethod.GET, entity, String.class);
    assertNotNull(response.getBody());
  }

  @Test
  public void testFilters() {
    String firstName = "Ram";
    String lastName = "Arora";
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/authors?firstName=" + firstName +
      "&lastName=" + lastName, HttpMethod.GET, entity, String.class);
    assertNotNull(response.getBody());
  }

  @Test
  public void testUpdateAuthor() {
    int id = 2;
    Authors author = restTemplate.getForObject(getRootUrl() + "/authors/" + id, Authors.class);
    author.setPosts(24);
    author.setFirstName("Shyam");
    author.setLastName("Kumar");
    restTemplate.put(getRootUrl() + "/authors/" + id, author);
    Authors updatedAuthor = restTemplate.getForObject(getRootUrl() + "/authors/" + id, Authors.class);
    assertNotNull(updatedAuthor);
  }

  @Test
  public void testPatchAuthor() {
    int id = 2;
    Authors author = restTemplate.getForObject(getRootUrl() + "/authors/" + id, Authors.class);
    assertNotNull(author);
    Map<String, String> map = new HashMap<>();
    map.put("firstName", "Lakhan");
    map.put("lastName", "Chakraborty");
    Authors patchedAuthor = restTemplate.patchForObject(getRootUrl() + "/authors" + id, map, Authors.class);
    assertNotNull(patchedAuthor);
  }

  @Test
  public void testDeleteAuthor() {
    int id = 0;
    Authors author = restTemplate.getForObject(getRootUrl() + "/authors/" + id, Authors.class);
    assertNotNull(author);
    restTemplate.delete(getRootUrl() + "/authors/" + id);
    try {
      author = restTemplate.getForObject(getRootUrl() + "/authors/" + id, Authors.class);
    } catch (final HttpClientErrorException e) {
      assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
    }
  }


  @Test
  public void testSearchedAuthor() {
    String query = "Lakhan";
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/authors/search?_q=" + query,
      HttpMethod.GET, entity, String.class);
    assertNotNull(response.getBody());
  }

}

