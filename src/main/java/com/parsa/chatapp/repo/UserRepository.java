package com.parsa.chatapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parsa.chatapp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findUserByUsername(String username);
}
