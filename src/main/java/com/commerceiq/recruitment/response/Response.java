package com.commerceiq.recruitment.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response implements Serializable {
  private int status;
  private String message;
  private Object content;
}
