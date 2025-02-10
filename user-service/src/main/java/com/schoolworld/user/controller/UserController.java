package com.schoolworld.user.controller;

import com.schoolworld.user.dto.ResponseDTO;
import com.schoolworld.user.dto.UserSignInRequestDTO;
import com.schoolworld.user.dto.UserSignUpRequestDTO;
import com.schoolworld.user.entity.User;
import com.schoolworld.user.service.JwtService;
import com.schoolworld.user.service.UserService;
import com.schoolworld.user.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public UserController(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final UserSignUpRequestDTO userSignUpRequestDTO) {
        return this.userService.create(userSignUpRequestDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable("id") final String id, @RequestBody final UserSignUpRequestDTO userSignUpRequestDTO) {
        return this.userService.update(id, userSignUpRequestDTO);
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieve(@PathVariable("id") final String id) {
        return this.userService.retrieve(id);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieveAll() {
        return this.userService.retrieveAll();
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO remove(@PathVariable final String id) {
        return this.userService.remove(id);
    }

    @PostMapping("/authenticate")
    public ResponseDTO authenticate(@RequestBody final UserSignInRequestDTO userSignInRequestDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSignInRequestDto.getEmail(), userSignInRequestDto.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(userSignInRequestDto.getEmail());
            return new ResponseDTO(Constants.TOKEN, token, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new UsernameNotFoundException(Constants.USER_NOT_FOUND);
        }
    }

    @GetMapping("/users")
    public Page<User> getUsers(@RequestParam(required = true) final int page,
                               @RequestParam(required = true) final int size,
                               @RequestParam(required = true) final String sortBy,
                               @RequestParam(required = true) final String sortDirection) {

        return userService.getUsers(page, size, sortBy, sortDirection);
    }
}
