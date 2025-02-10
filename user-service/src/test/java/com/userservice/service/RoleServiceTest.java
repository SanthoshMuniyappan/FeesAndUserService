package com.userservice.service;

import com.schoolworld.user.dto.ResponseDTO;
import com.schoolworld.user.dto.RoleRequestDTO;
import com.schoolworld.user.entity.ERole;
import com.schoolworld.user.entity.Role;
import com.schoolworld.user.entity.School;
import com.schoolworld.user.entity.Student;
import com.schoolworld.user.entity.Teacher;
import com.schoolworld.user.entity.User;
import com.schoolworld.user.exception.BadRequestServiceException;
import com.schoolworld.user.repository.RoleRepository;
import com.schoolworld.user.repository.StudentRepository;
import com.schoolworld.user.repository.TeacherRepository;
import com.schoolworld.user.repository.UserRepository;
import com.schoolworld.user.service.RoleService;
import com.schoolworld.user.util.Constants;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void createTest() {

        School school = new School();
        school.setId(Constants.ID);

        Teacher teacher = new Teacher();
        teacher.setRegNumber("TC1234");
        teacher.setSchool(school);

        when(this.teacherRepository.getTeacherByRegNumber(teacher.getRegNumber())).thenReturn(teacher);

        User teacherUser = new User();
        teacherUser.setId(Constants.ID);
        teacherUser.setRegNumber("TC1234");
        teacherUser.setSchool(school);
        teacherUser.setActive(false);

        when(this.userRepository.save(any(User.class))).thenReturn(teacherUser);


        RoleRequestDTO roleRequestDTOForTeacher = new RoleRequestDTO();
        roleRequestDTOForTeacher.setUserId(Constants.ID);
        roleRequestDTOForTeacher.setRole(ERole.ROLE_MODERATOR);
        roleRequestDTOForTeacher.setCreatedBy("admin");
        roleRequestDTOForTeacher.setUpdatedBy("admin");

        when(this.userRepository.findById(teacherUser.getId())).thenReturn(Optional.of(teacherUser));

        ResponseDTO responseForTeacher = roleService.create(roleRequestDTOForTeacher);
        assertEquals("Created", responseForTeacher.getStatus());

        Student student = new Student();
        student.setRegNumber("ST1234");
        student.setSchool(school);
        student.setId(Constants.ID);
        student.setDelete(false);

        when(this.studentRepository.getStudentByRegisterNumber(student.getRegNumber())).thenReturn(student);

        User studentUser = new User();
        studentUser.setId(Constants.ID);
        studentUser.setRegNumber("ST1234");
        studentUser.setSchool(school);
        studentUser.setActive(false);

        when(this.userRepository.save(any(User.class))).thenReturn(studentUser);

        RoleRequestDTO roleRequestDTOForStudent = new RoleRequestDTO();
        roleRequestDTOForStudent.setUserId(Constants.ID);
        roleRequestDTOForStudent.setRole(ERole.ROLE_END_USER);
        roleRequestDTOForStudent.setCreatedBy("admin");
        roleRequestDTOForStudent.setUpdatedBy("admin");

        when(this.userRepository.findById(studentUser.getId())).thenReturn(Optional.of(studentUser));

        ResponseDTO responseForStudent = roleService.create(roleRequestDTOForStudent);

        assertEquals("Created", responseForStudent.getStatus());
    }

    @Test
    public void updateTest() {
        RoleRequestDTO roleRequestDTO = new RoleRequestDTO();
        roleRequestDTO.setRole(ERole.ROLE_ADMINISTRATOR);

        Role role = new Role();
        when(this.roleRepository.findById(Constants.ID)).thenReturn(Optional.of(role));
        when(this.roleRepository.save(any(Role.class))).thenReturn(role);

        ResponseDTO response = roleService.update(Constants.ID, roleRequestDTO);

        assertEquals(Constants.UPDATED, response.getMessage());
        assertEquals(HttpStatus.OK.getReasonPhrase(), response.getStatus());
        assertNotNull(response.getData());
    }

    @Test
    public void retrieveUserTest() {
        Role role = new Role();

        when(this.roleRepository.findById(Constants.ID)).thenReturn(Optional.of(role));

        ResponseDTO response = this.roleService.retrieve(Constants.ID);

        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(role, response.getData());
    }

    @Test
    public void retrieveAllUserTest() {
        List<Role> roles = new ArrayList<>();
        when(this.roleRepository.retrieveAllRoles()).thenReturn(roles);

        ResponseDTO response = this.roleService.retrieveAll();

        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(roles, response.getData());
    }

    @Test
    public void removeUserTest() {

        Role role = new Role();
        when(this.roleRepository.findById(Constants.ID)).thenReturn(Optional.of(role));
        when(this.roleRepository.save(role)).thenReturn(role);

        ResponseDTO response = this.roleService.remove(Constants.ID);

        assertNotNull(response);
        TestCase.assertEquals(Constants.REMOVED, response.getMessage());
        TestCase.assertEquals(role, response.getData());
    }

    @Test(expected = BadRequestServiceException.class)
    public void createNegativeTest() {
        RoleRequestDTO roleRequestDTO = new RoleRequestDTO();
        roleRequestDTO.setUserId(Constants.ID);

        User user = new User();
        user.setActive(true);
        when(this.userRepository.findById(roleRequestDTO.getUserId())).thenReturn(Optional.of(user));
        this.roleService.create(roleRequestDTO);
    }

    @Test(expected = BadRequestServiceException.class)
    public void updateNegativeTest() {
        RoleRequestDTO roleRequestDTO = new RoleRequestDTO();
        roleRequestDTO.setRole(ERole.ROLE_ADMINISTRATOR);

        Role role = new Role();
        role.setDelete(true);
        when(this.roleRepository.findById(Constants.ID)).thenReturn(Optional.of(role));
        this.roleService.update(Constants.ID, roleRequestDTO);
    }

    @Test(expected = BadRequestServiceException.class)
    public void retrieveNegativeTest() {
        Role role = new Role();
        role.setDelete(true);
        when(this.roleRepository.findById(Constants.ID)).thenReturn(Optional.of(role));
        this.roleService.retrieve(Constants.ID);
    }

    @Test
    public void retrieveAllNegativeTest() {
        when(this.roleRepository.retrieveAllRoles()).thenReturn(null);
        ResponseDTO response = this.roleService.retrieveAll();
        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(null, response.getData());
    }

    @Test(expected = BadRequestServiceException.class)
    public void removeNegativeTest() {
        Role role = new Role();
        role.setDelete(true);
        when(this.roleRepository.findById(Constants.ID)).thenReturn(Optional.of(role));
        this.roleService.remove(Constants.ID);
    }
}
