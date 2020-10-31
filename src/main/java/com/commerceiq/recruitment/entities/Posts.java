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
public class Posts implements Serializable {

  private Long id;
  private String title;
  private String author;
  private int views;
  private int reviews;
  @JsonAnySetter
  private Map<String, String> additionalInformation = new HashMap<>();
}
