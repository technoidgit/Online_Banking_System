package com.cetpa.services;

import com.cetpa.entities.User;

public interface UserService 
{
	int saveUser(User user);
	User getUser(String userid);
	boolean validatePassword(String userid, String currentPassword);
	void updatePassword(String userid, String newPassword);
}
