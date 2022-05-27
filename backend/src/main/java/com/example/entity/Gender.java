package com.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Gender {
  @JsonProperty(value = "M") MALE,
  @JsonProperty(value = "F") FEMALE;
}
