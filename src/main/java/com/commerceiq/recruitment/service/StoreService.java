package com.commerceiq.recruitment.service;

import java.util.Collections;
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
  public List<Posts> getAllPosts(String title, String author, String sortBy, String orderBy) {
    List<Posts> posts = repository.getAllPosts();
    if(title != null && !title.equals("")){
      posts = posts.stream().filter(postIt -> postIt.getTitle().contains(title)).collect(Collectors.toList());
    }
    if(author != null && !author.equals("")){
      posts = posts.stream().filter(postIt -> postIt.getAuthor().contains(author)).collect(Collectors.toList());
    }
    if(sortBy != null && !sortBy.equals("")){
      switch (sortBy) {
        case "views" :
          if(orderBy != null && !orderBy.equals("")) {
            if(orderBy.equals("asc")){
              Collections.sort(posts, Comparator.comparingInt(Posts::getViews));
            }else {
              Collections.sort(posts, Comparator.comparingInt(Posts::getViews).reversed());
            }
          }
          break;
        case "reviews" :
          if(orderBy != null && !orderBy.equals("")) {
            if(orderBy.equals("asc")){
              Collections.sort(posts, Comparator.comparingInt(Posts::getReviews));
            }else {
              Collections.sort(posts, Comparator.comparingInt(Posts::getReviews).reversed());
            }
          }
          break;

        case "title" :
          if(orderBy != null && !orderBy.equals("")) {
            if(!orderBy.equals("desc")){
              Collections.sort(posts, Comparator.comparing(Posts::getTitle));
            }else {
              Collections.sort(posts, Comparator.comparing(Posts::getTitle).reversed());
            }
          }

        case "author" :
          if(orderBy != null && !orderBy.equals("")) {
            if(!orderBy.equals("desc")){
              Collections.sort(posts, Comparator.comparing(Posts::getAuthor));
            }else {
              Collections.sort(posts, Comparator.comparing(Posts::getAuthor).reversed());
            }
          }
          break;
        default:
          if(orderBy != null && !orderBy.equals("")) {
            if(!orderBy.equals("desc")){
              Collections.sort(posts, Comparator.comparingLong(Posts::getId));
            }else {
              Collections.sort(posts, Comparator.comparingLong(Posts::getId).reversed());
            }
          }
      }
    }

    return posts;
  }

  @Override
  public List<Authors> getAllAuthors(String firstName, String lastName, String sortBy, String orderBy) {
    List<Authors> authors =  repository.getAllAuthors();

    if(firstName != null && !firstName.equals("")){
      authors = authors.stream().filter(authorIt -> authorIt.getFirstName().contains(firstName)).
        collect(Collectors.toList());
    }
    if(lastName != null && !lastName.equals("")){
      authors = authors.stream().filter(authorIt -> authorIt.getLastName().contains(lastName)).
        collect(Collectors.toList());
    }
    if(sortBy != null && !sortBy.equals("")){
      switch (sortBy) {
        case "posts" :
          if(orderBy != null && !orderBy.equals("")) {
            if(orderBy.equals("asc")){
              Collections.sort(authors, Comparator.comparingInt(Authors::getPosts));
            }else {
              Collections.sort(authors, Comparator.comparingInt(Authors::getPosts).reversed());
            }
          }
          break;
        case "firstName" :
          if(orderBy != null && !orderBy.equals("")) {
            if(!orderBy.equals("desc")){
              Collections.sort(authors, Comparator.comparing(Authors::getFirstName));
            }else {
              Collections.sort(authors, Comparator.comparing(Authors::getFirstName).reversed());
            }
          }

        case "lastName" :
          if(orderBy != null && !orderBy.equals("")) {
            if(!orderBy.equals("desc")){
              Collections.sort(authors, Comparator.comparing(Authors::getLastName));
            }else {
              Collections.sort(authors, Comparator.comparing(Authors::getLastName).reversed());
            }
          }
          break;
        default:
          if(orderBy != null && !orderBy.equals("")) {
            if(!orderBy.equals("desc")){
              Collections.sort(authors, Comparator.comparingLong(Authors::getId));
            }else {
              Collections.sort(authors, Comparator.comparingLong(Authors::getId).reversed());
            }
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
    String authorFirstName = post.getAuthor().split(" ")[0];
//    Check exception here
    String authorLastName = post.getAuthor().split(" ")[1];
    if(!checkAuthorExist(authorFirstName, authorLastName)){
      addAuthor(new Authors(authorFirstName, authorLastName));
    }
    return newPost;
  }

  @Override
  public Authors addAuthor(Authors author) {
    return repository.addAuthor(author);
  }

  @Override
  public Posts editPost(Long id, Posts post) throws ResourceNotFoundException {
    return repository.editPost(id, post);
  }

  @Override
  public Authors editAuthor(Long id, Authors author) throws ResourceNotFoundException {
    return repository.editAuthor(id, author);
  }

  @Override
  public Posts patchPost(Long id, Map<String, String> request) throws ResourceNotFoundException {
    return repository.patchPost(id, request);
  }

  @Override
  public Authors patchAuthor(Long id, Map<String, String> request) throws ResourceNotFoundException {
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
