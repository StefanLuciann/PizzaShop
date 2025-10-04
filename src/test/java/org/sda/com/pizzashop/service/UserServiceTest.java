package org.sda.com.pizzashop.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sda.com.pizzashop.model.User;
import org.sda.com.pizzashop.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_PasswordIsEncrypted() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);


        User savedUser = userService.registerUser(user);


        assertEquals("encryptedPassword", savedUser.getPassword());
        verify(userRepository, times(1)).save(savedUser);
    }
}
