package com.fees_payment_service.Service;

import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.dto.ScholarShipRequestDTO;
import com.schoolworld.feesPayment.entity.ScholarShip;
import com.schoolworld.feesPayment.entity.Student;
import com.schoolworld.feesPayment.exception.BadServiceAlertException;
import com.schoolworld.feesPayment.repository.ScholarShipRepository;
import com.schoolworld.feesPayment.repository.StudentRepository;
import com.schoolworld.feesPayment.service.ScholarShipService;
import com.schoolworld.feesPayment.util.AuthenticationService;
import com.schoolworld.feesPayment.util.Constants;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScholarShipTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private ScholarShipRepository scholarShipRepository;

    @InjectMocks
    private ScholarShipService scholarShipService;

    @Test
    public void createTest() {
        ScholarShipRequestDTO scholarShipRequestDTO = new ScholarShipRequestDTO();
        scholarShipRequestDTO.setStudentId(Constants.ID);
        scholarShipRequestDTO.setScholarShip(true);
        scholarShipRequestDTO.setScholarShipAmount(1000);

        ScholarShip scholarShip = new ScholarShip();
        Student student = new Student();
        scholarShip.setScholarShipAmount(scholarShipRequestDTO.getScholarShipAmount());
        scholarShip.setScholarShip(scholarShipRequestDTO.isScholarShip());

        when(authenticationService.getUserName()).thenReturn("santhosh");

        scholarShip.setCreatedBy(authenticationService.getUserName());
        scholarShip.setUpdatedBy(authenticationService.getUserName());

        when(this.studentRepository.findById(scholarShipRequestDTO.getStudentId())).thenReturn(Optional.of(student));
        when(this.scholarShipRepository.save(any(ScholarShip.class))).thenReturn(scholarShip);

        ResponseDTO response = this.scholarShipService.create(scholarShipRequestDTO);

        assertNotNull(response);
        assertEquals(Constants.CREATED, response.getMessage());
        assertEquals(scholarShip, response.getData());
        verify(scholarShipRepository, times(1)).save(any(ScholarShip.class));
    }

    @Test
    public void updateTest() {
        ScholarShipRequestDTO scholarShipRequestDTO = new ScholarShipRequestDTO();
        scholarShipRequestDTO.setStudentId(Constants.ID);
        scholarShipRequestDTO.setScholarShip(true);
        scholarShipRequestDTO.setScholarShipAmount(1000);

        ScholarShip scholarShip = new ScholarShip();

        when(this.scholarShipRepository.findById(Constants.ID)).thenReturn(Optional.of(scholarShip));
        when(this.scholarShipRepository.save(any(ScholarShip.class))).thenReturn(scholarShip);

        ResponseDTO response = this.scholarShipService.update(Constants.ID, scholarShipRequestDTO);

        assertNotNull(response);
        assertEquals(Constants.UPDATED, response.getMessage());
        assertEquals(scholarShip, response.getData());
        verify(scholarShipRepository, times(1)).save(any(ScholarShip.class));
    }

    @Test
    public void retrieveTest() {
        ScholarShip scholarShip = new ScholarShip();

        when(this.scholarShipRepository.findById(Constants.ID)).thenReturn(Optional.of(scholarShip));

        ResponseDTO response = this.scholarShipService.retrieve(Constants.ID);

        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(scholarShip, response.getData());
    }

    @Test
    public void retrieveAllTest() {
        List<ScholarShip> scholarShips = new ArrayList<>();
        when(this.scholarShipRepository.retrieveAll()).thenReturn(scholarShips);

        ResponseDTO response = this.scholarShipService.retrieveAll();

        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(scholarShips, response.getData());
    }

    @Test
    public void removeTest() {

        ScholarShip scholarShip = new ScholarShip();
        when(this.scholarShipRepository.findById(Constants.ID)).thenReturn(Optional.of(scholarShip));
        when(this.scholarShipRepository.save(scholarShip)).thenReturn(scholarShip);

        ResponseDTO response = this.scholarShipService.remove(Constants.ID);

        assertNotNull(response);
        TestCase.assertEquals(Constants.DELETED, response.getMessage());
        TestCase.assertEquals(scholarShip, response.getData());
    }

    @Test
    public void findStudentTest() {
        Student student=new Student();
        when(this.studentRepository.findById(Constants.ID)).thenReturn(Optional.of(student));
        int amount =10;
        when(this.scholarShipRepository.findScholarShipAmountByStudentId(Constants.ID)).thenReturn(amount);
        ResponseDTO response = this.scholarShipService.retrieveStudent(Constants.ID);

        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(10, response.getData());
    }

    @Test(expected = BadServiceAlertException.class)
    public void createNegativeTest() {

        ScholarShipRequestDTO scholarShipRequestDTO = new ScholarShipRequestDTO();
        scholarShipRequestDTO.setStudentId(Constants.ID);
        scholarShipRequestDTO.setScholarShip(true);
        scholarShipRequestDTO.setScholarShipAmount(1000);

        Student student = new Student();
        student.setDelete(true);

        when(this.studentRepository.findById(scholarShipRequestDTO.getStudentId())).thenReturn(Optional.of(student));
        this.scholarShipService.create(scholarShipRequestDTO);
    }

    @Test(expected = BadServiceAlertException.class)
    public void updateNegativeTest() {
        ScholarShipRequestDTO scholarShipRequestDTO = new ScholarShipRequestDTO();
        scholarShipRequestDTO.setStudentId(Constants.ID);
        scholarShipRequestDTO.setScholarShip(true);
        scholarShipRequestDTO.setScholarShipAmount(1000);

        ScholarShip scholarShip = new ScholarShip();
        scholarShip.setDelete(true);

        when(this.scholarShipRepository.findById(Constants.ID)).thenReturn(Optional.of(scholarShip));
        this.scholarShipService.update(Constants.ID, scholarShipRequestDTO);
    }

    @Test(expected = BadServiceAlertException.class)
    public void retrieveNegativeTest() {
        ScholarShip scholarShip = new ScholarShip();
        scholarShip.setDelete(true);
        when(this.scholarShipRepository.findById(Constants.ID)).thenReturn(Optional.of(scholarShip));
        this.scholarShipService.retrieve(Constants.ID);
    }

    @Test
    public void retrieveAllNegativeTest() {
        when(this.scholarShipRepository.retrieveAll()).thenReturn(null);
        ResponseDTO response = this.scholarShipService.retrieveAll();
        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(null, response.getData());
    }

    @Test(expected = BadServiceAlertException.class)
    public void removeNegativeTest() {
        ScholarShip scholarShip = new ScholarShip();
        scholarShip.setDelete(true);
        when(this.scholarShipRepository.findById(Constants.ID)).thenReturn(Optional.of(scholarShip));
        this.scholarShipService.remove(Constants.ID);
    }
}
