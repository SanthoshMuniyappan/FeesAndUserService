package com.fees_payment_service.Service;
import com.schoolworld.feesPayment.dto.PaymentRequestDTO;
import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.entity.Discount;
import com.schoolworld.feesPayment.entity.Fees;
import com.schoolworld.feesPayment.entity.Payment;
import com.schoolworld.feesPayment.entity.ScholarShip;
import com.schoolworld.feesPayment.entity.Standard;
import com.schoolworld.feesPayment.entity.Student;
import com.schoolworld.feesPayment.exception.BadServiceAlertException;
import com.schoolworld.feesPayment.repository.DiscountRepository;
import com.schoolworld.feesPayment.repository.FeesRepository;
import com.schoolworld.feesPayment.repository.PaymentRepository;
import com.schoolworld.feesPayment.repository.ScholarShipRepository;
import com.schoolworld.feesPayment.repository.StudentRepository;
import com.schoolworld.feesPayment.service.PaymentService;
import com.schoolworld.feesPayment.util.AuthenticationService;
import com.schoolworld.feesPayment.util.Constants;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private FeesRepository feesRepository;

    @Mock
    private ScholarShipRepository scholarShipRepository;

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private AuthenticationService authenticationService;

    @Test
    public void testCreatePayment() {


        int payAmount = 1500;
        String paymentType = "Book Fees";

        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setStudentId(Constants.ID);
        paymentRequestDTO.setPaymentType(paymentType);
        paymentRequestDTO.setPayAmount(payAmount);

        Standard standard=new Standard();
        standard.setId(Constants.ID);

        Student student = new Student();
        student.setId(paymentRequestDTO.getStudentId());
        student.setDelete(false);
        student.setStandard(standard);
        student.setOutstandingFees(5000);

        Payment payment=new Payment();

        ScholarShip scholarship = new ScholarShip();
        scholarship.setScholarShipAmount(500);

        Discount discount = new Discount();
        discount.setDiscountAmount(200);

        List<Fees> fees = new ArrayList<>();

        when(this.studentRepository.findById(Constants.ID)).thenReturn(Optional.of(student));
        when(this.paymentRepository.retrieveByStudentId(Constants.ID)).thenReturn(Collections.emptyList());
        when(this.feesRepository.findAllByStandard(student.getStandard().getId())).thenReturn(fees);
        when(this.scholarShipRepository.findScholarShipAmountByStudentId(Constants.ID)).thenReturn(scholarship.getScholarShipAmount());
        when(this.discountRepository.findDiscountByStudentId(Constants.ID)).thenReturn(discount);
        when(this.authenticationService.getUserName()).thenReturn("TestUser");
        when(this.paymentRepository.save(any(Payment.class))).thenReturn(payment);

        ResponseDTO response = paymentService.create(paymentRequestDTO);

        assertNotNull(response);
        assertEquals(Constants.CREATED, response.getMessage());
        assertEquals(payment,response.getData());

    }

    @Test
    public void testRetrievePayAmount(){
        List<Payment> payments=new ArrayList<>();

        when(paymentRepository.retrieveByStudentId(Constants.ID)).thenReturn(payments);

        ResponseDTO response = paymentService.retrievePayAmount(Constants.ID);

        assertNotNull(response);
        assertEquals(Constants.RETRIEVED, response.getMessage());
        assertEquals(0, response.getData());
    }

    @Test
    public void testRetrieveUnPayAmount(){
        Student student=new Student();
        when(studentRepository.findById(Constants.ID)).thenReturn(Optional.of(student));

        ResponseDTO response = paymentService.retrieveUnPaidAmount(Constants.ID);

        assertNotNull(response);
        assertEquals(Constants.RETRIEVED, response.getMessage());
        assertEquals(0, response.getData());
    }

    @Test
    public void testRetrieveAmount(){
        Payment payment = new Payment();
        when(paymentRepository.findById(Constants.ID)).thenReturn(Optional.of(payment));

        ResponseDTO response = paymentService.retrieve(Constants.ID);

        assertNotNull(response);
        assertEquals(Constants.RETRIEVED, response.getMessage());
        assertEquals(payment, response.getData());
    }

    @Test
    public void testRetrieveAll(){
        List<Payment> payments = new ArrayList<>();
        when(this.paymentRepository.retrieveAll()).thenReturn(payments);

        ResponseDTO response = this.paymentService.retrieveAll();

        Assertions.assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(payments, response.getData());
    }

    @Test
    public void removeTest() {

        Payment payment=new Payment();
        when(this.paymentRepository.findById(Constants.ID)).thenReturn(Optional.of(payment));
        when(this.paymentRepository.save(payment)).thenReturn(payment);

        ResponseDTO response = this.paymentService.remove(Constants.ID);

        Assertions.assertNotNull(response);
        TestCase.assertEquals(Constants.DELETED, response.getMessage());
        TestCase.assertEquals(payment, response.getData());
    }

    @Test
    public void testRetrievePaidFeesTypeStatus(){
        Student student = new Student();
        Standard standard=new Standard();
        student.setId(Constants.ID);
        student.setDelete(false);
        student.setStandard(standard);

        when(studentRepository.findById(Constants.ID)).thenReturn(Optional.of(student));

        when(paymentRepository.getStudentPaymentByFeesType(Constants.ID, "Book Fees")).thenReturn(List.of());

        ResponseDTO response = paymentService.retrievePaidFeesTypeStatus(Constants.ID, "Book Fees");

        assertNotNull(response);
        assertEquals(Constants.RETRIEVED, response.getMessage());
        assertEquals(0, response.getData());
    }

    @Test
    public void testRetrieveUnPaidFeesTypeStatus(){
        Student student = new Student();
        Standard standard=new Standard();
        student.setId(Constants.ID);
        student.setDelete(false);
        student.setStandard(standard);
        when(studentRepository.findById(Constants.ID)).thenReturn(Optional.of(student));
        when(feesRepository.getStudentFeesTypeAmount(student.getStandard().getId(), "Book Fees")).thenReturn(5000);
        when(paymentRepository.getStudentPaymentByFeesType(Constants.ID, "Book Fees")).thenReturn(List.of());

        ResponseDTO response = paymentService.retrieveUnPaidFeesTypeStatus(Constants.ID, "Book Fees");

        assertNotNull(response);
        assertEquals(Constants.RETRIEVED, response.getMessage());
        assertEquals(5000, response.getData());
    }

    @Test(expected = BadServiceAlertException.class)
    public void testNegativeCreate(){
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setStudentId("existingStudentId");
        paymentRequestDTO.setPaymentType("Book Fees");
        paymentRequestDTO.setPayAmount(1500);

        Student student = new Student();
        Standard standard=new Standard();
        student.setId(paymentRequestDTO.getStudentId());
        student.setDelete(true);
        student.setStandard(standard);

        when(studentRepository.findById(paymentRequestDTO.getStudentId())).thenReturn(Optional.of(student));

        paymentService.create(paymentRequestDTO);
    }

    @Test(expected = BadServiceAlertException.class)
    public void retrieveNegativeTest() {
        Payment payment=new Payment();
        payment.setDelete(true);
        when(this.paymentRepository.findById(Constants.ID)).thenReturn(Optional.of(payment));
        this.paymentService.retrieve(Constants.ID);
    }

    @Test
    public void retrieveAllNegativeTest() {
        when(this.paymentRepository.retrieveAll()).thenReturn(null);
        ResponseDTO response = this.paymentService.retrieveAll();
        Assertions.assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(null, response.getData());
    }

    @Test(expected = BadServiceAlertException.class)
    public void removeNegativeTest() {
        Payment payment=new Payment();
        payment.setDelete(true);
        when(this.paymentRepository.findById(Constants.ID)).thenReturn(Optional.of(payment));
        this.paymentService.remove(Constants.ID);
    }
}
