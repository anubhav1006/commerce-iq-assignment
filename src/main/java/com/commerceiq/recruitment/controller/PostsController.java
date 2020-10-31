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
import com.commerceiq.recruitment.service.StoreService;

@RestController
@RequestMapping("/api/v1")
public class PostsController {

  @Autowired
  StoreService service;

  @GetMapping("/posts")
  public List<Posts> getAllPosts(@RequestParam(name = "title", required = false) String title,
                                 @RequestParam(name = "author", required = false) String author,
                                 @RequestParam(name = "_sort", required = false) String sortBy,
                                 @RequestParam(name = "_order", required = false) String orderBy
                                 ) {
    return service.getAllPosts(title, author, sortBy, orderBy);
  }

  @GetMapping("/posts/{id}")
  public ResponseEntity<Posts> getPostsById(@PathVariable(value = "id") Long id)
    throws ResourceNotFoundException {
    Posts post = service.getPost(id);
    return ResponseEntity.ok().body(post);
  }

  @PostMapping("/posts")
  public Posts createPosts(@RequestBody Posts post) {
    return service.addPost(post);
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

}
