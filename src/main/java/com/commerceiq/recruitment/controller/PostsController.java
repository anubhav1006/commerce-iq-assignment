package com.commerceiq.recruitment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerceiq.recruitment.entities.Posts;
import com.commerceiq.recruitment.exceptions.ResourceNotFoundException;
import com.commerceiq.recruitment.response.Response;
import com.commerceiq.recruitment.service.StoreService;

@RestController
@RequestMapping("/api/v1")
public class PostsController {

  @Autowired
  StoreService service;

  @GetMapping("/posts")
  public List<Posts> getAllPosts(@RequestParam(value = "title", required = false) String title,
                                 @RequestParam(value = "author", required = false) String author,
                                 @RequestParam(value = "_sort", required = false) String sortBy,
                                 @RequestParam(value = "_order", required = false) String orderBy,
                                 @RequestParam(value = "_q", required = false) String query) {
    return service.getAllPosts(title, author, sortBy, orderBy, query);
  }

  @GetMapping("/posts/{id}")
  public ResponseEntity<Posts> getPostsById(@PathVariable(value = "id") Long id)
    throws ResourceNotFoundException {
    Posts post = service.getPost(id);
    return ResponseEntity.ok().body(post);
  }

  @PostMapping("/posts")
  public Response createPosts(@RequestBody Posts post) {
    String message = post.getId() != null ? "id was not used while creating entry, " +
      "an auto increment id was used instead!" : "";
    return new Response(200, message, service.addPost(post));
  }

  @PutMapping("/posts/{id}")
  public ResponseEntity<Posts> updatePosts(@PathVariable(value = "id") Long id,
                                                  @RequestBody Posts post) throws ResourceNotFoundException {
    service.editPost(id, post);
    return ResponseEntity.ok(post);
  }

  @DeleteMapping("/posts/{id}")
  public Map<String, Boolean> deletePosts(@PathVariable(value = "id") Long id)
    throws ResourceNotFoundException {
  service.deletePost(id);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }

  @PatchMapping("/posts/{id}")
  public ResponseEntity<Posts> patchPosts(@PathVariable(value = "id") Long id,
                                           @RequestBody Map<String, String> body) throws ResourceNotFoundException {
    return ResponseEntity.ok(service.patchPost(id, body));
  }

  @GetMapping("/posts/search")
  public List<Posts> getSearchedPosts( @RequestParam(name = "_q", required = false) String query) {
    return service.searchPosts(query);
  }


}
