package com.example.demo.controller;

import com.example.demo.controller.dto.ShortenUrlRequest;
import com.example.demo.controller.dto.ShortenUrlResponse;
import com.example.demo.entities.UrlEntity;
import com.example.demo.repository.UrlRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
public class UrlController {

  private final UrlRepository urlRepository;

  public UrlController (UrlRepository urlRepository) {
    this.urlRepository = urlRepository;
  }

  @Operation(description = "Recebe uma URL pelo body e retorna ela encurtada")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Retorna a URL encurtada") ,
    @ApiResponse(responseCode = "400", description = "Formato de URL inválidad") ,
  })
  @PostMapping(value = "/shorten-url")
  public ResponseEntity<ShortenUrlResponse> shortenUrl (@RequestBody ShortenUrlRequest request , HttpServletRequest servletRequest) {

    String id;
    do {
      id = RandomStringUtils.randomAlphanumeric(5 , 10);
    } while (urlRepository.existsById(id));

    urlRepository.save(new UrlEntity(id , request.url() , LocalDateTime.now().plusMinutes(1)));

    var redirectUrl = servletRequest.getRequestURL().toString().replace("shorten-url" , id);

    return ResponseEntity.ok(new ShortenUrlResponse(redirectUrl));
  }

  @Operation(description = "Busca no banco de dados a url original usando o ID que foi gerado")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Busca pela URL original no banco de dados") ,
    @ApiResponse(responseCode = "400", description = "URL não encontrada") ,
  })
  @GetMapping("{id}")
  public ResponseEntity<Void> redirect (@PathVariable("id") String id) {

    var url = urlRepository.findById(id);

    if (url.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(url.get().getFullUrl()));

    return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
  }
}
