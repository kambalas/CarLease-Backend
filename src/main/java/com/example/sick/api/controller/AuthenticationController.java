package com.example.sick.api.controller;

import com.example.sick.domain.AuthenticationDAORequest;
import com.example.sick.domain.RegisterDAORequest;
import com.example.sick.domain.AuthenticationDAOResponse;
import com.example.sick.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;


  @PreAuthorize("hasRole('ROLE_Admin')")
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.OK)
  public AuthenticationDAOResponse register(
          @RequestBody RegisterDAORequest request
  ) {
    return authenticationService.register(request);
  }
  
  @PostMapping("/authenticate")
  @ResponseStatus(HttpStatus.OK)
  public AuthenticationDAOResponse authenticate(
          @RequestBody AuthenticationDAORequest request
  ) {
    return authenticationService.authenticate(request);
  }
}
