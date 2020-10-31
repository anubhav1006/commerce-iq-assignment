package com.commerceiq.recruitment.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Authors implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long id;
  private String firstName;
  private String lastName;
  private int posts;
  public Authors(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.posts = 1;
  }
  @JsonAnySetter
  private Map<String, String> additionalInformation = new HashMap<>();
}
