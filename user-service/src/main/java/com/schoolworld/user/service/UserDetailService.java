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
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserDetailService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_EMAIL_NOT_FOUND + email));

        List<String> roles = roleRepository.getUserRole(user.getId());
        return new UserRequestDetailDTO(user, roles);
    }
}
