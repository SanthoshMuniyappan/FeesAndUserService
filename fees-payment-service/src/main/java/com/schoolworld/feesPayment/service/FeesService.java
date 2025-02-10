package com.schoolworld.feesPayment.service;

import com.schoolworld.feesPayment.dto.FeesRequestDTO;
import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.entity.Discount;
import com.schoolworld.feesPayment.entity.Fees;
import com.schoolworld.feesPayment.entity.Standard;
import com.schoolworld.feesPayment.entity.Student;
import com.schoolworld.feesPayment.exception.BadServiceAlertException;
import com.schoolworld.feesPayment.exception.FeesNotFoundException;
import com.schoolworld.feesPayment.exception.StandardNotFoundException;
import com.schoolworld.feesPayment.exception.StudentNotFoundException;
import com.schoolworld.feesPayment.repository.DiscountRepository;
import com.schoolworld.feesPayment.repository.FeesRepository;
import com.schoolworld.feesPayment.repository.ScholarShipRepository;
import com.schoolworld.feesPayment.repository.StandardRepository;
import com.schoolworld.feesPayment.repository.StudentRepository;
import com.schoolworld.feesPayment.util.AuthenticationService;
import com.schoolworld.feesPayment.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeesService {

    @Autowired
    private FeesRepository feesRepository;

    @Autowired
    private StandardRepository standardRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScholarShipRepository scholarShipRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Transactional
    public ResponseDTO create(final FeesRequestDTO feesRequestDTO) {
        final Standard standard = this.standardRepository.findById(feesRequestDTO.getStandardId()).orElseThrow(() -> new StandardNotFoundException(Constants.STANDARD_ID));
        final Fees fees = new Fees();
        if (!standard.isDelete()) {
            fees.setStandard(standard);
            fees.setFeesCategory(feesRequestDTO.getFeesCategory());
            fees.setFeesAmount(feesRequestDTO.getFeesAmount());
            fees.setCreatedBy(authenticationService.getUserName());
            fees.setUpdatedBy(authenticationService.getUserName());
            return new ResponseDTO(Constants.CREATED, this.feesRepository.save(fees), HttpStatus.CREATED.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    @Transactional
    public ResponseDTO update(final String id, final FeesRequestDTO feesRequestDTO) {
        final Fees fees = this.feesRepository.findById(id).orElseThrow(() -> new FeesNotFoundException(Constants.FEES_ID));
        final int feesAmount = feesRequestDTO.getFeesAmount();
        if (!fees.isDelete()) {
            if (feesAmount > 0) {
                fees.setFeesAmount(feesRequestDTO.getFeesAmount());
            }
            if (authenticationService.getUserName() != null) {
                fees.setUpdatedBy(authenticationService.getUserName());
            }
            return new ResponseDTO(Constants.UPDATED, this.feesRepository.save(fees), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_NOT_AVAILABLE);
        }
    }

    public ResponseDTO retrieve(final String id) {
        final Fees fees = this.feesRepository.findById(id).orElseThrow(() -> new FeesNotFoundException(Constants.FEES_ID));
        if (!fees.isDelete()) {
            return new ResponseDTO(Constants.RETRIEVED, fees, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO retrieveAll() {
        return new ResponseDTO(Constants.RETRIEVED, this.feesRepository.retrieveAll(), HttpStatus.OK.getReasonPhrase());
    }

    public ResponseDTO remove(final String id) {
        final Fees fees = this.feesRepository.findById(id).orElseThrow(() -> new FeesNotFoundException(Constants.FEES_ID));
        if (!fees.isDelete()) {
            fees.setDelete(true);
            return new ResponseDTO(Constants.DELETED, this.feesRepository.save(fees), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO studentFees(final String studentId, final String feesType) {
        final Student student = this.studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(Constants.STUDENT_ID));
        if (!student.isDelete()) {
            final String standardId = student.getStandard().getId();
            int fees = this.feesRepository.StudentFees(standardId, feesType);
            if (feesType.equals(Constants.TUTION_FEES)) {
                final int scholarShipAmount = this.scholarShipRepository.findScholarShipAmountByStudentId(student.getId());
                final Discount discount = this.discountRepository.findDiscountByStudentId(student.getId());
                final int discountAmount = discount.getDiscountAmount();
                fees = fees - scholarShipAmount + discountAmount;
            }
            return new ResponseDTO(Constants.RETRIEVED, fees, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO standardAllFeesTypes(final String standardId) {
        final Standard standard = this.standardRepository.findById(standardId).orElseThrow(() -> new StandardNotFoundException(Constants.STANDARD_ID));
        final List<Fees> feesTypes = this.feesRepository.findAllByStandard(standardId);
        return new ResponseDTO(Constants.RETRIEVED, feesTypes, HttpStatus.OK.getReasonPhrase());
    }
}
