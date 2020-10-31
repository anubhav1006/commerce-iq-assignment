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

  import com.commerceiq.recruitment.entities.Authors;
  import com.commerceiq.recruitment.exceptions.ResourceNotFoundException;
  import com.commerceiq.recruitment.response.Response;
  import com.commerceiq.recruitment.service.StoreService;

@RestController
@RequestMapping("/api/v1")
public class AuthorsController {

  @Autowired
  StoreService service;

  @GetMapping("/authors")
  public List<Authors> getAllAuthors(@RequestParam(name = "firstName", required = false) String firstName,
                                     @RequestParam(name = "lastName", required = false) String lastName,
                                     @RequestParam(name = "_sort", required = false) String sortBy,
                                     @RequestParam(name = "_order", required = false) String orderBy) {
    return service.getAllAuthors(firstName, lastName, sortBy, orderBy);
  }

  @GetMapping("/authors/{id}")
  public ResponseEntity<Authors> getAuthorById(@PathVariable(value = "id") Long id)
    throws ResourceNotFoundException {
    Authors author = service.getAuthor(id);
    return ResponseEntity.ok().body(author);
  }

  @PostMapping("/authors")
  public Response createAuthor(@RequestBody Authors author) {
    String message = author.getId() != null ? "id was not used while creating entry, " +
      " an auto increment id was used instead!" : "";
    return new Response(200, message,service.addAuthor(author));
  }

  @PutMapping("/authors/{id}")
  public ResponseEntity<Authors> updateAuthors(@PathVariable(value = "id") Long id,
                                           @RequestBody Authors author) throws ResourceNotFoundException {
    service.editAuthor(id, author);
    return ResponseEntity.ok(author);
  }

  @DeleteMapping("/authors/{id}")
  public Map<String, Boolean> deleteAuthors(@PathVariable(value = "id") Long id)
    throws ResourceNotFoundException {
    service.deleteAuthor(id);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }

  @PatchMapping("/authors/{id}")
  public ResponseEntity<Authors> patchAuthors(@PathVariable(value = "id") Long id,
                                          @RequestBody Map<String, String> body) throws ResourceNotFoundException {
    return ResponseEntity.ok(service.patchAuthor(id, body));
  }

}
