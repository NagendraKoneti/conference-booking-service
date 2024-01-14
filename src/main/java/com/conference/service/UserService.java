package com.conference.service;

import com.conference.dto.User;

public interface UserService {

	User getUserById(Long id);

	User createUser(User any);

}
