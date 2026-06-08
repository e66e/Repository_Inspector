package io.github.e66e.git_repo_inspector;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;
import tools.jackson.databind.JsonNode;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class, RestClientResponseException.class})
    public ResponseEntity<ErrorResponse> handleException(HttpClientErrorException ex){
        String msg = Objects.requireNonNull(ex.getResponseBodyAs(JsonNode.class)).get("message").asString();
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatusCode().value(), msg);
        return ResponseEntity.status(errorResponse.status()).body(errorResponse);
    }
}
