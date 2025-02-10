package com.schoolworld.user.service;

import com.schoolworld.user.dto.ResponseDTO;
import com.schoolworld.user.dto.UserSignUpRequestDTO;
import com.schoolworld.user.entity.School;
import com.schoolworld.user.entity.User;
import com.schoolworld.user.exception.BadRequestServiceException;
import com.schoolworld.user.repository.SchoolRepository;
import com.schoolworld.user.repository.UserRepository;
import com.schoolworld.user.util.Constants;
import com.schoolworld.user.util.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseDTO create(final UserSignUpRequestDTO userSignUpRequestDTO) {
        final User user = new User();
        final School school = this.schoolRepository.findById(userSignUpRequestDTO.getSchoolId()).orElseThrow(() -> new BadRequestServiceException(Constants.SCHOOL_NOT_FOUND));
        if (!school.isDelete()) {
            user.setUserName(userSignUpRequestDTO.getUserName());
            user.setPassword(this.passwordEncoder.encode(userSignUpRequestDTO.getPassword()));
            if (UtilService.emailValidation(userSignUpRequestDTO.getEmail())) {
                List<User> users = this.userRepository.findByEmail(userSignUpRequestDTO.getEmail());
                if (users.isEmpty()) {
                    user.setEmail(userSignUpRequestDTO.getEmail());
                } else {
                    throw new BadRequestServiceException(Constants.EMAIL_IS_ALREADY_EXIST);
                }
            } else {
                throw new BadRequestServiceException(Constants.EMAIL_NOT_VALID + Constants.EMAIL_PATTERN);
            }
            user.setSchool(school);
            user.setRegNumber(userSignUpRequestDTO.getRegNumber());
            user.setCreatedBy(userSignUpRequestDTO.getCreatedBy());
            user.setUpdatedBy(userSignUpRequestDTO.getUpdatedBy());
            return new ResponseDTO(Constants.CREATED, this.userRepository.save(user), HttpStatus.CREATED.getReasonPhrase());
        } else {
            throw new BadRequestServiceException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    @Transactional
    public ResponseDTO update(final String id, final UserSignUpRequestDTO userSignUpRequestDTO) {
        final User user = this.userRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constants.USER_ID_NOT_FOUND));
        if (!user.isActive()) {
            if (userSignUpRequestDTO.getUserName() != null) {
                user.setUserName(userSignUpRequestDTO.getUserName());
            }
            if (userSignUpRequestDTO.getPassword() != null) {
                user.setPassword(this.passwordEncoder.encode(userSignUpRequestDTO.getPassword()));
            }
            if (userSignUpRequestDTO.getUpdatedBy() != null) {
                user.setCreatedBy(userSignUpRequestDTO.getUpdatedBy());
            }
            return new ResponseDTO(Constants.UPDATED, this.userRepository.save(user), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadRequestServiceException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO retrieve(final String id) {
        final User user = this.userRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constants.USER_ID_NOT_FOUND));
        if (!user.isActive()) {
            return new ResponseDTO(Constants.RETRIEVED, user, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadRequestServiceException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO retrieveAll() {
        return new ResponseDTO(Constants.RETRIEVED, this.userRepository.retrieveAllUsers(), HttpStatus.OK.getReasonPhrase());
    }

    public ResponseDTO remove(final String id) {
        final User user = this.userRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constants.USER_ID_NOT_FOUND));
        if (!user.isActive()) {
            user.setActive(true);
            return new ResponseDTO(Constants.REMOVED, this.userRepository.save(user), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadRequestServiceException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public Page<User> getUsers(int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Order.by(sortBy));
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findByIsDeleteFalse(pageable);
    }
}
