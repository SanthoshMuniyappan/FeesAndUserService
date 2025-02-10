package com.userservice.service;


import com.schoolworld.user.dto.ResponseDTO;
import com.schoolworld.user.dto.UserSignUpRequestDTO;
import com.schoolworld.user.entity.School;
import com.schoolworld.user.entity.User;
import com.schoolworld.user.exception.BadRequestServiceException;
import com.schoolworld.user.repository.SchoolRepository;
import com.schoolworld.user.repository.UserRepository;
import com.schoolworld.user.service.UserService;
import com.schoolworld.user.util.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreate() {

        School school = new School();

        UserSignUpRequestDTO userSignUpRequestDTO = new UserSignUpRequestDTO();
        userSignUpRequestDTO.setUserName("Santhosh");
        userSignUpRequestDTO.setPassword("password");
        userSignUpRequestDTO.setSchoolId(Constants.ID);
        userSignUpRequestDTO.setEmail("test@example.com");
        userSignUpRequestDTO.setCreatedBy("admin");
        userSignUpRequestDTO.setUpdatedBy("admin");

        User user = new User();
        user.setId(Constants.ID);
        user.setUserName(userSignUpRequestDTO.getUserName());
        user.setPassword("encodedPassword");
        user.setSchool(school);
        user.setEmail(userSignUpRequestDTO.getEmail());
        user.setCreatedBy(userSignUpRequestDTO.getCreatedBy());
        user.setUpdatedBy(userSignUpRequestDTO.getUpdatedBy());
        user.setActive(false);

        when(this.schoolRepository.findById(Constants.ID)).thenReturn(Optional.of(school));
        when(this.passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(this.userRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
        when(this.userRepository.save(any(User.class))).thenReturn(user);

        ResponseDTO response = this.userService.create(userSignUpRequestDTO);

        assertNotNull(response);
        assertEquals(Constants.CREATED, response.getMessage());
        assertEquals(user, response.getData());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateUserTest() {
        UserSignUpRequestDTO userSignUpRequestDTO = new UserSignUpRequestDTO();
        userSignUpRequestDTO.setUserName("Santhosh");
        userSignUpRequestDTO.setPassword("password");

        User user = new User();
        user.setUserName(userSignUpRequestDTO.getUserName());
        user.setPassword("encodedPassword");

        when(this.userRepository.findById(Constants.ID)).thenReturn(Optional.of(user));
        when(this.passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(this.userRepository.save(user)).thenReturn(user);

        ResponseDTO response = this.userService.update(Constants.ID, userSignUpRequestDTO);

        assertNotNull(response);
        assertEquals(Constants.UPDATED, response.getMessage());
        assertEquals(user, response.getData());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void retrieveUserTest() {
        User user = new User();
        when(this.userRepository.findById(Constants.ID)).thenReturn(Optional.of(user));

        ResponseDTO response = this.userService.retrieve(Constants.ID);

        assertNotNull(response);
        assertEquals(Constants.RETRIEVED, response.getMessage());
        assertEquals(user, response.getData());
    }

    @Test
    public void retrieveAllUserTest() {
        List<User> users = new ArrayList<>();
        when(this.userRepository.retrieveAllUsers()).thenReturn(users);

        ResponseDTO response = this.userService.retrieveAll();

        assertNotNull(response);
        assertEquals(Constants.RETRIEVED, response.getMessage());
        assertEquals(users, response.getData());
    }

    @Test
    public void removeUserTest() {

        User user = new User();
        when(this.userRepository.findById(Constants.ID)).thenReturn(Optional.of(user));
        when(this.userRepository.save(user)).thenReturn(user);

        ResponseDTO response = this.userService.remove(Constants.ID);

        assertNotNull(response);
        assertEquals(Constants.REMOVED, response.getMessage());
        assertEquals(user, response.getData());
    }

    @Test
    public void testPageUsers() {
        int page = 0;
        int size = 10;
        String sortBy = "userName";
        String sortDirection = "asc";

        User user = new User();
        Page<User> mockPage = new PageImpl<>(Collections.singletonList(user));

        when(userRepository.findByIsDeleteFalse(any(Pageable.class))).thenReturn(mockPage);

        Page<User> result = userService.getUsers(page, size, sortBy, sortDirection);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(user, result.getContent().get(0));

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(userRepository, times(1)).findByIsDeleteFalse(pageableCaptor.capture());

        Pageable capturedPageable = pageableCaptor.getValue();
        assertEquals(page, capturedPageable.getPageNumber());
        assertEquals(size, capturedPageable.getPageSize());
        assertEquals(Sort.Direction.ASC, capturedPageable.getSort().getOrderFor(sortBy).getDirection());
    }

    @Test(expected = BadRequestServiceException.class)
    public void createNegativeTest() {

        final School school=new School();

        UserSignUpRequestDTO userSignUpRequestDTO = new UserSignUpRequestDTO();
        userSignUpRequestDTO.setUserName("Santhosh");
        userSignUpRequestDTO.setPassword("password");
        userSignUpRequestDTO.setSchoolId(Constants.ID);
        userSignUpRequestDTO.setEmail("invalid-email");
        userSignUpRequestDTO.setCreatedBy("admin");
        userSignUpRequestDTO.setUpdatedBy("admin");

        when(this.schoolRepository.findById(userSignUpRequestDTO.getSchoolId())).thenReturn(Optional.of(school));

        this.userService.create(userSignUpRequestDTO);
    }

    @Test(expected = BadRequestServiceException.class)
    public void updateNegativeTest() {

        UserSignUpRequestDTO userSignUpRequestDTO = new UserSignUpRequestDTO();
        userSignUpRequestDTO.setUserName("Santhosh");
        userSignUpRequestDTO.setPassword("password");

        User user = new User();
        user.setActive(true);

        when(this.userRepository.findById(Constants.ID)).thenReturn(Optional.of(user));

        this.userService.update(Constants.ID, userSignUpRequestDTO);
    }

    @Test(expected = BadRequestServiceException.class)
    public void retrieveNegativeTest() {
        User user = new User();
        user.setActive(true);
        when(this.userRepository.findById(Constants.ID)).thenReturn(Optional.of(user));
        this.userService.retrieve(Constants.ID);
    }

    @Test
    public void retrieveAllNegativeTest() {
        when(this.userRepository.retrieveAllUsers()).thenReturn(null);
        ResponseDTO response = this.userService.retrieveAll();
        assertNotNull(response);
        assertEquals(Constants.RETRIEVED, response.getMessage());
        assertEquals(null, response.getData());
    }

    @Test(expected = BadRequestServiceException.class)
    public void removeNegativeTest() {
        User user = new User();
        user.setActive(true);
        when(this.userRepository.findById(Constants.ID)).thenReturn(Optional.of(user));
        this.userService.remove(Constants.ID);
    }
}