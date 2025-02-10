package com.schoolworld.feesPayment.service;

import com.schoolworld.feesPayment.dto.PaymentRequestDTO;
import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.entity.Discount;
import com.schoolworld.feesPayment.entity.Fees;
import com.schoolworld.feesPayment.entity.Payment;
import com.schoolworld.feesPayment.entity.Student;
import com.schoolworld.feesPayment.exception.BadServiceAlertException;
import com.schoolworld.feesPayment.exception.StudentNotFoundException;
import com.schoolworld.feesPayment.repository.DiscountRepository;
import com.schoolworld.feesPayment.repository.FeesRepository;
import com.schoolworld.feesPayment.repository.PaymentRepository;
import com.schoolworld.feesPayment.repository.ScholarShipRepository;
import com.schoolworld.feesPayment.repository.StudentRepository;
import com.schoolworld.feesPayment.util.AuthenticationService;
import com.schoolworld.feesPayment.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FeesRepository feesRepository;

    @Autowired
    private ScholarShipRepository scholarShipRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Transactional
    public ResponseDTO create(final PaymentRequestDTO paymentRequestDTO) {
        final Payment payment = new Payment();
        final Student student = this.studentRepository.findById(paymentRequestDTO.getStudentId()).orElseThrow(() -> new StudentNotFoundException(Constants.ID_NOT_FOUND));
        if (!student.isDelete()) {
            final List<Payment> studentPayment = this.paymentRepository.retrieveByStudentId(student.getId());
            final List<Fees> fees = this.feesRepository.findAllByStandard(student.getStandard().getId());
            if (!studentPayment.isEmpty()) {
                final Payment studentPaymentAmount = studentPayment.get(0);
                int totalAmount = studentPaymentAmount.getTotalAmount();
                payment.setTotalAmount(totalAmount);
                int outStandingAmount = student.getOutstandingFees();
                outStandingAmount = outStandingAmount - paymentRequestDTO.getPayAmount();
                updateFees(outStandingAmount == paymentRequestDTO.getPayAmount(), outStandingAmount, student.getId());
            } else {
                int totalAmount = fees.stream().mapToInt(Fees::getFeesAmount).sum();
                final int scholarShipAmount = this.scholarShipRepository.findScholarShipAmountByStudentId(student.getId());
                totalAmount = totalAmount - scholarShipAmount;
                final Discount discount = this.discountRepository.findDiscountByStudentId(student.getId());
                final int discountAmount = discount.getDiscountAmount();
                totalAmount = totalAmount - discountAmount;
                int outStandingFees = totalAmount - paymentRequestDTO.getPayAmount();
                updateFees(totalAmount == paymentRequestDTO.getPayAmount(), outStandingFees, paymentRequestDTO.getStudentId());
                payment.setTotalAmount(totalAmount);
            }
            payment.setStudent(student);
            payment.setPaymentType(paymentRequestDTO.getPaymentType());
            payment.setPayAmount(paymentRequestDTO.getPayAmount());
            payment.setCreatedBy(authenticationService.getUserName());
            payment.setUpdatedBy(authenticationService.getUserName());
            return new ResponseDTO(Constants.CREATED, this.paymentRepository.save(payment), HttpStatus.CREATED.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    @Transactional
    public ResponseDTO retrievePayAmount(final String studentId) {
        final List<Payment> paidAmount = this.paymentRepository.retrieveByStudentId(studentId);
        int studentPaidAmount = paidAmount.stream().mapToInt(Payment::getPayAmount).sum();
        return new ResponseDTO(Constants.RETRIEVED, studentPaidAmount, HttpStatus.OK.getReasonPhrase());
    }

    @Transactional
    public ResponseDTO retrieveUnPaidAmount(final String studentId) {
        final Student student = this.studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(Constants.ID_NOT_FOUND));
        final Integer unPaidAmount = student.getOutstandingFees();
        return new ResponseDTO(Constants.RETRIEVED, unPaidAmount, HttpStatus.OK.getReasonPhrase());
    }

    public ResponseDTO retrieve(final String id) {
        final Payment payment = this.paymentRepository.findById(id).orElseThrow(() -> new BadServiceAlertException(Constants.PAYMENT_ID));
        if (!payment.isDelete()) {
            return new ResponseDTO(Constants.RETRIEVED, payment, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_NOT_AVAILABLE);
        }
    }

    public ResponseDTO retrieveAll() {
        return new ResponseDTO(Constants.RETRIEVED, this.paymentRepository.retrieveAll(), HttpStatus.OK.getReasonPhrase());
    }

    public ResponseDTO remove(final String id) {
        final Payment payment = this.paymentRepository.findById(id).orElseThrow(() -> new BadServiceAlertException(Constants.PAYMENT_ID));
        if (!payment.isDelete()) {
            payment.setDelete(true);
            return new ResponseDTO(Constants.DELETED, this.paymentRepository.save(payment), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    @Transactional
    public void updateFees(final boolean paidStatus, final int outStandingFees, final String studentId) {
        this.studentRepository.updateStatusAndRemainingFees(paidStatus, outStandingFees, studentId);
    }

    public ResponseDTO retrieveUnPaidFeesTypeStatus(final String studentId, final String feesType) {
        final Student student = this.studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(Constants.ID_NOT_FOUND));
        if (!student.isDelete()) {
            int feesTypeAmount = this.feesRepository.getStudentFeesTypeAmount(student.getStandard().getId(), feesType);
            if (feesType.equals(Constants.TUTION_FEES)) {
                final int scholarShipAmount = this.scholarShipRepository.findScholarShipAmountByStudentId(student.getId());
                final Discount discount = this.discountRepository.findDiscountByStudentId(student.getId());
                final int discountAmount = discount.getDiscountAmount();
                feesTypeAmount = feesTypeAmount - scholarShipAmount + discountAmount;
            }
            List<Integer> feesTypeStudentPayAmount = this.paymentRepository.getStudentPaymentByFeesType(studentId, feesType);
            final int feesTypePayAmount = feesTypeStudentPayAmount.stream().mapToInt(Integer::intValue).sum();
            final int studentUnPaidFeesTypeAmount = feesTypeAmount - feesTypePayAmount;
            return new ResponseDTO(Constants.RETRIEVED, studentUnPaidFeesTypeAmount, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO retrievePaidFeesTypeStatus(final String studentId, final String feesType) {
        final Student student = this.studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(Constants.ID_NOT_FOUND));
        if (!student.isDelete()) {
            List<Integer> feesTypeStudentPayAmount = this.paymentRepository.getStudentPaymentByFeesType(studentId, feesType);
            final int feesTypePayAmount = feesTypeStudentPayAmount.stream().mapToInt(Integer::intValue).sum();
            return new ResponseDTO(Constants.RETRIEVED, feesTypePayAmount, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public Page<Payment> getUsers(int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Order.by(sortBy));
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return paymentRepository.findByIsDeleteFalse(pageable);
    }
}
