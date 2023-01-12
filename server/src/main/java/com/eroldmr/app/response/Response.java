package com.eroldmr.app.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Mompati 'Patco' Keetile
 * @created 22-11-2022 @ 15:41
 */
@Data
@SuperBuilder
@JsonInclude(NON_NULL)
public class Response {
  protected LocalDateTime timeStamp;
  protected int statusCode;
  protected HttpStatus status;
  protected String message;
  protected String developerMessage;
  protected Map<?, ?> data;
}
