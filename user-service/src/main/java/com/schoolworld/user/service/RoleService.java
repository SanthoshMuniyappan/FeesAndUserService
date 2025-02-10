package com.schoolworld.user.service;

import com.schoolworld.user.dto.ResponseDTO;
import com.schoolworld.user.dto.RoleRequestDTO;
import com.schoolworld.user.entity.ERole;
import com.schoolworld.user.entity.Role;
import com.schoolworld.user.entity.Student;
import com.schoolworld.user.entity.Teacher;
import com.schoolworld.user.entity.User;
import com.schoolworld.user.exception.BadRequestServiceException;
import com.schoolworld.user.repository.RoleRepository;
import com.schoolworld.user.repository.SchoolRepository;
import com.schoolworld.user.repository.StudentRepository;
import com.schoolworld.user.repository.TeacherRepository;
import com.schoolworld.user.repository.UserRepository;
import com.schoolworld.user.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseDTO create(final RoleRequestDTO roleRequestDTO) {
        final Role role = new Role();
        final User user = this.userRepository.findById(roleRequestDTO.getUserId()).orElseThrow(() -> new BadRequestServiceException(Constants.USER_ID_NOT_FOUND));
        if (!user.isActive()) {
            role.setUser(user);
            if (user.getRegNumber().startsWith("TC")) {
                final Teacher teacher = this.teacherRepository.getTeacherByRegNumber(user.getRegNumber());
                if (teacher.getSchool().getId().equals(user.getSchool().getId())) {
                    if (ERole.ROLE_MODERATOR.equals(roleRequestDTO.getRole())) {
                        role.setRole(roleRequestDTO.getRole());
                    } else {
                        throw new BadRequestServiceException(Constants.NOT_APPLICABLE);
                    }
                } else {
                    throw new BadRequestServiceException(Constants.USER_NOT_IN_SCHOOL);
                }
            }
            if (user.getRegNumber().startsWith("ST")) {
                final Student student = this.studentRepository.getStudentByRegisterNumber(user.getRegNumber());
                if (student.getSchool().getId().equals(user.getSchool().getId())) {
                    if (ERole.ROLE_END_USER.equals(roleRequestDTO.getRole())) {
                        role.setRole(roleRequestDTO.getRole());
                    } else {
                        throw new BadRequestServiceException(Constants.NOT_APPLICABLE);
                    }
                } else {
                    throw new BadRequestServiceException(Constants.USER_NOT_IN_SCHOOL);
                }
            }
            role.setCreatedBy(roleRequestDTO.getCreatedBy());
            role.setUpdatedBy(roleRequestDTO.getUpdatedBy());
            return new ResponseDTO(Constants.CREATED, this.roleRepository.save(role), HttpStatus.CREATED.getReasonPhrase());
        } else {
            throw new BadRequestServiceException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    @Transactional
    public ResponseDTO update(final String id, final RoleRequestDTO roleRequestDTO) {
        final Role role = this.roleRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constants.ROLE_ID_NOT_FOUND));
        if (!role.isDelete()) {
            if (roleRequestDTO.getRole() != null) {
                role.setRole(roleRequestDTO.getRole());
            }
            if (roleRequestDTO.getUpdatedBy() != null) {
                role.setUpdatedBy(roleRequestDTO.getUpdatedBy());
            }
            return new ResponseDTO(Constants.UPDATED, this.roleRepository.save(role), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadRequestServiceException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO retrieve(final String id) {
        final Role role = this.roleRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constants.ROLE_ID_NOT_FOUND));
        if (!role.isDelete()) {
            return new ResponseDTO(Constants.RETRIEVED, role, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadRequestServiceException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO retrieveAll() {
        return new ResponseDTO(Constants.RETRIEVED, this.roleRepository.retrieveAllRoles(), HttpStatus.OK.getReasonPhrase());
    }

    public ResponseDTO remove(final String id) {
        final Role role = this.roleRepository.findById(id).orElseThrow(() -> new BadRequestServiceException(Constants.ROLE_ID_NOT_FOUND));
        if (!role.isDelete()) {
            role.setDelete(true);
            return new ResponseDTO(Constants.REMOVED, this.roleRepository.save(role), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadRequestServiceException(Constants.ID_DELETED_IN_TABLE);
        }

    }
}
