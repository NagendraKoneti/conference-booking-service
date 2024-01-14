package com.conference.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conference.dto.User;
import com.conference.entity.Customer;
import com.conference.repo.CustomerRepository;
import com.conference.service.UserService;

@Service	
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public User getUserById(Long id) {
		logger.info("Fetching user by id : {}",id);
		Optional<Customer> customerResult= customerRepository.findById(id);
		
		return customerResult.isPresent()? new User(customerResult.get().getId(), customerResult.get().getUsername()): new User();
		
	}
	
	@Override
	public User createUser(User user) {
		Customer customer = new Customer();
		customer.setEmail("Email 1");
		customer.setUsername(user.getUsername());
		customerRepository.save(customer);
		return user;
	}
}
