package com.schoolworld.user.service;

import com.schoolworld.user.dto.ResponseDTO;
import com.schoolworld.user.dto.UserInfoRequestDTO;
import com.schoolworld.user.entity.User;
import com.schoolworld.user.entity.UserInfo;
import com.schoolworld.user.exception.BadRequestServiceException;
import com.schoolworld.user.repository.UserInfoRepository;
import com.schoolworld.user.repository.UserRepository;
import com.schoolworld.user.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseDTO create(final UserInfoRequestDTO userInfoRequestDTO) {
        final UserInfo userInfo = new UserInfo();
        final User user = this.userRepository.findById(userInfoRequestDTO.getUserId()).orElseThrow(() -> new BadRequestServiceException(Constants.USER_ID_NOT_FOUND));
        if (!user.isActive()) {
            final Optional<UserInfo> userData = this.userInfoRepository.getUserExist(userInfoRequestDTO.getUserId());
            if (userData.isEmpty()) {
                userInfo.setFirstName(userInfoRequestDTO.getFirstName());
                userInfo.setUser(user);
                userInfo.setLastName(userInfoRequestDTO.getLastName());
                userInfo.setAddress(userInfoRequestDTO.getAddress());
                userInfo.setGender(userInfoRequestDTO.getGender());
                userInfo.setDateOfBirth(userInfoRequestDTO.getDateOfBirth());
                userInfo.setCreatedBy(userInfoRequestDTO.getCreatedBy());
                userInfo.setUpdatedBy(userInfoRequestDTO.getUpdatedBy());
                return new ResponseDTO(Constants.CREATED, this.userInfoRepository.save(userInfo), HttpStatus.CREATED.getReasonPhrase());
            } else {
                throw new BadRequestServiceException(Constants.USER_ALREADY_EXIST);
            }
        } else {
            throw new BadRequestServiceException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    @Transactional
    public ResponseDTO update(final String id, final UserInfoRequestDTO userInfoRequestDTO) {
        final UserInfo userInfo = this.userInfoRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constants.USER_INFO_NOT_FOUND));
        if (!userInfo.isDelete()) {
            if (userInfoRequestDTO.getFirstName() != null) {
                userInfo.setFirstName(userInfoRequestDTO.getFirstName());
            }
            if (userInfoRequestDTO.getLastName() != null) {
                userInfo.setLastName(userInfoRequestDTO.getLastName());
            }
            if (userInfoRequestDTO.getAddress() != null) {
                userInfo.setAddress(userInfoRequestDTO.getAddress());
            }
            if (userInfoRequestDTO.getGender() != null) {
                userInfo.setGender(userInfoRequestDTO.getGender());
            }
            if (userInfoRequestDTO.getDateOfBirth() != null) {
                userInfo.setDateOfBirth(userInfoRequestDTO.getDateOfBirth());
            }
            if (userInfoRequestDTO.getCreatedBy() != null) {
                userInfo.setCreatedBy(userInfoRequestDTO.getCreatedBy());
            }
            return new ResponseDTO(Constants.UPDATED, this.userInfoRepository.save(userInfo), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadRequestServiceException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO retrieve(final String id) {
        final UserInfo userInfo = this.userInfoRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constants.USER_INFO_NOT_FOUND));
        if (!userInfo.isDelete()) {
            return new ResponseDTO(Constants.RETRIEVED, userInfo, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadRequestServiceException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO retrieveAll() {
        return new ResponseDTO(Constants.RETRIEVED, this.userInfoRepository.retrieveAllUserInfoValues(), HttpStatus.OK.getReasonPhrase());
    }

    public ResponseDTO remove(final String id) {
        final UserInfo userInfo = this.userInfoRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constants.USER_INFO_NOT_FOUND));
        if (!userInfo.isDelete()) {
            userInfo.setDelete(true);
            return new ResponseDTO(Constants.REMOVED, this.userInfoRepository.save(userInfo), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadRequestServiceException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public Page<UserInfo> getUsers(int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Order.by(sortBy));
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return userInfoRepository.findByIsDeleteFalse(pageable);
    }
}
