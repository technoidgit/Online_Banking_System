package com.cetpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cetpa.entities.User;

public interface UserRepository extends JpaRepository<User,String> 
{

	User findByUserid(String userid);
}
