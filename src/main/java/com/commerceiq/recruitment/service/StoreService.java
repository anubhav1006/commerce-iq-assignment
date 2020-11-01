package com.commerceiq.recruitment.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerceiq.recruitment.entities.Authors;
import com.commerceiq.recruitment.entities.Posts;
import com.commerceiq.recruitment.exceptions.ResourceNotFoundException;
import com.commerceiq.recruitment.repository.StoreRepository;


@Service
public class StoreService implements IStoreService {

  @Autowired
  StoreRepository repository;

  @Override
  public List<Posts> getAllPosts(String title, String author, String sortBy, String orderBy, String query) {
    List<Posts> posts = repository.getAllPosts();

    if(query != null && !query.equals("")){
        posts = posts.stream().filter(postIt -> (postIt.getTitle() != null && postIt.getTitle().contains(query)) ||
          (postIt.getAuthor() != null && postIt.getAuthor().contains(query))).
          collect(Collectors.toList());
    }
    if(title != null && !title.equals("")){
      posts = posts.stream().filter(postIt -> (postIt.getTitle() != null && postIt.getTitle().contains(title)))
        .collect(Collectors.toList());
    }
    if(author != null && !author.equals("")){
      posts = posts.stream().filter(postIt -> (postIt.getAuthor() != null && postIt.getAuthor().contains(author)))
        .collect(Collectors.toList());
    }
    if(sortBy != null && !sortBy.equals("")){
      switch (sortBy) {
        case "views" :
          if(orderBy != null && !orderBy.equals("")) {
            if(orderBy.equals("asc")){
              posts.sort(Comparator.comparingInt(Posts::getViews));
            }else {
              posts.sort(Comparator.comparingInt(Posts::getViews).reversed());
            }
          }
          break;
        case "reviews" :
          if(orderBy != null && !orderBy.equals("")) {
            if(orderBy.equals("asc")){
              posts.sort(Comparator.comparingInt(Posts::getReviews));
            }else {
              posts.sort(Comparator.comparingInt(Posts::getReviews).reversed());
            }
          }
          break;

        case "title" :
          if(orderBy != null && !orderBy.equals("")) {
            if(!orderBy.equals("desc")){
              posts.sort(Comparator.comparing(Posts::getTitle));
            }else {
              posts.sort(Comparator.comparing(Posts::getTitle).reversed());
            }
          }

        case "author" :
          if(orderBy != null && !orderBy.equals("")) {
            if(!orderBy.equals("desc")){
              posts.sort(Comparator.comparing(Posts::getAuthor));
            }else {
              posts.sort(Comparator.comparing(Posts::getAuthor).reversed());
            }
          }
          break;
        default:
          if(orderBy != null && !orderBy.equals("")) {
            if(!orderBy.equals("desc")){
              posts.sort(Comparator.comparingLong(Posts::getId));
            }else {
              posts.sort(Comparator.comparingLong(Posts::getId).reversed());
            }
          }
      }
    }

    return posts;
  }

  @Override
  public List<Authors> getAllAuthors(String firstName, String lastName, String sortBy, String orderBy, String query) {
    List<Authors> authors =  repository.getAllAuthors();

    if(query != null && !query.equals("")){
      authors = authors.stream().filter(authorIt -> (authorIt.getFirstName() != null && authorIt.getFirstName()
        .contains(query)) || (authorIt.getLastName() != null && authorIt.getLastName().contains(query)))
        .collect(Collectors.toList());
    }
    if(firstName != null && !firstName.equals("")){
      authors = authors.stream().filter(authorIt -> (authorIt.getFirstName() != null && authorIt.getFirstName()
        .contains(firstName))).collect(Collectors.toList());
    }
    if(lastName != null && !lastName.equals("")){
      authors = authors.stream().filter(authorIt ->(authorIt.getLastName() != null && authorIt.getLastName()
        .contains(lastName))).collect(Collectors.toList());
    }
    if(sortBy != null && !sortBy.equals("")){
      switch (sortBy) {
        case "posts" :
          if(orderBy != null && !orderBy.equals("")) {
            if(orderBy.equals("asc")){
              authors.sort(Comparator.comparingInt(Authors::getPosts));
            }else {
              authors.sort(Comparator.comparingInt(Authors::getPosts).reversed());
            }
          }
          break;
        case "firstName" :
          if(orderBy != null && !orderBy.equals("")) {
            if(!orderBy.equals("desc")){
              authors.sort(Comparator.comparing(Authors::getFirstName));
            }else {
              authors.sort(Comparator.comparing(Authors::getFirstName).reversed());
            }
          }

        case "lastName" :
          if(orderBy != null && !orderBy.equals("")) {
            if(!orderBy.equals("desc")){
              authors.sort(Comparator.comparing(Authors::getLastName));
            }else {
              authors.sort(Comparator.comparing(Authors::getLastName).reversed());
            }
          }
          break;
        default:
          if(orderBy != null && !orderBy.equals("")) {
            if(!orderBy.equals("desc")){
              authors.sort(Comparator.comparingLong(Authors::getId));
            } else {
              authors.sort(Comparator.comparingLong(Authors::getId).reversed());
            }
          } else {
            authors.sort(Comparator.comparingLong(Authors::getId));
          }
      }
    }

    return authors;
  }

  @Override
  public Posts getPost(long id) throws ResourceNotFoundException {
    return repository.getPost(id);
  }

  @Override
  public Authors getAuthor(long id) throws ResourceNotFoundException {
    return repository.getAuthor(id);
  }

  private boolean checkAuthorExist(String firstName, String lastName) {
    return repository.checkAuthorExists(firstName, lastName);
  }

  @Override
  public Posts addPost(Posts post) {
    Posts newPost = repository.addPost(post);
    updateAuthor(post);
    return newPost;
  }

  private void updateAuthor(Posts post) {
    String authorFirstName="";
    String authorLastName="";
    try {
       authorFirstName = post.getAuthor().split(" ")[0];

      authorLastName = post.getAuthor().split(" ")[1];
      if(!checkAuthorExist(authorFirstName, authorLastName)){
        addAuthor(new Authors(authorFirstName, authorLastName));
      }
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  @Override
  public Authors addAuthor(Authors author) {
    return repository.addAuthor(author);
  }

  @Override
  public Posts editPost(Long id, Posts post) throws ResourceNotFoundException {
    updateAuthor(post);
    return repository.editPost(id, post);
  }

  @Override
  public Authors editAuthor(Long id, Authors author) throws ResourceNotFoundException {
    return repository.editAuthor(id, author);
  }

  @Override
  public Posts patchPost(Long id, Map<String, ?> request) throws ResourceNotFoundException {
    return repository.patchPost(id, request);
  }

  @Override
  public Authors patchAuthor(Long id, Map<String, ?> request) throws ResourceNotFoundException {
    return repository.patchAuthor(id, request);
  }

  @Override
  public void deletePost(Long id) throws ResourceNotFoundException {
    if(!repository.deletePost(id))
      throw new ResourceNotFoundException("Post not found!");
  }

  @Override
  public void deleteAuthor(Long id) throws ResourceNotFoundException {
    if(!repository.deleteAuthor(id))
      throw new ResourceNotFoundException("Author not found!");
  }

  @Override
  public List<Posts> searchPosts(String query) {
    return repository.searchPosts(query);
  }

  @Override
  public List<Authors> searchAuthors(String query) {
    return repository.searchAuthors(query);
  }
}
