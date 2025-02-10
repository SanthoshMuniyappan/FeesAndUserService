package com.schoolworld.user.service;


import com.schoolworld.user.dto.UserRequestDetailDTO;
import com.schoolworld.user.entity.User;
import com.schoolworld.user.repository.RoleRepository;
import com.schoolworld.user.repository.UserRepository;
import com.schoolworld.user.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UsersDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = this.userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_NOT_FOUND + userName));

        List<String> roles = roleRepository.getUserRole(user.getId());
        return new UserRequestDetailDTO(user, roles);
    }
}
