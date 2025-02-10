package com.schoolworld.feesPayment.service;

import com.schoolworld.feesPayment.dto.DiscountRequestDTO;
import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.entity.Discount;
import com.schoolworld.feesPayment.entity.Student;
import com.schoolworld.feesPayment.exception.BadServiceAlertException;
import com.schoolworld.feesPayment.exception.StudentNotFoundException;
import com.schoolworld.feesPayment.repository.DiscountRepository;
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
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Transactional
    public ResponseDTO create(@RequestBody final DiscountRequestDTO discountRequestDTO) {
        final Discount discount = new Discount();
        final Student student = this.studentRepository.findById(discountRequestDTO.getStudentId()).orElseThrow(() -> new StudentNotFoundException(Constants.STUDENT_ID));
        if (!student.isDelete()) {
            discount.setStudent(student);
            discount.setIsDiscount(discountRequestDTO.getIsDiscount());
            discount.setDiscountAmount(discountRequestDTO.getDiscountAmount());
            discount.setCreatedBy(authenticationService.getUserName());
            discount.setUpdatedBy(authenticationService.getUserName());
            return new ResponseDTO(Constants.CREATED, this.discountRepository.save(discount), HttpStatus.CREATED.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    @Transactional
    public ResponseDTO update(final String id, final DiscountRequestDTO discountRequestDTO) {
        final Discount discount = this.discountRepository.findById(id).orElseThrow(() -> new BadServiceAlertException(Constants.DISCOUNT_ID));
        if (!discount.isDelete()) {
            if (discountRequestDTO.getIsDiscount()) {
                discount.setIsDiscount(discountRequestDTO.getIsDiscount());
            }
            if (discountRequestDTO.getDiscountAmount() > 0) {
                discount.setDiscountAmount(discountRequestDTO.getDiscountAmount());
            }
            if (authenticationService.getUserName() != null) {
                discount.setUpdatedBy(authenticationService.getUserName());
            }
            return new ResponseDTO(Constants.UPDATED, this.discountRepository.save(discount), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO retrieve(final String id) {
        final Discount discount = this.discountRepository.findById(id).orElseThrow(() -> new BadServiceAlertException(Constants.DISCOUNT_ID));
        if (!discount.isDelete()) {
            return new ResponseDTO(Constants.RETRIEVED, discount, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO retrieveAll() {
        return new ResponseDTO(Constants.RETRIEVED, this.discountRepository.retrieveAll(), HttpStatus.OK.getReasonPhrase());
    }

    public ResponseDTO remove(final String id) {
        final Discount discount = this.discountRepository.findById(id).orElseThrow(() -> new BadServiceAlertException(Constants.DISCOUNT_ID));
        if (!discount.isDelete()) {
            discount.setDiscount(true);
            return new ResponseDTO(Constants.DELETED, this.discountRepository.save(discount), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO studentDiscountAmount(final String studentId) {
        final Discount discount = this.discountRepository.findDiscountByStudentId(studentId);
        if (!discount.isDelete()) {
            final int discountAmount = discount.getDiscountAmount();
            return new ResponseDTO(Constants.RETRIEVED, discountAmount, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public Page<Discount> getUsers(int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Order.by(sortBy));
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return discountRepository.findByIsDeleteFalse(pageable);
    }
}
