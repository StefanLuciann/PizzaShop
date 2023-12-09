package org.sda.com.pizzashop.service;

import org.sda.com.pizzashop.model.User;
import org.sda.com.pizzashop.model.UserProfile;
import org.sda.com.pizzashop.model.enums.UserRole;
import org.sda.com.pizzashop.repository.UserProfileRepository;
import org.sda.com.pizzashop.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserProfileRepository userProfileRepository;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserProfileRepository userProfileRepository, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createUser(String email, String password, UserRole role, String name) {
        User user = new User(
                name,
                email,
                passwordEncoder.encode(password),
                role
        );

        if(role.equals(UserRole.CLIENT)){
            UserProfile userProfile = new UserProfile();
            userProfileRepository.save(userProfile);
            user.setUserProfile(userProfile);
        }
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Wrong username or password");
        }
        User user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getUserRole().name()))
        );
    }
}
