package com.pension.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pension.auth.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
	User findByUsername(String username); 
}