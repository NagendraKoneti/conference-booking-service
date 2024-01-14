package com.conference.controler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.conference.controler.UserController;
import com.conference.dto.User;
import com.conference.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	@InjectMocks
	private UserController userController;

	@Mock
	private UserService userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetUserById() {
		Long userId = 1L;
		User mockUser = new User(userId, "MockUser");
		when(userService.getUserById(anyLong())).thenReturn(mockUser);
		ResponseEntity<User> responseEntity = userController.getUserById(userId);
		verify(userService, times(1)).getUserById(userId);
	}

	@Test
	void testCreateUser() {
		User newUser = new User(null, "NewUser");
		User createdUser = new User(1L, "NewUser");
		when(userService.createUser(any(User.class))).thenReturn(createdUser);
		ResponseEntity<User> responseEntity = userController.createUser(newUser);
		verify(userService, times(1)).createUser(newUser);
	}
}
