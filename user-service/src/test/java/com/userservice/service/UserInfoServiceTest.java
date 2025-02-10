package com.userservice.service;

import com.schoolworld.user.dto.ResponseDTO;
import com.schoolworld.user.dto.UserInfoRequestDTO;
import com.schoolworld.user.entity.User;
import com.schoolworld.user.entity.UserInfo;
import com.schoolworld.user.exception.BadRequestServiceException;
import com.schoolworld.user.repository.UserInfoRepository;
import com.schoolworld.user.repository.UserRepository;
import com.schoolworld.user.service.UserInfoService;
import com.schoolworld.user.util.Constants;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserInfoServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserInfoRepository userInfoRepository;

    @InjectMocks
    private UserInfoService userInfoService;

    @Test
    public void createTest() {

        UserInfoRequestDTO userInfoRequestDTO = new UserInfoRequestDTO();
        userInfoRequestDTO.setUserId(Constants.ID);
        userInfoRequestDTO.setFirstName("Santhosh");
        userInfoRequestDTO.setLastName("M");
        userInfoRequestDTO.setAddress("Chennai");
        userInfoRequestDTO.setGender("Male");
        userInfoRequestDTO.setDateOfBirth(LocalDate.parse("2003-05-10"));
        userInfoRequestDTO.setCreatedBy("Admin");
        userInfoRequestDTO.setUpdatedBy("Admin");

        UserInfo userInfo = new UserInfo();
        User user = new User();
        userInfo.setUser(user);
        userInfo.setFirstName(userInfoRequestDTO.getFirstName());
        userInfo.setLastName(userInfoRequestDTO.getLastName());
        userInfo.setAddress(userInfoRequestDTO.getAddress());
        userInfo.setGender(userInfoRequestDTO.getGender());
        userInfo.setDateOfBirth(userInfoRequestDTO.getDateOfBirth());
        userInfo.setCreatedBy(userInfoRequestDTO.getCreatedBy());
        userInfo.setUpdatedBy(userInfoRequestDTO.getUpdatedBy());

        when(this.userRepository.findById(userInfoRequestDTO.getUserId())).thenReturn(Optional.of(user));
        when(this.userInfoRepository.getUserExist(userInfoRequestDTO.getUserId())).thenReturn(Optional.empty());
        when(this.userInfoRepository.save(any(UserInfo.class))).thenReturn(userInfo);

        ResponseDTO response = userInfoService.create(userInfoRequestDTO);

        assertEquals(Constants.CREATED, response.getMessage());
        assertEquals(HttpStatus.CREATED.getReasonPhrase(), response.getStatus());
        assertNotNull(response.getData());

        verify(userRepository, times(1)).findById(userInfoRequestDTO.getUserId());
    }

    @Test
    public void updateTest() {
        UserInfoRequestDTO userInfoRequestDTO = new UserInfoRequestDTO();
        userInfoRequestDTO.setFirstName("Santhosh");
        userInfoRequestDTO.setLastName("M");
        userInfoRequestDTO.setAddress("Chennai");

        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName(userInfoRequestDTO.getFirstName());
        userInfo.setLastName(userInfoRequestDTO.getLastName());
        userInfo.setAddress(userInfoRequestDTO.getAddress());
        userInfo.setDelete(false);

        when(this.userInfoRepository.findById(Constants.ID)).thenReturn(Optional.of(userInfo));
        when(this.userInfoRepository.save(any(UserInfo.class))).thenReturn(userInfo);

        ResponseDTO response = this.userInfoService.update(Constants.ID, userInfoRequestDTO);

        assertNotNull(response);
        TestCase.assertEquals(Constants.UPDATED, response.getMessage());
        TestCase.assertEquals(userInfo, response.getData());
        verify(userInfoRepository, times(1)).save(any(UserInfo.class));
    }

    @Test
    public void retrieveUserTest() {
        UserInfo userInfo = new UserInfo();

        when(this.userInfoRepository.findById(Constants.ID)).thenReturn(Optional.of(userInfo));

        ResponseDTO response = this.userInfoService.retrieve(Constants.ID);

        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(userInfo, response.getData());
    }

    @Test
    public void retrieveAllUserTest() {
        List<UserInfo> users = new ArrayList<>();
        when(this.userInfoRepository.retrieveAllUserInfoValues()).thenReturn(users);

        ResponseDTO response = this.userInfoService.retrieveAll();

        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(users, response.getData());
    }

    @Test
    public void removeUserTest() {

        UserInfo userInfo = new UserInfo();
        when(this.userInfoRepository.findById(Constants.ID)).thenReturn(Optional.of(userInfo));
        when(this.userInfoRepository.save(userInfo)).thenReturn(userInfo);

        ResponseDTO response = this.userInfoService.remove(Constants.ID);

        assertNotNull(response);
        TestCase.assertEquals(Constants.REMOVED, response.getMessage());
        TestCase.assertEquals(userInfo, response.getData());
    }

    @Test(expected = BadRequestServiceException.class)
    public void createNegativeTest() {

        UserInfoRequestDTO userInfoRequestDTO = new UserInfoRequestDTO();
        userInfoRequestDTO.setUserId(Constants.ID);
        userInfoRequestDTO.setFirstName("Santhosh");
        userInfoRequestDTO.setLastName("M");

        User user = new User();
        user.setActive(true);
        when(this.userRepository.findById(userInfoRequestDTO.getUserId())).thenReturn(Optional.of(user));
        this.userInfoService.create(userInfoRequestDTO);
    }

    @Test(expected = BadRequestServiceException.class)
    public void updateNegativeTest() {
        UserInfoRequestDTO userInfoRequestDTO = new UserInfoRequestDTO();
        userInfoRequestDTO.setFirstName("Santhosh");
        userInfoRequestDTO.setLastName("M");
        userInfoRequestDTO.setAddress("Chennai");
        userInfoRequestDTO.setGender("Male");

        UserInfo userInfo = new UserInfo();
        userInfo.setDelete(true);
        when(this.userInfoRepository.findById(userInfoRequestDTO.getUserId())).thenReturn(Optional.of(userInfo));
        this.userInfoService.update(Constants.ID, userInfoRequestDTO);
    }

    @Test(expected = BadRequestServiceException.class)
    public void retrieveNegativeTest() {
        UserInfo userInfo = new UserInfo();
        userInfo.setDelete(true);
        when(this.userInfoRepository.findById(Constants.ID)).thenReturn(Optional.of(userInfo));
        this.userInfoService.retrieve(Constants.ID);
    }

    @Test
    public void retrieveAllNegativeTest() {
        when(this.userInfoRepository.retrieveAllUserInfoValues()).thenReturn(null);
        ResponseDTO response = this.userInfoService.retrieveAll();
        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(null, response.getData());
    }

    @Test(expected = BadRequestServiceException.class)
    public void removeNegativeTest() {
        UserInfo userInfo = new UserInfo();
        userInfo.setDelete(true);
        when(this.userInfoRepository.findById(Constants.ID)).thenReturn(Optional.of(userInfo));
        this.userInfoService.remove(Constants.ID);
    }
}
