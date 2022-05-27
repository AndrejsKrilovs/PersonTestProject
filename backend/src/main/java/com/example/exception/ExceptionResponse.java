package com.example.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder
public class ExceptionResponse {
  @JsonProperty(value = "exception_message")
  String message;

  @JsonProperty(value = "exception_date_time")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.mm.yyyy HH:mm:ss")
  LocalDateTime dateTime;

  @JsonProperty(value = "exception_code")
  Integer errorCode;
}