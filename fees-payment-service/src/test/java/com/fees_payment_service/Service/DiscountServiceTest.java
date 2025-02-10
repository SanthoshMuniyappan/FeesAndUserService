package com.fees_payment_service.Service;

import com.schoolworld.feesPayment.dto.DiscountRequestDTO;
import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.entity.Discount;
import com.schoolworld.feesPayment.entity.Student;
import com.schoolworld.feesPayment.exception.BadServiceAlertException;
import com.schoolworld.feesPayment.repository.DiscountRepository;
import com.schoolworld.feesPayment.repository.StudentRepository;
import com.schoolworld.feesPayment.service.DiscountService;
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
public class DiscountServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private DiscountService discountService;

    @Test
    public void createTest() {
        DiscountRequestDTO discountRequestDTO = new DiscountRequestDTO();
        discountRequestDTO.setStudentId(Constants.ID);
        discountRequestDTO.setIsDiscount(true);
        discountRequestDTO.setDiscountAmount(2000);

        Discount discount = new Discount();
        Student student = new Student();
        discount.setDiscount(discountRequestDTO.getIsDiscount());
        discount.setDiscountAmount(discountRequestDTO.getDiscountAmount());

        when(authenticationService.getUserName()).thenReturn("santhosh");

        discount.setCreatedBy(authenticationService.getUserName());
        discount.setUpdatedBy(authenticationService.getUserName());

        when(this.studentRepository.findById(discountRequestDTO.getStudentId())).thenReturn(Optional.of(student));
        when(this.discountRepository.save(any(Discount.class))).thenReturn(discount);

        ResponseDTO response = this.discountService.create(discountRequestDTO);

        assertNotNull(response);
        assertEquals(Constants.CREATED, response.getMessage());
        assertEquals(discount, response.getData());
        verify(discountRepository, times(1)).save(any(Discount.class));
    }

    @Test
    public void updateTest() {
        DiscountRequestDTO discountRequestDTO = new DiscountRequestDTO();
        discountRequestDTO.setStudentId(Constants.ID);
        discountRequestDTO.setIsDiscount(true);
        discountRequestDTO.setDiscountAmount(2000);

        Discount discount = new Discount();

        when(this.discountRepository.findById(Constants.ID)).thenReturn(Optional.of(discount));
        when(this.discountRepository.save(any(Discount.class))).thenReturn(discount);

        ResponseDTO response = this.discountService.update(Constants.ID, discountRequestDTO);

        assertNotNull(response);
        assertEquals(Constants.UPDATED, response.getMessage());
        assertEquals(discount, response.getData());
        verify(discountRepository, times(1)).save(any(Discount.class));
    }

    @Test
    public void retrieveTest() {
        Discount discount = new Discount();

        when(this.discountRepository.findById(Constants.ID)).thenReturn(Optional.of(discount));

        ResponseDTO response = this.discountService.retrieve(Constants.ID);

        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(discount, response.getData());
    }

    @Test
    public void retrieveAllTest() {
        List<Discount> discounts = new ArrayList<>();
        when(this.discountRepository.retrieveAll()).thenReturn(discounts);

        ResponseDTO response = this.discountService.retrieveAll();

        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(discounts, response.getData());
    }

    @Test
    public void removeTest() {

        Discount discount = new Discount();
        when(this.discountRepository.findById(Constants.ID)).thenReturn(Optional.of(discount));
        when(this.discountRepository.save(discount)).thenReturn(discount);

        ResponseDTO response = this.discountService.remove(Constants.ID);

        assertNotNull(response);
        TestCase.assertEquals(Constants.DELETED, response.getMessage());
        TestCase.assertEquals(discount, response.getData());
    }

    @Test
    public void findStudentTest() {
        Discount discount = new Discount();

        when(this.discountRepository.findDiscountByStudentId(Constants.ID)).thenReturn(discount);

        ResponseDTO response = this.discountService.studentDiscountAmount(Constants.ID);

        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(0, response.getData());
    }

    @Test(expected = BadServiceAlertException.class)
    public void createNegativeTest() {
        DiscountRequestDTO discountRequestDTO = new DiscountRequestDTO();
        discountRequestDTO.setStudentId(Constants.ID);

        Student student = new Student();
        student.setDelete(true);

        when(this.studentRepository.findById(discountRequestDTO.getStudentId())).thenReturn(Optional.of(student));
        this.discountService.create(discountRequestDTO);
    }

    @Test(expected = BadServiceAlertException.class)
    public void updateNegativeTest() {
        DiscountRequestDTO discountRequestDTO = new DiscountRequestDTO();
        discountRequestDTO.setDiscountAmount(2000);

        Discount discount = new Discount();
        discount.setDelete(true);
        when(this.discountRepository.findById(Constants.ID)).thenReturn(Optional.of(discount));
        this.discountService.update(Constants.ID, discountRequestDTO);
    }

    @Test(expected = BadServiceAlertException.class)
    public void retrieveNegativeTest() {
        Discount discount = new Discount();
        discount.setDelete(true);
        when(this.discountRepository.findById(Constants.ID)).thenReturn(Optional.of(discount));
        this.discountService.retrieve(Constants.ID);
    }

    @Test
    public void retrieveAllNegativeTest() {
        when(this.discountRepository.retrieveAll()).thenReturn(null);
        ResponseDTO response = this.discountService.retrieveAll();
        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(null, response.getData());
    }

    @Test(expected = BadServiceAlertException.class)
    public void removeNegativeTest() {
        Discount discount = new Discount();
        discount.setDelete(true);
        when(this.discountRepository.findById(Constants.ID)).thenReturn(Optional.of(discount));
        this.discountService.remove(Constants.ID);
    }
}
