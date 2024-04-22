package com.example.sick.service;

import com.example.sick.domain.AuthenticationDAORequest;
import com.example.sick.domain.RegisterDAORequest;
import com.example.sick.domain.AuthenticationDAOResponse;
import com.example.sick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationDAOResponse register(RegisterDAORequest request) {
    var user = new AuthenticationDAORequest(

            request.username(),
            passwordEncoder.encode(request.password()),
            true,
            true,
            true,
            true
    );

    repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    return new AuthenticationDAOResponse(jwtToken);
  }

  public AuthenticationDAOResponse authenticate(AuthenticationDAORequest request) {
    try {
      if(check(request.getUsername(), request.getPassword())){

      };
      Authentication auth = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getUsername(),
                      request.getPassword()
              )
      );
      SecurityContextHolder.getContext().setAuthentication(auth);
      var user = repository.findByUsername(request.getUsername());
      var jwtToken = jwtService.generateToken(user);
      return new AuthenticationDAOResponse(jwtToken);
    } catch (Exception e) {
      throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
    }
  }

  private boolean check(String username, String password) {
    if (username == null || password == null) {
      return false;
    }
    if (username == "" || password == "") {
      return false;
    }
    else {
      return true;
    }

  }
}