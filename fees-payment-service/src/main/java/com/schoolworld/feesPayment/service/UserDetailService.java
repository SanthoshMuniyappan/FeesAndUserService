package com.schoolworld.feesPayment.service;

import com.schoolworld.feesPayment.dto.UserRequestDetailDTO;
import com.schoolworld.feesPayment.entity.User;
import com.schoolworld.feesPayment.repository.RoleRepository;
import com.schoolworld.feesPayment.repository.UserRepository;
import com.schoolworld.feesPayment.util.Constants;
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
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_NOT_FOUND + userName));

        List<String> roles = roleRepository.getUserRole(user.getId());
        return new UserRequestDetailDTO(user, roles);
    }
}
