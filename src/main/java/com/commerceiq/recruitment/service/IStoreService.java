package com.commerceiq.recruitment.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.commerceiq.recruitment.entities.Authors;
import com.commerceiq.recruitment.entities.Posts;
import com.commerceiq.recruitment.exceptions.ResourceNotFoundException;

@Service
public interface IStoreService {

  /*
  * Get list of all posts from Json file
  * Sort and filter option available
  * Query option available
  * */
  List<Posts> getAllPosts(String query, String title, String author, String sortBy, String orderBy);

  /*
   * Get list of all authors from Json file
   * */
  List<Authors> getAllAuthors(String query, String firstName, String lastName, String sortBy, String orderBy);

  /*
   * Get post based on id from Json file
   * */
  Posts getPost(long id) throws ResourceNotFoundException;

  /*
   * Get author based on id from Json file
   * */
  Authors getAuthor(long id) throws ResourceNotFoundException;

  /*
   * Add new post to Json file.
   * Adding a new post will also add new author if doesn't already exist.
   * */
  Posts addPost(Posts post);

  /*
   * Add new author to Json file
   * */
  Authors addAuthor(Authors authors);

  /*
   * Edit a post in Json file
   * */
  Posts editPost(Long id, Posts post) throws ResourceNotFoundException;

  /*
   * Edit a author in Json file
   * */
  Authors editAuthor(Long id, Authors author) throws ResourceNotFoundException;

  /*
   * Patch a post in Json file
   * */
  Posts patchPost(Long id, Map<String, String> request) throws ResourceNotFoundException;

  /*
   * Patch a author in Json file
   * */
  Authors patchAuthor(Long id, Map<String, String> request) throws ResourceNotFoundException;

  /*
   * Delete a post from Json file
   * */
  void deletePost(Long id) throws ResourceNotFoundException;

  /*
   * Delete a author in Json file
   * */
  void deleteAuthor(Long id) throws ResourceNotFoundException;

  /*
   * Search for a query in posts
   * */
  List<Posts> searchPosts(String query);

  /*
   * Search for a query in authors
   * */
  List<Authors> searchAuthors(String query);


}
