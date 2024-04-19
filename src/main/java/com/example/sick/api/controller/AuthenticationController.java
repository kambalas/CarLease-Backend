package com.example.sick.api.controller;

import com.example.sick.domain.AuthenticationDAORequest;
import com.example.sick.domain.RegisterDAORequest;
import com.example.sick.domain.AuthenticationDAOResponse;
import com.example.sick.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationDAOResponse> register(
          @RequestBody RegisterDAORequest request
  ) {
    return new ResponseEntity<>(service.register(request), HttpStatus.OK);
  }
  @CrossOrigin(origins = "https://ci-cd-angular.onrender.com")
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationDAOResponse> authenticate(
          @RequestBody AuthenticationDAORequest request
  ) {
    return new ResponseEntity<>(service.authenticate(request), HttpStatus.OK);
  }
}
