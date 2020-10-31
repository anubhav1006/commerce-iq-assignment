package com.commerceiq.recruitment.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import com.commerceiq.recruitment.entities.Authors;
import com.commerceiq.recruitment.entities.Posts;
import com.commerceiq.recruitment.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StoreRepository {

  private static final String STORE = "store.json";
  final ObjectMapper objectMapper = new ObjectMapper();

  JSONObject readJsonData() {
    JSONObject obj = new JSONObject();
    try {
      obj = objectMapper.readValue(new FileReader(STORE), JSONObject.class);
    } catch (JsonParseException | JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException fe) {
      fe.printStackTrace();
      Path newFilePath = Paths.get(STORE);
      try {
        Files.createFile(newFilePath);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return obj;
  }

  public List<Posts> getAllPosts() {
    JSONObject obj = readJsonData();
    JSONArray postsArray = (JSONArray) obj.get("posts");
    try {
      return Arrays.asList(objectMapper.readValue(postsArray.toJSONString(), Posts[].class));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  public List<Authors> getAllAuthors() {
    JSONObject obj = readJsonData();
    JSONArray postsArray = (JSONArray) obj.get("authors");
    try {
      return Arrays.asList(objectMapper.readValue(postsArray.toJSONString(), Authors[].class));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  public Posts getPost(long id) throws ResourceNotFoundException {
    for(Posts post: getAllPosts()){
      if(post.getId() == id){
        return post;
      }
    }
    throw new ResourceNotFoundException("Post not found");
  }

  public Authors getAuthor(long id) throws ResourceNotFoundException {
    for(Authors author: getAllAuthors()){
      if(author.getId() == id){
        return author;
      }
    }
    throw new ResourceNotFoundException("Author not found");
  }

  public Authors addAuthor(Authors author) {
    List<Authors> authors = getAllAuthors();
    authors.add(author);
    JSONObject obj = readJsonData();
    obj.replace("authors", authors);
    writeJsonData(obj);
    return author;
  }

  public Posts addPost(Posts post) {
    List<Posts> posts = getAllPosts();
    posts.add(post);
    JSONObject obj = readJsonData();
    obj.replace("posts", posts);
    writeJsonData(obj);
    return post;
  }

  public Posts editPost(Long id, Posts post) throws ResourceNotFoundException {
    List<Posts> posts = getAllPosts();
    for(Posts postIt: posts){
      if(postIt.getId() == id) {
        posts.remove(postIt);
        posts.add(post);
        JSONObject obj = readJsonData();
        obj.replace("posts", posts);
        writeJsonData(obj);
        return post;
      }
    }
    throw new ResourceNotFoundException("Post not found");
  }

  public Authors editAuthor(Long id, Authors author) throws ResourceNotFoundException {
    List<Authors> authors = getAllAuthors();
    for(Authors authorIt: authors){
      if(authorIt.getId() == id) {
        authors.remove(authorIt);
        authors.add(author);
        JSONObject obj = readJsonData();
        obj.replace("authors", authors);
        writeJsonData(obj);
        return author;
      }
    }
    throw new ResourceNotFoundException("Author not found");
  }

  public Posts patchPost(Long id, Map<String, String> values) throws ResourceNotFoundException {
    List<Posts> posts = getAllPosts();
    for(Posts postIt: posts){
      if(postIt.getId() == id) {
        Posts newPost = replacePost(postIt, values);
        newPost.setId(id);
        posts.remove(postIt);
        posts.add(newPost);
        JSONObject obj = readJsonData();
        obj.replace("posts", posts);
        writeJsonData(obj);
        return newPost;
      }
    }
    throw new ResourceNotFoundException("Post not found");
  }

  public Authors patchAuthor(Long id, Map<String, String> values) throws ResourceNotFoundException {
    List<Authors> authors = getAllAuthors();
    for(Authors authorIt: authors){
      if(authorIt.getId() == id) {
        Authors newAuthor = replaceAuthor(authorIt, values);
        newAuthor.setId(id);
        authors.remove(authorIt);
        authors.add(newAuthor);
        JSONObject obj = readJsonData();
        obj.replace("authors", authors);
        writeJsonData(obj);
        return newAuthor;
      }
    }
    throw new ResourceNotFoundException("Author not found");
  }

  private Posts replacePost (Posts post, Map<String, String> values) throws NumberFormatException{
    for(String key: values.keySet()){
      switch(key) {
        case "title" : post.setTitle(values.get(key));
        break;
        case "author" : post.setAuthor(values.get(key));
        break;
        case "views" : post.setViews(Integer.parseInt(values.get(key)));
        break;
        case "reviews" : post.setReviews(Integer.parseInt(values.get(key)));
        break;
        default:
            post.getAdditionalInformation().put(key, values.get(key));
      }
    }
    return post;
  }

  private Authors replaceAuthor (Authors author, Map<String, String> values) throws NumberFormatException{
    for(String key: values.keySet()){
      switch(key) {
        case "firstName" : author.setFirstName(values.get(key));
          break;
        case "lastName" : author.setLastName(values.get(key));
          break;
        case "posts" : author.setPosts(Integer.parseInt(values.get(key)));
          break;
        default:
          author.getAdditionalInformation().put(key, values.get(key));
      }
    }
    return author;
  }



  public boolean deletePost(Long id) {
    List<Posts> posts = getAllPosts();
    for(Posts it : posts) {
      if(it.getId() == id){
        posts.remove(it);
        JSONObject obj = readJsonData();
        obj.replace("posts", posts);
        writeJsonData(obj);
        return true;
      }
    }
    return false;
  }

  public boolean deleteAuthor(Long id) {
    List<Authors> authors = getAllAuthors();
    for(Authors it : authors) {
      if(it.getId() == id){
        authors.remove(it);
        JSONObject obj = readJsonData();
        obj.replace("authors", authors);
        writeJsonData(obj);
        return true;
      }
    }
    return false;
  }


  void writeJsonData(JSONObject object) {
    FileWriter file = null;
    try {
      file = new FileWriter(STORE);
      file.write(object.toJSONString());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {

      try {
        if (file != null) {
          file.flush();
          file.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  public boolean checkAuthorExists(String firstName, String lastName) {
    for(Authors author: getAllAuthors()){
      if(author.getFirstName().equals(firstName) && author.getLastName().equals(lastName)){
        return true;
      }
    }
    return false;
  }

  public List<Posts> searchPosts(String query) {
    List<Posts> posts = new ArrayList<>();
    for(Posts post: getAllPosts()){
      if(post.getTitle().contains(query) || post.getAuthor().contains(query)){
        posts.add(post);
      }
    }
    return posts;

  }

  public List<Authors> searchAuthors(String query) {
    List<Authors> authors = new ArrayList<>();
    for(Authors author: getAllAuthors()){
      if(author.getFirstName().contains(query) || author.getLastName().contains(query)){
        authors.add(author);
      }
    }
    return authors;
  }
}
