package com.example.sick.repository;

import com.example.sick.domain.AuthenticationDAORequest;

public interface UserRepositoryInterface {
  AuthenticationDAORequest findByUsername(String username);
}
